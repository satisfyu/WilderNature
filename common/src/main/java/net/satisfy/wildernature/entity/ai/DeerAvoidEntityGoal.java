package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.satisfy.wildernature.entity.DeerEntity;

public class DeerAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
    private final DeerEntity deer;

    public DeerAvoidEntityGoal(DeerEntity deer, Class<T> tClass) {
        super(deer, tClass, 16.0F, 2, 2);
        this.deer = deer;
    }

    @Override
    public boolean canUse() {
        if (this.toAvoid instanceof Player && this.toAvoid.isCrouching()) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public void start() {
        deer.startRunningAnim();
        super.start();
    }

    @Override
    public void stop() {
        deer.stopRunningAnim();
        super.stop();
    }
}
