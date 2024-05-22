package satisfy.wildernature.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import satisfy.wildernature.registry.EntityRegistry;

public class CompletionistBannerEntity extends BlockEntity {
    public CompletionistBannerEntity(BlockPos blockPos, BlockState state) {
        super(EntityRegistry.TAPESTRY.get(), blockPos, state);
    }
}