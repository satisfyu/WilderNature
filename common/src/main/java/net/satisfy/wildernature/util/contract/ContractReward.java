package net.satisfy.wildernature.util.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record ContractReward(int blockExpReward, int playerExpReward, Optional<ResourceLocation> playerRewardLoot) {
    public static final Codec<ContractReward> CODEC = RecordCodecBuilder.create(contractRewardInstance -> contractRewardInstance.group(
            Codec.INT.fieldOf("blockExpReward").forGetter(ContractReward::blockExpReward),
            Codec.INT.fieldOf("playerExpReward").forGetter(ContractReward::playerExpReward),
            ResourceLocation.CODEC.optionalFieldOf("loot").forGetter(ContractReward::playerRewardLoot)
    ).apply(contractRewardInstance, ContractReward::new));
}
