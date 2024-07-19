package net.satisfy.wildernature.entity.ai;

import dev.architectury.platform.Platform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.entity.RaccoonEntity;
import net.satisfy.wildernature.entity.animation.RaccoonAnimation;
import org.spongepowered.asm.mixin.injection.At;

import java.util.EnumSet;
import java.util.Random;

public class RaccoonWashingGoal extends Goal {
    private final RaccoonEntity target;
    int counter;

    public RaccoonWashingGoal(RaccoonEntity mob) {
        this.target = mob;
        setFlags(EnumSet.of(Flag.LOOK));
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
        return r < 0.01f && !target.isRaccoonRunning();
    }

    @Override
    public boolean canContinueToUse() {
        return counter > 0 && counter < 48 && !target.isRaccoonRunning();
    }

    @Override
    public void tick() {
        WilderNature.infoDebug("Washing goal %d".formatted(counter));
        counter++;

    }

    AttributeModifier modifier = new AttributeModifier("racoon_wash_do_not_move",0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    @Override
    public void start() {
        counter=0;
        WilderNature.infoDebug("start washing goal");
        target.startWash();
        target.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(modifier);
        super.start();
    }

    @Override
    public void stop() {
        WilderNature.infoDebug("stop washing goal");
        target.stopWash();
        target.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(modifier);
        super.stop();
    }
}
