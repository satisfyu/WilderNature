package net.satisfy.wildernature.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.satisfy.wildernature.entity.ai.*;
import net.satisfy.wildernature.entity.animation.OwlAnimation;
import net.satisfy.wildernature.registry.EntityRegistry;
import net.satisfy.wildernature.registry.SoundRegistry;
import net.satisfy.wildernature.registry.TagsRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Predicate;


// Owl by Lemonszz: https://github.com/Lemonszz/Biome-Makeover/blob/1.20/LICENCE
public class OwlEntity extends ShoulderRidingEntity implements EntityWithAttackAnimation {
    private static final EntityDataAccessor<Integer> STANDING_STATE = SynchedEntityData.defineId(OwlEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> OWL_STATE = SynchedEntityData.defineId(OwlEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(OwlEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HOOTING = SynchedEntityData.defineId(OwlEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(OwlEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDimensions FLYING_DIMENSION = new EntityDimensions(0.7F, 1.4F, false);
    private static final Predicate<LivingEntity> IS_OWL_TARGET = e -> e.getType().is(TagsRegistry.OWL_TARGETS);

    private float leaningPitch;
    private float lastLeaningPitch;

    public OwlEntity(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);

        this.moveControl = new FlyingMoveControl(this, 0, false);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
    }

    public static boolean checkSpawnRules(EntityType<OwlEntity> owlEntityEntityType, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        BlockState state = level.getBlockState(pos.below());

        return ((state.is(Blocks.GRASS_BLOCK) || state.is(BlockTags.LEAVES))) && level.getRawBrightness(pos, 0) > 2;
    }


    public static AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.FLYING_SPEED, 0.8D).add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.4D).add(Attributes.ATTACK_DAMAGE, 2D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        int i = 0;
        this.goalSelector.addGoal(++i, new AvoidEntityGoal<>(this, Player.class, 32,2,2) {
            @Override
            public boolean canUse() {
                return super.canUse() && isInPanicRightNow();
            }
        });
        this.goalSelector.addGoal(++i, new PanicGoal(this, 1.75D){
            protected boolean findRandomPosition() {
                Vec3 vec3 = DefaultRandomPos.getPos(this.mob, 20, 20);
                if (vec3 == null) {
                    return false;
                } else {
                    this.posX = vec3.x;
                    this.posY = vec3.y;
                    this.posZ = vec3.z;
                    return true;
                }
            }
        });
        this.goalSelector.addGoal(++i, new SleepGoal(this));
        this.goalSelector.addGoal(++i, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(++i, new FloatGoal(this){
            @Override
            public boolean canUse() {
                return super.canUse() && !isSleeping();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isSleeping();
            }
        });
        this.goalSelector.addGoal(++i, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(++i, new AnimationAttackGoal(this, 1.0D, true, (int) OwlAnimation.attack.lengthInSeconds()*20,15){
            @Override
            public boolean canUse() {
                return super.canUse() && !isSleeping();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isSleeping();
            }
        });
        this.goalSelector.addGoal(++i, new FlyingFollowOwnerGoal(this, 1.2D, 10.0F, 2.0F, true){
            @Override
            public boolean canUse() {
                return super.canUse() && !isSleeping();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isSleeping();
            }
        });
        this.goalSelector.addGoal(++i, new PredicateTemptGoal(this, 1.2D, this::isFood, false){
            @Override
            public boolean canUse() {
                return super.canUse() && !isSleeping();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !isSleeping();
            }
        });
        this.goalSelector.addGoal(++i, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(++i, new ExtendedFlyOntoTree(this, 1D, 0.5F));
        this.goalSelector.addGoal(++i, new RandomStrollGoal(this, 1D));
        this.goalSelector.addGoal(++i, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(++i, new RandomActionGoal(new RandomAction() {


            @Override
            public boolean isInterruptable() {
                return false;
            }

            @Override
            public void onStop() {
                setHooting(false);
            }

            @Override
            public void onStart() {
                if (onGround()) {
                    setHooting(true);
                }
                var owlSound = SoundRegistry.OWL_AMBIENT.get();
                level().playSound(null, OwlEntity.this, owlSound, SoundSource.NEUTRAL, 1f, 1f);
            }

            @Override
            public boolean isPossible() {
                return !isAttacking() && !isSleeping();
            }

            @Override
            public int duration() {
                return (int) (OwlAnimation.hoot.lengthInSeconds() * 20);
            }

            @Override
            public float chance() {
                return 0.01f;
            }

            @Override
            public AttributeInstance getAttribute(Attribute movementSpeed) {
                return OwlEntity.this.getAttribute(movementSpeed);
            }
        }));
        //next line is for testing, comment it if it's not
        //this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<Player>(this, Player.class, true));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new NonTameRandomTargetGoal<>(this, LivingEntity.class, false, IS_OWL_TARGET));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        OwlEntity owl = EntityRegistry.OWL.get().create(serverLevel);
        UUID uUID = this.getOwnerUUID();
        if (uUID != null) {
            owl.setOwnerUUID(uUID);
            owl.setTame(true);
        }
        return owl;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, level);
        birdNavigation.setCanPassDoors(true);
        birdNavigation.setCanFloat(false);
        birdNavigation.setCanOpenDoors(false);
        return birdNavigation;
    }


    @Override
    public float getSwimAmount(float f) {
        return Mth.rotLerp(f, lastLeaningPitch, leaningPitch);
    }

    @Override
    public void tick() {
        super.tick();
        setStandingState(onGround() || isInWater() || isOrderedToSit() ? StandingState.STANDING : StandingState.FLYING);

        lastLeaningPitch = leaningPitch;
        switch (getStandingState()) {
            case STANDING -> this.leaningPitch = Math.max(0.0F, this.leaningPitch - 2F);
            case FLYING -> this.leaningPitch = Math.min(7F, this.leaningPitch + 1.5F);
        }
        if(level().isClientSide()){
            setupAnimationStates();
        }
    }

    public AnimationState flyingState = new AnimationState();
    public AnimationState hootState = new AnimationState();
    public AnimationState deathState = new AnimationState();
    public AnimationState attackState = new AnimationState();
    public AnimationState walkState = new AnimationState();
    public AnimationState sleepState = new AnimationState();
    public AnimationState idleState = new AnimationState();


    private void setupAnimationStates() {
        var owlState = getOwlState();
        var standingState = getStandingState();
        flyingState.animateWhen(standingState == StandingState.FLYING, this.tickCount);
        attackState.animateWhen(this.isAttacking(), this.tickCount);
        hootState.animateWhen(this.isHooting(), this.tickCount);
        sleepState.animateWhen(isSleeping(), this.tickCount);
        //idleState.animateWhen(true, this.tickCount);

    }

    private boolean isHooting() {
        return this.entityData.get(HOOTING);
    }
    private void setHooting(boolean hooting) {
        this.entityData.set(HOOTING,hooting);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Vec3 velocity = this.getDeltaMovement();
        if (!this.onGround() && velocity.y < 0.0D) {
            this.setDeltaMovement(velocity.multiply(1.0D, 0.75D, 1.0D));
        }
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);

        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
            this.setHealth(20.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6.0D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        }

    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        if (this.isTame()) {
            if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!level().isClientSide()) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                    this.heal((float) item.getFoodProperties().getNutrition());
                }
                return InteractionResult.SUCCESS;
            }

            InteractionResult actionResult = super.mobInteract(player, hand);
            if ((!actionResult.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
                if (!level().isClientSide()) {
                    setOrderedToSit(!isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget(null);
                }
                return InteractionResult.SUCCESS;
            }
            return actionResult;
        } else {
            if (isFood(stack) && this.getTarget() == null) {
                if (!level().isClientSide()) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                    if (this.random.nextInt(3) == 0) {
                        this.tame(player);
                        this.navigation.stop();
                        this.setTarget(null);
                        setOrderedToSit(true);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item.isEdible() && item.getFoodProperties().isMeat();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("OwlState", getEntityData().get(OWL_STATE));
        tag.putInt("StandingState", getEntityData().get(STANDING_STATE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setOwlState(OwlState.values()[tag.getInt("OwlState")]);
        setStandingState(StandingState.values()[tag.getInt("StandingState")]);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return getStandingState() == StandingState.STANDING ? super.getDimensions(pose) : FLYING_DIMENSION;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(STANDING_STATE, 0);
        getEntityData().define(OWL_STATE, 0);
        getEntityData().define(ATTACKING, false);
        getEntityData().define(HOOTING, false);
        getEntityData().define(SLEEPING, false);
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    public StandingState getStandingState() {
        return StandingState.values()[getEntityData().get(STANDING_STATE)];
    }

    public void setStandingState(StandingState standingState) {
        getEntityData().set(STANDING_STATE, standingState.ordinal());
    }

    public OwlState getOwlState() {
        return OwlState.values()[getEntityData().get(OWL_STATE)];
    }

    public void setOwlState(OwlState owlState) {
        getEntityData().set(OWL_STATE, owlState.ordinal());
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.OWL_DEATH.get();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.OWL_HURT.get();
    }

    @Override
    public void setAttacking(boolean b) {
        this.entityData.set(ATTACKING, b);
    }
    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    @Override
    public Vec3 getPosition(int i) {
        return super.getPosition(i);
    }

    @Override
    public void doHurtTarget(LivingEntity targetEntity) {
        super.doHurtTarget(targetEntity);
    }


    public enum StandingState {
        STANDING, FLYING
    }

    public enum OwlState {
        IDLE, ATTACKING
    }


    private static class ExtendedFlyOntoTree extends WaterAvoidingRandomStrollGoal {
        public  ExtendedFlyOntoTree(PathfinderMob pathAwareEntity, double speed, float probability) {
            super(pathAwareEntity, speed, probability);
        }

        @Override
        protected Vec3 getPosition() {
            Vec3 vec3d = null;
            if (this.mob.isInWaterOrBubble()) {
                vec3d = LandRandomPos.getPos(this.mob, 15, 7);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3d = this.getTreeTarget();
            }

            return vec3d == null ? super.getPosition() : vec3d;
        }

        private Vec3 getTreeTarget() {
            BlockPos blockPos = this.mob.getOnPos();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutable2 = new BlockPos.MutableBlockPos();
            Iterable<BlockPos> iterable = BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 3.0D), Mth.floor(this.mob.getY() - 6.0D), Mth.floor(this.mob.getZ() - 3.0D), Mth.floor(this.mob.getX() + 3.0D), Mth.floor(this.mob.getY() + 6.0D), Mth.floor(this.mob.getZ() + 3.0D));
            Iterator<BlockPos> var5 = iterable.iterator();

            BlockPos blockPos2;
            boolean bl;
            do {
                do {
                    if (!var5.hasNext()) {
                        return null;
                    }

                    blockPos2 = var5.next();
                }
                while (blockPos.equals(blockPos2));

                BlockState blockState = this.mob.level().getBlockState(mutable2.setWithOffset(blockPos2, Direction.DOWN));
                bl = blockState.getBlock() instanceof LeavesBlock || blockState.is(BlockTags.LOGS);
            }
            while (!bl || !this.mob.level().isEmptyBlock(blockPos2) || !this.mob.level().isEmptyBlock(mutable.setWithOffset(blockPos2, Direction.UP)));

            return Vec3.atBottomCenterOf(blockPos2);
        }
    }

    private void setSleeping(boolean b) {
        this.entityData.set(SLEEPING,b);
    }

    public boolean isInPanicRightNow(){
        return getLastHurtByMob() != null || isOnFire();
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    private static class SleepGoal extends Goal {
        private final OwlEntity owl;

        public SleepGoal(OwlEntity owlEntity) {
            this.owl = owlEntity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return owl.level().isDay() && !owl.isInPanicRightNow();
        }
        public boolean canContinueToUse() {
            return canUse();
        }



        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void start() {
            super.start();
            owl.setSleeping(true);
        }

        @Override
        public void tick() {
            super.tick();
        }
        public void stop() {
            super.stop();
            owl.setSleeping(false);
        }
    }

}