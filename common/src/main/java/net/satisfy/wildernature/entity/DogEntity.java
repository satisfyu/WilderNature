package net.satisfy.wildernature.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.wildernature.entity.ai.RandomAction;
import net.satisfy.wildernature.entity.ai.RandomActionGoal;
import net.satisfy.wildernature.entity.animation.DogAnimation;
import net.satisfy.wildernature.registry.EntityRegistry;
import net.satisfy.wildernature.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DogEntity extends TamableAnimal {
    public AnimationState walkAnimationState = new AnimationState();
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState howlingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private static final EntityDataAccessor<Boolean> HOWLING = SynchedEntityData.defineId(DogEntity.class, EntityDataSerializers.BOOLEAN);


    public DogEntity(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.@NotNull Builder createMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.MAX_HEALTH, 12.0).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Nullable
    public DogEntity getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return EntityRegistry.DOG.get().create(serverLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.25d, 18f, 7f, false));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(Items.BONE), false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new PanicGoal(this, 2.0D));
        if(1==1)
            return;
        this.goalSelector.addGoal(7, new RandomActionGoal(new RandomAction() {
            @Override
            public boolean isInterruptable() {
                return true;
            }

            @Override
            public void onStart() {
                //Replace SoundEvents.WOLF_HOWL to your sound
                setHowling(true);
            }

            @Override
            public void onStop() {
                setHowling(false);
            }
            @Override
            public boolean isPossible() {
                return true;
            }

            @Override
            public void onTick(int tick) {
                if(tick == 20){
                    level().playSound(null,DogEntity.this, SoundRegistry.RED_WOLF_AMBIENT.get(), SoundSource.NEUTRAL,1,1);
                }
            }

            @Override
            public int duration() {
                return (int) (DogAnimation.howl.lengthInSeconds()*20);
            }

            @Override
            public float chance() {
                return 0.005f;
            }

            @Override
            public AttributeInstance getAttribute(Attribute movementSpeed) {
                return DogEntity.this.getAttribute(movementSpeed);
            }
        }));
        this.targetSelector.addGoal(10, new HurtByTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        howlingAnimationState.animateWhen(this.isHowling(),tickCount);
    }



    private boolean isHowling() {
        return this.entityData.get(HOWLING);
    }

    public void setHowling(boolean howling) {
        this.entityData.set(HOWLING,howling);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOWLING, false);
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


    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundRegistry.RED_WOLF_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.RED_WOLF_DEATH.get();
    }

    protected float getSoundVolume() {
        return 0.3F;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.BONE);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return true;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.level().isClientSide()) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || itemstack.is(Items.BONE) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (this.isOwnedBy(player)) {
                    if (itemstack.is(Items.BONE)) {
                        if (this.getHealth() < this.getMaxHealth()) {
                            this.heal(2.0F);
                            this.gameEvent(GameEvent.ENTITY_INTERACT, this);
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.CONSUME;
                    } else {
                        InteractionResult interactionresult = super.mobInteract(player, hand);
                        if (!interactionresult.consumesAction() || this.isBaby()) {
                            this.setOrderedToSit(!this.isOrderedToSit());
                            this.jumping = false;
                            this.navigation.stop();
                            this.setTarget(null);
                            return InteractionResult.SUCCESS;
                        }

                        return interactionresult;
                    }
                }
            } else if (itemstack.is(Items.BONE)) {
                this.usePlayerItem(player, hand, itemstack);
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }

                return InteractionResult.SUCCESS;
            }

            return super.mobInteract(player, hand);
        }
    }

}
