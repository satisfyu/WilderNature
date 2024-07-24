package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.satisfy.wildernature.entity.MiniSheepEntity;
import net.satisfy.wildernature.entity.PelicanEntity;
import net.satisfy.wildernature.entity.TurkeyEntity;

public class CritterAttackGoal extends MeleeAttackGoal {
    private final PathfinderMob entity;
    private int attackDelay = 0;
    private int ticksUntilNextAttack = 80;
    private boolean shouldCountTillNextAttack = false;

    public CritterAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 0;
        ticksUntilNextAttack = 80;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if (isTimeToStartAttackAnimation()) {
                setEntityAttacking(true);
            }

            if (isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            setEntityAttacking(false);
            resetAttackAnimationTimeout();
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= attackDelay;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    public void stop() {
        setEntityAttacking(false);
        super.stop();
    }

    private void setEntityAttacking(boolean attacking) {
        if (entity instanceof PelicanEntity) {
            ((PelicanEntity) entity).setAttacking(attacking);
        } else if (entity instanceof TurkeyEntity) {
            ((TurkeyEntity) entity).setAttacking(attacking);
        } else if (entity instanceof MiniSheepEntity) {
            ((MiniSheepEntity) entity).setAttacking(attacking);
        }
    }

    private void resetAttackAnimationTimeout() {
        if (entity instanceof PelicanEntity) {
            ((PelicanEntity) entity).attackAnimationTimeout = 0;
        } else if (entity instanceof TurkeyEntity) {
            ((TurkeyEntity) entity).attackAnimationTimeout = 0;
        }
        else if (entity instanceof MiniSheepEntity) {
            ((MiniSheepEntity) entity).attackAnimationTimeout = 50;
        }
    }
}
