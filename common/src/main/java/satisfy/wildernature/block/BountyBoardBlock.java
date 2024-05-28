package satisfy.wildernature.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BountyBoardBlock extends Block {
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

    private static final VoxelShape SHAPE_BOTTOM_LEFT = makeBottomLeftShape();
    private static final VoxelShape SHAPE_BOTTOM_RIGHT = makeBottomRightShape();
    private static final VoxelShape SHAPE_TOP_LEFT = makeTopLeftShape();
    private static final VoxelShape SHAPE_TOP_RIGHT = makeTopRightShape();

    public BountyBoardBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, Part.BOTTOM_LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();

        if (!canPlaceAt(world, pos)) {
            return null;
        }

        world.setBlock(pos, this.defaultBlockState().setValue(PART, Part.BOTTOM_LEFT), 3);
        world.setBlock(pos.above(), this.defaultBlockState().setValue(PART, Part.TOP_LEFT), 3);
        world.setBlock(pos.east(), this.defaultBlockState().setValue(PART, Part.BOTTOM_RIGHT), 3);
        world.setBlock(pos.east().above(), this.defaultBlockState().setValue(PART, Part.TOP_RIGHT), 3);

        return this.defaultBlockState().setValue(PART, Part.BOTTOM_LEFT);
    }

    private boolean canPlaceAt(Level world, BlockPos pos) {
        return world.getBlockState(pos).canBeReplaced() &&
                world.getBlockState(pos.above()).canBeReplaced() &&
                world.getBlockState(pos.east()).canBeReplaced() &&
                world.getBlockState(pos.east().above()).canBeReplaced();
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            Part part = state.getValue(PART);
            BlockPos basePos = getBasePos(pos, part);
            destroyAdjacentBlocks(world, basePos);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    private BlockPos getBasePos(BlockPos pos, Part part) {
        switch (part) {
            case TOP_LEFT:
                return pos.below();
            case BOTTOM_RIGHT:
                return pos.west();
            case TOP_RIGHT:
                return pos.below().west();
            default:
                return pos;
        }
    }

    private void destroyAdjacentBlocks(Level world, BlockPos basePos) {
        world.removeBlock(basePos, false);
        world.removeBlock(basePos.above(), false);
        world.removeBlock(basePos.east(), false);
        world.removeBlock(basePos.east().above(), false);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Part part = state.getValue(PART);
        switch (part) {
            case BOTTOM_LEFT:
                return SHAPE_BOTTOM_LEFT;
            case BOTTOM_RIGHT:
                return SHAPE_BOTTOM_RIGHT;
            case TOP_LEFT:
                return SHAPE_TOP_LEFT;
            case TOP_RIGHT:
                return SHAPE_TOP_RIGHT;
            default:
                return Shapes.empty();
        }
    }

    private static VoxelShape makeBottomLeftShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.4375, 0.125, 1, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.625, 0.5, 1, 1, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.25, 0.5, 0.375, 0.5, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.5, 0.4375, 1, 0.625, 0.5625), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeBottomRightShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.4375, 1, 1, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.625, 0.5, 0.875, 1, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.625, 0.25, 0.5, 0.875, 0.5, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.5, 0.4375, 0.875, 0.625, 0.5625), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeTopLeftShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.4375, 1, 0.875, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.5, 0.875, 0.75, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.75, 0.4375, 0.875, 0.875, 0.5625), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeTopRightShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.4375, 0.125, 0.875, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.5, 1, 0.75, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.75, 0.4375, 1, 0.875, 0.5625), BooleanOp.OR);
        return shape;
    }

    public enum Part implements StringRepresentable {
        BOTTOM_LEFT("bottom_left"),
        BOTTOM_RIGHT("bottom_right"),
        TOP_LEFT("top_left"),
        TOP_RIGHT("top_right");

        private final String name;

        Part(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
