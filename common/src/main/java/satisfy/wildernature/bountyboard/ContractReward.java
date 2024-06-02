package satisfy.wildernature.bountyboard;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaEntityLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

public record ContractReward(int blockExpReward, int playerExpReward, ResourceLocation playerRewardLoot){
    public static Codec<ContractReward> CODEC = RecordCodecBuilder.create(contractRewardInstance -> contractRewardInstance.group(
            Codec.INT.fieldOf("blockExpReward").forGetter(ContractReward::blockExpReward),
            Codec.INT.fieldOf("playerExpReward").forGetter(ContractReward::playerExpReward),
            ResourceLocation.CODEC.fieldOf("loot").forGetter(ContractReward::playerRewardLoot)
    ).apply(contractRewardInstance,ContractReward::new));
}
