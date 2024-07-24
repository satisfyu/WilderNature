package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.satisfy.wildernature.entity.BisonEntity;

public class BisonAttackGoal extends MeleeAttackGoal {
    private final BisonEntity bison;
    private int counter;
    private int attackDelay = 40;
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
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity targetEntity, double discanceSqr) {
        if (targetEntity.getPosition(0).distanceToSqr(bison.getPosition(0))<discanceSqr) {
            if (counter == attackFrame) {
                this.bison.doHurtTarget(targetEntity);
            }
            counter++;
        }

        if(counter>=attackDelay){
            counter=0;
        }

        bison.setAttacking(counter!=0);
    }
}