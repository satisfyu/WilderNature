package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface EntityWithAttackAnimation {
    LivingEntity getTarget();

    double getMeleeAttackRangeSqr(LivingEntity target);

    void setAttacking(boolean b);

    Vec3 getPosition(int i);

    void doHurtTarget(LivingEntity targetEntity);
}
