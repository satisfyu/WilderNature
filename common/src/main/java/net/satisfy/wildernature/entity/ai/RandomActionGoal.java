package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.Random;

public class RandomActionGoal extends Goal {
    private final RandomAction action;
    int counter;

    public RandomActionGoal(RandomAction mob) {
        this.action = mob;
        setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean isInterruptable() {
        return action.isInterruptable();
    }


    @Override
    public boolean canUse() {
        var r = new Random().nextFloat();
        return r < action.chance() && action.isPossible();
    }

    @Override
    public boolean canContinueToUse() {
        return counter > 0 && counter < action.duration() && action.isPossible();
    }

    @Override
    public void tick() {
        counter++;
    }


    @Override
    public void start() {
        counter = 0;
        action.onStart();
        super.start();
    }

    @Override
    public void stop() {
        action.onStop();
        super.stop();
    }
}
