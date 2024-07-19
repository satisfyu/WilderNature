package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.satisfy.wildernature.entity.RaccoonEntity;

public class RaccoonAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
    private final RaccoonEntity raccoon;

    public RaccoonAvoidEntityGoal(RaccoonEntity raccoon, Class<T> tClass) {
        super(raccoon, tClass, 16.0F, 2, 2);
        this.raccoon = raccoon;
    }

    @Override
    public void start() {
        raccoon.startRunningAnim();
        super.start();
    }

    @Override
    public void stop() {
        raccoon.stopRunningAnim();
        super.stop();
    }
}
