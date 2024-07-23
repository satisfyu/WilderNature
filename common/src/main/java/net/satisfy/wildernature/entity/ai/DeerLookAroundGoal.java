package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.satisfy.wildernature.entity.DeerEntity;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Random;

public class DeerLookAroundGoal extends Goal {
    private final DeerEntity target;
    private int counter;
    private static final int LOOK_AROUND_DURATION_TICKS = (int) (3.5F * 20);
    private static final int COOLDOWN_TICKS = 400;

    public DeerLookAroundGoal(DeerEntity mob) {
        this.target = mob;
        setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean isInterruptable() {
        return true;
    }

    @Override
    public boolean canUse() {
        var r = new Random().nextFloat();
        return r < 0.01f && !target.isDeerRunning() && target.globalCooldown == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return counter < LOOK_AROUND_DURATION_TICKS && !target.isDeerRunning();
    }

    @Override
    public void tick() {
        counter++;
    }

    public static final AttributeModifier modifier = new AttributeModifier("deer_look_around_do_not_move", -1000, AttributeModifier.Operation.ADDITION);

    @Override
    public void start() {
        counter = 0;
        Objects.requireNonNull(target.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(modifier);
        target.startLookingAround();
        super.start();
    }

    @Override
    public void stop() {
        Objects.requireNonNull(target.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(modifier);
        target.stopLookingAround();
        target.globalCooldown = COOLDOWN_TICKS;
        super.stop();
    }
}
