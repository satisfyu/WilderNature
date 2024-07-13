package net.satisfy.wildernature.entity.ai;

import dev.architectury.platform.Platform;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.entity.RaccoonEntity;

import java.util.EnumSet;

public class RaccoonWashingGoal extends Goal {
    private final RaccoonEntity target;

    public RaccoonWashingGoal(RaccoonEntity mob) {
        this.target = mob;
        setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.target.getRandom().nextFloat() < 0.01F;
    }

    @Override
    public void tick() {
        WilderNature.infoDebug("Washing goal");
        super.tick();
    }

    @Override
    public void start() {
        WilderNature.infoDebug("start washing goal");
        target.startWash();
        super.start();
    }

    @Override
    public void stop() {
        WilderNature.infoDebug("stop washing goal");
        super.stop();
    }
}
