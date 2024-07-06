package net.satisfy.wildernature.fabric.datagen;

import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.util.contract.Contract;
import net.satisfy.wildernature.util.contract.ContractReward;
import net.satisfy.wildernature.util.WilderNatureIdentifier;
import java.util.Random;
import java.util.function.BiConsumer;

public class WilderNatureDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(ContractGenerator::new);
    }

    static Random r = new Random();
    @Override
    public @Nullable String getEffectiveModId() {
        return WilderNature.MOD_ID;
    }
    private static class ContractGenerator extends FabricCodecDataProvider<Contract> {

        private final FabricDataOutput dataOutput;

        protected ContractGenerator(FabricDataOutput dataOutput) {
            super(dataOutput, PackOutput.Target.DATA_PACK, "contracts", Contract.CODEC);
            this.dataOutput = dataOutput;
        }

        @Override
        protected void configure(BiConsumer<ResourceLocation, Contract> provider) {
            new EntityPredicate.Builder().entityType(new EntityTypePredicate() {
                @Override
                public boolean matches(EntityType<?> entityType) {
                    return entityType == EntityType.PIG;
                }

                @Override
                public JsonElement serializeToJson() {
                    return null;
                }
            });
            var random = new Random(12345);
            for(var type : BuiltInRegistries.ENTITY_TYPE){
                var tier = random.nextInt(6);
                var texture = "";
                switch (tier){
                    case 0:
                    case 1:
                        texture = "common";
                        break;
                    case 2:
                    case 3:
                        texture = "uncommon";
                        break;
                    case 5:
                    case 6:
                        texture = "rare";
                        break;
                }
                var name = type.getDescriptionId();
                name = name.split("\\.")[name.split("\\.").length-1];
                provider.accept(BuiltInRegistries.ENTITY_TYPE.getKey(type).withPrefix("contract_for_"), new Contract(
                        new WilderNatureIdentifier("tier%d".formatted(tier)),
                        new ItemStack(BuiltInRegistries.ITEM.get(new WilderNatureIdentifier("%s_contract".formatted(texture)))),
                        BuiltInRegistries.ENTITY_TYPE.getKey(type),
                        new WilderNatureIdentifier("damage_any"),
                        6-tier,
                        "%s killer".formatted(name),

                        "kill %d entities of type %s".formatted(6-tier,name),
                        new ContractReward(
                                tier*5,
                                tier*40,
                                type.getDefaultLootTable()

                        )
                ));
            }
        }
        public Contract createTestContract(BiConsumer<ResourceLocation, Contract> provider){

                return null;
        }
        public ResourceLocation createPredicateForEntity(BiConsumer<ResourceLocation, Contract> provider, EntityType randomEntity){
            //var generator = new PredicateGenerator(this.dataOutput);
            //provider.accept(id,predicate);
            return null;
        }

        @Override
        public String getName() {
            return "Contracts";
        }
    }
}
