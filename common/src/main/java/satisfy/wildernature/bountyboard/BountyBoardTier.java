package satisfy.wildernature.bountyboard;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import satisfy.wildernature.bountyboard.contract.ContractReloader;

import java.util.Optional;

public record BountyBoardTier(int experience, Optional<ResourceLocation> nextTier, Optional<ResourceLocation> previousTier) {
    public static Codec<BountyBoardTier> CODEC = RecordCodecBuilder.create(bountyBoardTierInstance -> bountyBoardTierInstance.group(
            Codec.INT.fieldOf("experience").forGetter(BountyBoardTier::experience),
            ResourceLocation.CODEC.optionalFieldOf("nextTier").forGetter(BountyBoardTier::nextTier),
            ResourceLocation.CODEC.optionalFieldOf("previousTier").forGetter(BountyBoardTier::previousTier)
    ).apply(bountyBoardTierInstance,BountyBoardTier::new));

    public float progress(int xp){
        if(this.experience==-1)
            return 1;
        else
            return (float)xp/((float)experience-1);
    }
    public static Optional<BountyBoardTier> byId(ResourceLocation id){
        return Optional.ofNullable(ContractReloader.tiers.get(id));
    }
}
