package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.satisfy.wildernature.entity.DogEntity;
import net.satisfy.wildernature.entity.animation.DogAnimation;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Random;

public class DogHowlGoal extends Goal {
    private final DogEntity target;
    int counter;

    public DogHowlGoal(DogEntity mob) {
        this.target = mob;
        setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }


    @Override
    public boolean canUse() {
        var r = new Random().nextFloat();
        return r < 0.001f;
    }

    @Override
    public boolean canContinueToUse() {
        return counter > 0 && counter < DogAnimation.howl.lengthInSeconds()*20;
    }

    @Override
    public void tick() {
        counter++;
    }

    public static final AttributeModifier modifier = new AttributeModifier("bison_roll_do_not_move", -1000, AttributeModifier.Operation.ADDITION);

    @Override
    public void start() {
        counter = 0;
        Objects.requireNonNull(target.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(modifier);
        target.setHowling(true);
        super.start();
    }

    @Override
    public void stop() {
        Objects.requireNonNull(target.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(modifier);
        target.setHowling(false);
        super.stop();
    }
}
