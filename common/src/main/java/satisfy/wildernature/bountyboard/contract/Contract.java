package satisfy.wildernature.bountyboard.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import satisfy.wildernature.util.WilderNatureIdentifier;

import java.util.Random;

public record Contract(ResourceLocation tier, ResourceLocation texture, ResourceLocation targetPredicate, ResourceLocation damagePredicate, int count, String name, String description, ContractReward reward) {
    public static Codec<Contract> CODEC = RecordCodecBuilder.create(contractInstance -> contractInstance.group(
            ResourceLocation.CODEC.fieldOf("type").forGetter(Contract::tier),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(Contract::texture),
            ResourceLocation.CODEC.fieldOf("targetPredicate").forGetter(Contract::targetPredicate),
            ResourceLocation.CODEC.fieldOf("damagePredicate").forGetter(Contract::damagePredicate),
            Codec.INT.fieldOf("count").forGetter(Contract::count),
            Codec.STRING.fieldOf("name").forGetter(Contract::name),
            Codec.STRING.fieldOf("description").forGetter(Contract::description),
            ContractReward.CODEC.fieldOf("reward").forGetter(Contract::reward)
    ).apply(contractInstance,Contract::new));
    public static Contract createTestRandom(){
        return
                new Contract(
                        new WilderNatureIdentifier("tier1"),
                        new WilderNatureIdentifier("textures/item/common_contract.png"),
                        randomTestResource("target"),
                        randomTestResource("damage"),
                        2,
                        "test-target-text",
                        "test-damage-text",
                        new ContractReward(
                                0,
                                0,
                                randomTestResource("reward")));
    }
    public static ResourceLocation randomTestResource(String prefix) {
        String[] colors = {
                "red", "green", "blue", "yellow", "purple", "orange", "lime", "pink",
                "brown", "cyan", "magenta", "violet", "gold", "silver", "black", "white"
        };
        String[] shapes = {
                "circle", "square", "triangle", "rectangle", "hexagon", "pentagon",
                "octagon", "star", "oval", "heart", "diamond", "trapezoid", "parallelogram"
        };

        Random random = new Random();
        String randomColor = colors[random.nextInt(colors.length)];
        String randomShape = shapes[random.nextInt(shapes.length)];

        return new WilderNatureIdentifier(prefix + "-" + randomColor + "-" + randomShape);
    }

    public static Contract fromId(ResourceLocation contract) {
        return ContractReloader.contracts.get(contract);
    }
}
