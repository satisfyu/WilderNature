package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class AnimationAttackGoal extends MeleeAttackGoal {
    private final EntityWithAttackAnimation animationEntity;
    private int counter;
    private int attackDelay;
    private int attackTick;

    public AnimationAttackGoal(EntityWithAttackAnimation pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, int attackDelay, int attackTick) {
        super((PathfinderMob) pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.attackDelay = attackDelay;
        this.attackTick = attackTick;
        animationEntity = pMob;
    }

    @Override
    public void tick() {
        super.tick();
        var target = animationEntity.getTarget();
        if(target!=null){
            checkAndPerformAttack(target, animationEntity.getMeleeAttackRangeSqr(target));
        }
        animationEntity.setAttacking(counter!=0);

        if(counter!=0)
            counter++;
        if(counter>=attackDelay){
            counter=0;
        }
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity targetEntity, double discanceSqr) {
        if (targetEntity.getPosition(0).distanceToSqr(animationEntity.getPosition(0)) < discanceSqr) {
            if(counter==0){
                counter++;
            }
            if (counter == attackTick) {
                this.animationEntity.doHurtTarget(targetEntity);
            }
        }
    }

    @Override
    public void stop() {
        animationEntity.setAttacking(false);
        super.stop();
    }
}