package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.satisfy.wildernature.entity.BisonEntity;
import net.satisfy.wildernature.entity.RaccoonEntity;
import net.satisfy.wildernature.entity.animation.BisonAnimation;
import net.satisfy.wildernature.entity.animation.ServerAnimationDurations;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Random;

public class BisonRollingGoal extends Goal {
    private final BisonEntity target;
    int counter;

    public BisonRollingGoal(BisonEntity mob) {
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
        return r < 0.001f && !target.isAngry();
    }

    @Override
    public boolean canContinueToUse() {
        return counter > 0 && counter < ServerAnimationDurations.bison_roll *20;
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
        target.setRolling(true);
        super.start();
    }

    @Override
    public void stop() {
        Objects.requireNonNull(target.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(modifier);
        target.setRolling(false);
        super.stop();
    }
}
