package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.satisfy.wildernature.entity.BisonEntity;
import net.satisfy.wildernature.entity.animation.BisonAnimation;

public class BisonAttackGoal extends MeleeAttackGoal {
    private final BisonEntity bison;
    private int counter;
    private int attackDelay = (int) (BisonAnimation.attack.lengthInSeconds()*20)+5;
    private int attackFrame = 5;

    public BisonAttackGoal(BisonEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        bison = pMob;
    }

    @Override
    public void tick() {
        var target = bison.getTarget();
        if(target!=null){
            checkAndPerformAttack(target,bison.getMeleeAttackRangeSqr(target));
        }
        bison.setAttacking(counter!=0);

        if(counter!=0)
            counter++;
        if(counter>=attackDelay){
            counter=0;
        }
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity targetEntity, double discanceSqr) {
        if (targetEntity.getPosition(0).distanceToSqr(bison.getPosition(0))<discanceSqr) {
            counter++;
            if (counter == attackFrame) {
                this.bison.doHurtTarget(targetEntity);
            }
        }



    }

    @Override
    public void stop() {
        bison.setAttacking(false);
        super.stop();
    }
}