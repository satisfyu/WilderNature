package net.satisfy.wildernature.bountyboard;

import net.minecraft.world.item.BlockItem;
import net.satisfy.wildernature.registry.ObjectRegistry;

public class BountyBoardBlockItem extends BlockItem {
    public BountyBoardBlockItem(Properties properties) {
        super(ObjectRegistry.BOUNTY_BOARD.get(), properties);
    }
}
