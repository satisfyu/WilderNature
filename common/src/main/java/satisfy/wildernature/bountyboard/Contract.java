package satisfy.wildernature.bountyboard;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.resources.ResourceLocation;

public record Contract(ResourceLocation targetPredicate, ResourceLocation damagePredicate, ContractReward reward) {
    public static Codec<Contract> CODEC = RecordCodecBuilder.create(contractInstance -> contractInstance.group(
            ResourceLocation.CODEC.fieldOf("targetPredicate").forGetter(Contract::targetPredicate),
            ResourceLocation.CODEC.fieldOf("damagePredicate").forGetter(Contract::damagePredicate),
            ContractReward.CODEC.fieldOf("reward").forGetter(Contract::reward)
    ).apply(contractInstance,Contract::new));
}
