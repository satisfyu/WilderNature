package net.satisfy.wildernature.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.wildernature.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class HazelnutBushBlock extends SweetBerryBushBlock {

    public HazelnutBushBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter arg, BlockPos arg2, BlockState arg3) {
        return new ItemStack(ObjectRegistry.HAZELNUT.get());
    }

    @Override
    public @NotNull InteractionResult use(BlockState arg, Level arg2, BlockPos arg3, Player arg4, InteractionHand arg5, BlockHitResult arg6) {
        int i = arg.getValue(AGE);
        boolean flag = i == 3;
        if (!flag && arg4.getItemInHand(arg5).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (i > 1) {
            int j = 1 + arg2.random.nextInt(2);
            popResource(arg2, arg3, new ItemStack(ObjectRegistry.HAZELNUT.get(), j + (flag ? 1 : 0)));
            arg2.playSound(null, arg3, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + arg2.random.nextFloat() * 0.4F);
            BlockState blockstate = arg.setValue(AGE, 1);
            arg2.setBlock(arg3, blockstate, 2);
            arg2.gameEvent(GameEvent.BLOCK_CHANGE, arg3, Context.of(arg4, blockstate));
            return InteractionResult.sidedSuccess(arg2.isClientSide);
        } else {
            return super.use(arg, arg2, arg3, arg4, arg5, arg6);
        }
    }

    @Override
    public void entityInside(BlockState arg, Level arg2, BlockPos arg3, Entity arg4) {
    }
}
