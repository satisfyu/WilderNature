package net.satisfy.wildernature.entity.ai;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.level.BlockGetter;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.entity.RaccoonEntity;
import net.satisfy.wildernature.entity.animation.RaccoonAnimation;

import java.util.EnumSet;

public class RaccoonDoorInteractGoal extends DoorInteractGoal {

    private final RaccoonEntity raccoon;
    public static final AttributeModifier modifier = new AttributeModifier("racoon_door_do_not_move",-1000, AttributeModifier.Operation.ADDITION);
    int counter = 0;

    public RaccoonDoorInteractGoal(RaccoonEntity raccoon) {
        super(raccoon);
        this.raccoon = raccoon;
        setFlags(EnumSet.of(Flag.LOOK,Flag.MOVE,Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void start() {
        counter = 0;
        raccoon.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(modifier);
        super.start();
        raccoon.startOpenDoorAnim();
    }

    @Override
    public boolean canContinueToUse() {
        return counter > 0 && counter < RaccoonAnimation.opening_door_length && (!isOpen() || counter >= RaccoonAnimation.opening_door_tick);
    }

    @Override
    public void tick() {
        raccoon.level().getBlockState(doorPos).getShape(raccoon.level().getChunk(doorPos),doorPos).bounds().getCenter();
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
        raccoon.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(modifier);
        counter = 0;
        super.stop();
        setOpen(true);
        raccoon.stopOpenDoorAnim();
    }
}
