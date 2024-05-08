package satisfy.wildernature.entity.ai;

import java.util.EnumSet;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

public class DigIntoGrassGoal extends Goal {
    private static final int EAT_ANIMATION_TICKS = 40;
    private static final BlockStatePredicate IS_GRASS_BLOCK = BlockStatePredicate.forBlock(Blocks.GRASS_BLOCK);
    private final Mob mob;
    private final Level level;
    private int eatAnimationTick;

    public DigIntoGrassGoal(Mob mob) {
        this.mob = mob;
        this.level = mob.level();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    public boolean canUse() {
        if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos blockPos = this.mob.blockPosition();
            return IS_GRASS_BLOCK.test(this.level.getBlockState(blockPos.below()));
        }
    }

    public void start() {
        this.eatAnimationTick = this.adjustedTickDelay(EAT_ANIMATION_TICKS);
        this.level.broadcastEntityEvent(this.mob, (byte)10);
        this.mob.getNavigation().stop();
    }

    public void stop() {
        this.eatAnimationTick = 0;
    }

    public boolean canContinueToUse() {
        return this.eatAnimationTick > 0;
    }

    public int getEatAnimationTick() {
        return this.eatAnimationTick;
    }

    public void tick() {
        this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        if (this.eatAnimationTick == this.adjustedTickDelay(4)) {
            BlockPos blockPos = this.mob.blockPosition().below();
            if (IS_GRASS_BLOCK.test(this.level.getBlockState(blockPos))) {
                if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    this.level.levelEvent(2001, blockPos, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                    this.level.setBlock(blockPos, Blocks.COARSE_DIRT.defaultBlockState(), 2);
                    spawnEatParticlesAndDrops((ServerLevel) this.level, blockPos);
                }
                this.mob.ate();
            }
        }
    }

    private void spawnEatParticlesAndDrops(ServerLevel serverLevel, BlockPos blockPos) {
        BlockState blockState = Blocks.GRASS_BLOCK.defaultBlockState();
        serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5,
                10, 0.25, 0.25, 0.25, 0.5);

        Random random = new Random();
        int chance = random.nextInt(100);
        if (chance < 10) {
            spawnItem(serverLevel, blockPos, new ItemStack(Items.BROWN_MUSHROOM));
        } else if (chance < 15) {
            spawnItem(serverLevel, blockPos, new ItemStack(Items.RED_MUSHROOM));
        } else if (chance < 30) {
            spawnItem(serverLevel, blockPos, new ItemStack(Items.HANGING_ROOTS));
        }
    }

    private void spawnItem(ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack) {
        Block.popResource(serverLevel, blockPos, itemStack);
    }

}
