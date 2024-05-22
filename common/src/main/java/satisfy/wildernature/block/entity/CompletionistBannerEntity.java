package satisfy.wildernature.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import satisfy.wildernature.registry.EntityRegistry;

public class CompletionistBannerEntity extends BlockEntity {
    public CompletionistBannerEntity(BlockPos blockPos, BlockState state) {
        super(EntityRegistry.COMPLETIONIST_BANNER_ENTITY.get(), blockPos, state);
    }
}