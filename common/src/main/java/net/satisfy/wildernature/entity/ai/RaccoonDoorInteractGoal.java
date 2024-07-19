package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.entity.RaccoonEntity;
import net.satisfy.wildernature.entity.animation.RaccoonAnimation;

public class RaccoonDoorInteractGoal extends DoorInteractGoal {

    private final RaccoonEntity raccoon;
    int counter = 0;

    public RaccoonDoorInteractGoal(RaccoonEntity raccoon) {
        super(raccoon);
        this.raccoon = raccoon;
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void start() {
        counter = 0;
        super.start();
        raccoon.startOpenDoorAnim();
    }

    @Override
    public boolean canContinueToUse() {
        return counter > 0 && counter < RaccoonAnimation.opening_door_length && (!isOpen() || counter >= RaccoonAnimation.opening_door_tick);
    }

    @Override
    public void tick() {
        if (canContinueToUse()) {
            raccoon.startOpenDoorAnim();
        } else {
            raccoon.stopOpenDoorAnim();
        }
        if (counter < RaccoonAnimation.opening_door_length) {
            counter++;
        }
        if (counter == RaccoonAnimation.opening_door_tick) {
            setOpen(true);
        }
        if (counter == RaccoonAnimation.opening_door_length) {
            super.tick();
        }
    }

    @Override
    public void stop() {
        counter = 0;
        super.stop();
        setOpen(true);
        raccoon.stopOpenDoorAnim();
    }
}
