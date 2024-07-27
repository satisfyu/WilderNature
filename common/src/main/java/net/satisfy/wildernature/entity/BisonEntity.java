package net.satisfy.wildernature.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.satisfy.wildernature.entity.ai.AnimationAttackGoal;
import net.satisfy.wildernature.entity.ai.EntityWithAttackAnimation;
import net.satisfy.wildernature.entity.ai.BisonRollingGoal;
import net.satisfy.wildernature.entity.animation.BisonAnimation;
import net.satisfy.wildernature.registry.EntityRegistry;
import net.satisfy.wildernature.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

//TODO: Add rolling animation / bison doesnt fight back when hit 
public class BisonEntity extends Animal implements EntityWithAttackAnimation {
    private static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.INT);
    private static final UniformInt ANGER_RANGE = TimeUtil.rangeOfSeconds(15, 34);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ROLLING = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Long> LAST_HURT_TIME = SynchedEntityData.defineId(BisonEntity.class, EntityDataSerializers.LONG);
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public AnimationState rollingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public int attackAnimationTimeout = 0;
    private UUID lastHurtBy;

    public BisonEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    public static @NotNull AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 28.0)
                .add(Attributes.ATTACK_DAMAGE, 1.5F)
                .add(Attributes.ATTACK_SPEED, 1.25)
                .add(Attributes.MOVEMENT_SPEED, 0.1800009838F)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0177774783F)
                .add(Attributes.ATTACK_KNOCKBACK, 2F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
        if (this.isAngry() && this.level().getGameTime() - this.getLastHurtTime() > 300) {
            this.setAngry(false);
        }
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        attackAnimationState.animateWhen(this.isAttacking(),tickCount);
        rollingAnimationState.animateWhen(this.isRolling(),tickCount);

//        if(this.isAttacking() && attackAnimationTimeout <= 0) {
//            attackAnimationTimeout = 80;
//            attackAnimationState.start(this.tickCount);
//        } else {
//            --this.attackAnimationTimeout;
//        }
//
//        if(!this.isAttacking()) {
//            attackAnimationState.stop();
//        }
    }

    private boolean isRolling() {
        return this.entityData.get(ROLLING);
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    @Override
    public Vec3 getPosition(int i) {
        return super.getPosition(i);
    }

    @Override
    public void doHurtTarget(LivingEntity targetEntity) {
        super.doHurtTarget(targetEntity);
    }

    public boolean isAngry() {
        return this.entityData.get(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.entityData.set(ANGRY, angry);
    }

    public long getLastHurtTime() {
        return this.entityData.get(LAST_HURT_TIME);
    }

    public void setLastHurtTime(long time) {
        this.entityData.set(LAST_HURT_TIME, time);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(ROLLING, false);
        this.entityData.define(ANGER_TIME, 0);
        this.entityData.define(ANGRY, false);
        this.entityData.define(LAST_HURT_TIME, 0L);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AnimationAttackGoal(this, 1.0D, true,(int) (BisonAnimation.attack.lengthInSeconds()*20)+5,5));
        this.goalSelector.addGoal(1, new BisonPanicGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.GRASS), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.3F));
        this.goalSelector.addGoal(9, new MeleeAttackGoal(this, 1.4D, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new BisonRollingGoal(this));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return EntityRegistry.BISON.get().create(pLevel);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAngry() ? SoundRegistry.BISON_ANGRY.get() : SoundRegistry.BISON_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundRegistry.BISON_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.BISON_DEATH.get();
    }

    @Override
    public int getMaxHeadYRot() {
        return 35;
    }

    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(ANGER_TIME, time);
    }

    public UUID getPersistentAngerTarget() {
        return this.lastHurtBy;
    }

    public void setPersistentAngerTarget(UUID target) {
        this.lastHurtBy = target;
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_RANGE.sample(this.random));
        System.out.println(this.getRemainingPersistentAngerTime()); // TODO fix this not hitting
    }

    public void setRolling(boolean rolling) {
        this.entityData.set(ROLLING,rolling);
    }

//    @Override
//    public boolean isAngryAt(LivingEntity entity) {
//
//        try (Level level = entity.level()) {
//            System.out.println(level.isClientSide);
//        }
//        catch (IOException e) {
//            System.out.println(e.getMessage());
//        } // TODO remove this try-catch
//
//        return entity instanceof Player && this.isAngry() && this.shouldBeAngry();
//    }
//
//    private boolean shouldBeAngry() {
//        if (this.getHealth() < this.getMaxHealth() * 0.25) {
//            return false;
//        }
//        return this.getCommandSenderWorld().getGameTime() - this.getLastHurtTime() < 300;
//    }

//    @Override
//    public boolean hurt(DamageSource source, float amount) {
//        if (super.hurt(source, amount)) {
//            if (source.getEntity() instanceof Player) {
//                this.setLastHurtTime(this.level().getGameTime());
//                this.setAngry(true);
//                this.setPersistentAngerTarget(source.getEntity().getUUID());
//            }
//            return true;
//        }
//        return false;
//    }

    static class BisonPanicGoal extends PanicGoal {
        public BisonPanicGoal(BisonEntity bison) {
            super(bison, 1.2D);
        }

        @Override
        public boolean canUse() {
            return this.mob.isBaby() && super.canUse();
        }
    }


}
