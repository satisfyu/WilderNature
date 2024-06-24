package net.satisfy.wildernature.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.wildernature.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class HazelnutBushBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    private static final VoxelShape SAPLING_SHAPE;
    private static final VoxelShape AGE_1_TOP_SHAPE;
    private static final VoxelShape AGE_2_TOP_SHAPE;
    private static final VoxelShape AGE_3_TOP_SHAPE;

    static {
        AGE = BlockStateProperties.AGE_3;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        SAPLING_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        AGE_1_TOP_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
        AGE_2_TOP_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
        AGE_3_TOP_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }

    public HazelnutBushBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public @NotNull ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(ObjectRegistry.HAZELNUT.get());
    }

    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        int age = blockState.getValue(AGE);
        if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return SAPLING_SHAPE;
        } else {
            return switch (age) {
                case 1 -> AGE_1_TOP_SHAPE;
                case 2 -> AGE_2_TOP_SHAPE;
                case 3 -> AGE_3_TOP_SHAPE;
                default -> Shapes.empty();
            };
        }
    }

    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(AGE) < 3;
    }

    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        int i = blockState.getValue(AGE);
        if (i < 3 && randomSource.nextInt(5) == 0 && serverLevel.getRawBrightness(blockPos.above(), 0) >= 9) {
            BlockState newState = blockState.setValue(AGE, i + 1);
            serverLevel.setBlock(blockPos, newState, 2);
            serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, Context.of(newState));
            if (newState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                serverLevel.setBlock(blockPos.above(), newState.setValue(HALF, DoubleBlockHalf.UPPER), 2);
            }
        }
    }

    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return InteractionResult.PASS;
        }

        int i = blockState.getValue(AGE);
        boolean isFullyGrown = i == 3;
        if (!isFullyGrown && player.getItemInHand(interactionHand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (i > 1) {
            int dropAmount = 1 + level.random.nextInt(2);
            popResource(level, blockPos, new ItemStack(ObjectRegistry.HAZELNUT.get(), dropAmount + (isFullyGrown ? 1 : 0)));
            level.playSound(null, blockPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            BlockState newState = blockState.setValue(AGE, 1);
            level.setBlock(blockPos, newState, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, Context.of(player, newState));
            if (newState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                level.setBlock(blockPos.above(), newState.setValue(HALF, DoubleBlockHalf.UPPER), 2);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE).add(HALF);
    }

    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return blockState.getValue(AGE) < 3 && blockState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return;
        }
        int i = Math.min(3, blockState.getValue(AGE) + 1);
        BlockState newState = blockState.setValue(AGE, i);
        serverLevel.setBlock(blockPos, newState, 2);
        BlockPos upperPos = blockPos.above();
        BlockState upperState = serverLevel.getBlockState(upperPos);
        if (upperState.is(this) && upperState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            serverLevel.setBlock(upperPos, newState.setValue(HALF, DoubleBlockHalf.UPPER), 2);
        } else if (i > 0) {
            serverLevel.setBlock(upperPos, newState.setValue(HALF, DoubleBlockHalf.UPPER), 2);
        }
    }

    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            BlockPos blockpos1 = blockPos.above();
            BlockState blockstate1 = level.getBlockState(blockpos1);
            if (blockstate1.is(this) && blockstate1.getValue(HALF) == DoubleBlockHalf.UPPER) {
                level.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, blockpos1, Block.getId(blockstate1));
            }
        } else {
            BlockPos blockpos = blockPos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            }
        }
        super.playerWillDestroy(level, blockPos, blockState, player);
    }
}
