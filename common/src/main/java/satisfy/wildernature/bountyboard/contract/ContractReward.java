package satisfy.wildernature.bountyboard.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record ContractReward(int blockExpReward, int playerExpReward, ResourceLocation playerRewardLoot){
    public static Codec<ContractReward> CODEC = RecordCodecBuilder.create(contractRewardInstance -> contractRewardInstance.group(
            Codec.INT.fieldOf("blockExpReward").forGetter(ContractReward::blockExpReward),
            Codec.INT.fieldOf("playerExpReward").forGetter(ContractReward::playerExpReward),
            ResourceLocation.CODEC.fieldOf("loot").forGetter(ContractReward::playerRewardLoot)
    ).apply(contractRewardInstance,ContractReward::new));
}
