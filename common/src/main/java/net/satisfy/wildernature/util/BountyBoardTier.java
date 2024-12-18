package net.satisfy.wildernature.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.wildernature.util.contract.ContractReloader;

import java.util.Optional;

public record BountyBoardTier(int experience, Optional<ResourceLocation> nextTier,
                              Optional<ResourceLocation> previousTier) {
    public static final Codec<BountyBoardTier> CODEC = RecordCodecBuilder.create(bountyBoardTierInstance -> bountyBoardTierInstance.group(
            Codec.INT.fieldOf("experience").forGetter(BountyBoardTier::experience),
            ResourceLocation.CODEC.optionalFieldOf("nextTier").forGetter(BountyBoardTier::nextTier),
            ResourceLocation.CODEC.optionalFieldOf("previousTier").forGetter(BountyBoardTier::previousTier)
    ).apply(bountyBoardTierInstance, BountyBoardTier::new));

    public float progress(int xp) {
        return this.experience == -1 ? 1.0f : (float) xp / (experience - 1);
    }

    public static Optional<BountyBoardTier> byId(ResourceLocation id) {
        return Optional.ofNullable(ContractReloader.tiers.get(id));
    }
}
