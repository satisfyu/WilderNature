package satisfy.wildernature.bountyboard;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import satisfy.wildernature.registry.EntityRegistry;

public class BountyBoardBlockEntity extends BlockEntity implements MenuProvider {


    public BountyBoardBlockEntity(BlockPos a, BlockState b) {
        super(EntityRegistry.BOUNTY_BLOCK.get(), a, b);

    }


    @Override
    public Component getDisplayName() {
        return Component.literal("BountyBoard_");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BountyBlockScreenHandler(i,inventory,player, this);
    }
}
