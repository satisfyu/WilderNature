package net.satisfy.wildernature.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.satisfy.wildernature.entity.ai.DeerAvoidEntityGoal;
import net.satisfy.wildernature.entity.ai.DeerEatingGoal;
import net.satisfy.wildernature.entity.ai.DeerLookAroundGoal;
import net.satisfy.wildernature.registry.EntityRegistry;
import net.satisfy.wildernature.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeerEntity extends Animal {
    private static final int FLAG_RUNNING = 0x00000100;
    private static final int FLAG_EATING = 0x00010000;
    private static final int FLAG_LOOKING_AROUND = 0x00000010;
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID;
    private static final EntityDataAccessor<Integer> DATA_FLAGS_ID;

    static {
        DATA_TYPE_ID = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.INT);
        DATA_FLAGS_ID = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.INT);
    }

    public final AnimationState idleState = new AnimationState();
    public final AnimationState lookAroundState = new AnimationState();
    public final AnimationState eatingState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public int globalCooldown = 0;

    public DeerEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.@NotNull Builder createMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.27000001192092896).add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ATTACK_DAMAGE, 1.5);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, 0);
        this.entityData.define(DATA_FLAGS_ID, 0);
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
    protected void registerGoals() {
        int i = 0;
        this.goalSelector.addGoal(++i, new DeerAvoidEntityGoal<>(this, Villager.class));
        this.goalSelector.addGoal(++i, new DeerAvoidEntityGoal<>(this, Pillager.class));
        this.goalSelector.addGoal(++i, new DeerAvoidEntityGoal<>(this, Player.class));
        this.goalSelector.addGoal(++i, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(Items.GRASS), false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new DeerEatingGoal(this));
        this.goalSelector.addGoal(7, new DeerLookAroundGoal(this));
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return this.isBaby() ? entityDimensions.height * 0.4F : entityDimensions.height * 0.5F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundRegistry.DEER_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundRegistry.DEER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.DEER_DEATH.get();
    }

    @Nullable
    @Override
    public DeerEntity getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return EntityRegistry.DEER.get().create(serverLevel);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.GRASS);
    }

    public void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    private void setFlag(int i, boolean bl) {
        if (bl) {
            this.entityData.set(DATA_FLAGS_ID, (this.entityData.get(DATA_FLAGS_ID) | i));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (this.entityData.get(DATA_FLAGS_ID) & ~i));
        }
    }

    private boolean getFlag(int i) {
        return (this.entityData.get(DATA_FLAGS_ID) & i) != 0;
    }

    public void startEating() {
        this.setFlag(FLAG_EATING, true);
    }

    public void stopEating() {
        this.setFlag(FLAG_EATING, false);
    }

    public boolean isEating() {
        return getFlag(FLAG_EATING);
    }

    public void startLookingAround() {
        this.setFlag(FLAG_LOOKING_AROUND, true);
    }

    public void stopLookingAround() {
        this.setFlag(FLAG_LOOKING_AROUND, false);
    }

    public boolean isLookingAround() {
        return getFlag(FLAG_LOOKING_AROUND);
    }

    public boolean isDeerRunning() {
        return getFlag(FLAG_RUNNING);
    }

    public void startRunningAnim() {
        this.setFlag(FLAG_RUNNING, true);
    }

    public void stopRunningAnim() {
        this.setFlag(FLAG_RUNNING, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (globalCooldown > 0) {
            globalCooldown--;
        }
        this.eatingState.animateWhen(isEating(), this.tickCount);
        this.lookAroundState.animateWhen(isLookingAround(), this.tickCount);
        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }
    }
}
