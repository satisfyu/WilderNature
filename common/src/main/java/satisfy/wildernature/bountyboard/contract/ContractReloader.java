package satisfy.wildernature.bountyboard.contract;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import satisfy.wildernature.WilderNature;
import satisfy.wildernature.bountyboard.BountyBoardTier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ContractReloader implements ResourceManagerReloadListener {
    public static HashMap<ResourceLocation,Contract> contracts = new HashMap<>();
    public static HashMap<ResourceLocation, BountyBoardTier> tiers = new HashMap<>();
    public static List<ResourceLocation> getContractsOfTier(ResourceLocation tierId){
        return contracts.keySet().stream().filter(key -> {
            var allTiers = new ArrayList<>();
            BountyBoardTier tier;
            ResourceLocation id = tierId;
            while (true) {
                allTiers.add(id);
                final var idcopy = id;
                tier = BountyBoardTier.byId(id).orElseThrow(()->{
                    throw new RuntimeException("Error: Not found tier with id %s".formatted(idcopy));
                });
                if (tier.previousTier().isPresent())
                    id = tier.previousTier().get();
                else break;
            }
            return allTiers.contains(contracts.get(key).tier());
        }).toList();
    }

    public static ResourceLocation getRandomContractOfTier(ResourceLocation tier) {
        var tier1 = getContractsOfTier(tier);
        return tier1.get(new Random().nextInt(tier1.size()));
    }

    public void onResourceManagerReload(ResourceManager manager) {
        contracts.clear();
        var contracts = manager.listResources("contracts", path -> path.getPath().endsWith(".json"));
        WilderNature.info("Reloading contracts");
        contracts.forEach((resourceLocation, resource) -> {
            WilderNature.info("Found contract {}",resourceLocation);
            try {
                var open = resource.open();
                var jsonString = new String(open.readAllBytes(), StandardCharsets.UTF_8);
                var contract = Contract.CODEC.parse(JsonOps.INSTANCE,new Gson().fromJson(jsonString, JsonElement.class)).getOrThrow(false,(err)->{throw new RuntimeException(err);});
                this.contracts.put(resourceLocation,contract);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        contracts.clear();
        var tiers = manager.listResources("tiers", path -> path.getPath().endsWith(".json"));
        WilderNature.info("Reloading tiers");
        tiers.forEach((resourceLocation, resource) -> {
            WilderNature.info("Found tier {}",resourceLocation);
            try {
                var namespace = resourceLocation.getNamespace();
                var pathEdits = resourceLocation.getPath();
                pathEdits = pathEdits.substring("tiers/".length());
                pathEdits = pathEdits.substring(0,pathEdits.length()-".json".length());
                var open = resource.open();
                var jsonString = new String(open.readAllBytes(), StandardCharsets.UTF_8);
                var tier = BountyBoardTier.CODEC.parse(JsonOps.INSTANCE,new Gson().fromJson(jsonString, JsonElement.class)).getOrThrow(false,(err)->{throw new RuntimeException(err);});
                this.tiers.put(new ResourceLocation(namespace,pathEdits),tier);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
