package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.satisfy.wildernature.WilderNature;

public class AnimationAttackGoal extends MeleeAttackGoal {
    private final EntityWithAttackAnimation animationEntity;
    private int counter;
    private int attackDelay;
    private int attackFrame;

    public AnimationAttackGoal(EntityWithAttackAnimation pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, int attackDelay, int attackFrame) {
        super((PathfinderMob) pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.attackDelay = attackDelay;
        this.attackFrame = attackFrame;
        if(!(pMob instanceof PathfinderMob)){
            throw new IllegalArgumentException("mob for AttackAnimationGoal must be an instance of PathfinderMob");
        }
        animationEntity = pMob;
    }

    @Override
    public void tick() {
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
            if (counter == attackFrame) {
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