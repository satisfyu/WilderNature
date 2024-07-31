package net.satisfy.wildernature.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.satisfy.wildernature.entity.ai.AnimationAttackGoal;
import net.satisfy.wildernature.entity.ai.EntityWithAttackAnimation;
import net.satisfy.wildernature.entity.animation.RedWolfAnimation;
import net.satisfy.wildernature.registry.EntityRegistry;
import net.satisfy.wildernature.registry.SoundRegistry;
import org.jetbrains.annotations.Nullable;

public class RedWolfEntity extends Wolf implements EntityWithAttackAnimation {
    public RedWolfEntity(EntityType<? extends Wolf> entityType, Level world) {
        super(entityType, world);
    }

    private static final EntityDataAccessor<Boolean> LEAPING = SynchedEntityData.defineId(RedWolfEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(RedWolfEntity.class, EntityDataSerializers.BOOLEAN);

    public static AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.MAX_HEALTH, 12.0).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return SoundRegistry.RED_WOLF_AGGRO.get();
        } else if (this.random.nextInt(3) == 0) {
            return this.isTame() && this.getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
        } else {
            return SoundRegistry.RED_WOLF_AMBIENT.get();
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5) {
            protected boolean shouldPanic() {
                return this.mob.isFreezing() || this.mob.isOnFire();
            }
        });
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal(this, Llama.class, 24.0F, 1.5, 1.5) {

            public boolean canUse() {
                if (super.canUse() && this.toAvoid instanceof Llama) {
                    return !RedWolfEntity.this.isTame() && this.avoidLlama((Llama) this.toAvoid);
                } else {
                    return false;
                }
            }

            private boolean avoidLlama(Llama llama) {
                return llama.getStrength() >= RedWolfEntity.this.random.nextInt(5);
            }

            public void start() {
                RedWolfEntity.this.setTarget(null);
                super.start();
            }

            public void tick() {
                RedWolfEntity.this.setTarget(null);
                super.tick();
            }
        });
//        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F){
//
//        });
//        this.goalSelector.addGoal(5,
//
//        });
        this.goalSelector.addGoal(5, new AnimationAttackGoal(this, 1.0, true,(int) (RedWolfAnimation.attack.lengthInSeconds()*20),4));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(9, new BegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, (entity -> isAngryAt((LivingEntity) entity))));
        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal(this, Animal.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, AbstractSkeleton.class, false));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
    }

    public AnimationState attackState = new AnimationState();
    @Override
    public void tick() {
        super.tick();
        setSneaking(getTarget()!=null);
        if(level().isClientSide()){
            attackState.animateWhen(isAttacking(),this.tickCount);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEAPING, false);
        this.entityData.define(ATTACKING, false);
    }

    private void setSneaking(boolean b) {
        this.entityData.set(LEAPING,b);
    }
    public boolean isSneaking(){
        return this.entityData.get(LEAPING);
    }

    @Nullable
    public RedWolfEntity getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return EntityRegistry.RED_WOLF.get().create(serverLevel);
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


    public int getMaxHeadXRot() {
        return this.isInSittingPose() ? 16 : super.getMaxHeadXRot();
    }


    public float getTailAngle() {
        if (this.isAngry()) {
            return 1.5393804F;
        } else {
            return this.isTame() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * 3.1415927F : 0.62831855F;
        }
    }

    public boolean isAttacking(){
        return this.entityData.get(ATTACKING);
    }
    @Override
    public void setAttacking(boolean b) {
        this.entityData.set(ATTACKING,b);
    }

    @Override
    public Vec3 getPosition(int i) {
        return super.getPosition(i);
    }

    @Override
    public void doHurtTarget(LivingEntity targetEntity) {
        super.doHurtTarget(targetEntity);
    }
}