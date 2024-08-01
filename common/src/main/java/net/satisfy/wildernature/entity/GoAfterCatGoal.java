package net.satisfy.wildernature.entity;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.satisfy.wildernature.WilderNature;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class GoAfterCatGoal extends Goal {
    private final DogEntity dog;
    private List<Cat> list;
    private int lastCatUpdate = 0;
    private Cat targetCat;

    public GoAfterCatGoal(DogEntity dogEntity) {
        this.setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK,Flag.TARGET));
        this.dog = dogEntity;
    }

    @Override
    public void start() {
        super.start();
    }


    @Override
    public void tick() {
        super.tick();
        if (this.targetCat != null) {
        }
    }

    private List<Cat> getCats() {
        if(this.list == null || dog.tickCount - lastCatUpdate >= 20){
            this.list = dog.level().getNearbyEntities(Cat.class, TargetingConditions.forNonCombat(),dog,dog.getBoundingBox().inflate(16));
            lastCatUpdate = dog.tickCount;
        }
        updateTargetCat();
        return this.list;
    }

    @Nullable
    void updateTargetCat(){
        if (this.targetCat == null || this.targetCat.distanceToSqr(this.dog) > 16 * 16) {
            double closestDistance = Double.MAX_VALUE;
            Cat closestCat = null;

            for (Cat cat : this.list) {
                double distance = cat.distanceToSqr(this.dog);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestCat = cat;
                }
            }

            this.targetCat = closestCat;
            if(closestCat != null){
                var path = this.dog.getNavigation().moveTo(this.targetCat, 1.5);
                WilderNature.info("{} {}",closestCat.getUUID(),path);
            }
        }
    }
    public boolean canContinueToUse() {
        return this.targetCat != null && this.targetCat.isAlive() && this.targetCat.distanceToSqr(this.dog) <= 16 * 16;
    }
    public void stop() {
        this.targetCat = null;
    }
    @Override
    public boolean canUse() {
        return getCats().size()>0;
    }
}
