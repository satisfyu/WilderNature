package net.satisfy.wildernature.util.contract;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.util.BountyBoardTier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ContractReloader implements ResourceManagerReloadListener {
    private static HashMap<ResourceLocation,Contract> contracts = new HashMap<>();
    public static Contract getContract(ResourceLocation location) {
        var path = location.getPath();

        int index = path.indexOf("/", 0);

        var newLocation = path.substring(index+1);
        var rl = new ResourceLocation(location.getNamespace(),newLocation);
        var contract = contracts.get(rl);

        return contract;
    }
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

    public static Map<ResourceLocation,Contract> getAllContracts() {
        return new HashMap<>(contracts);
    }

    public void onResourceManagerReload(ResourceManager manager) {
        contracts.clear();
        var contracts = manager.listResources("wildernature_contracts", path -> path.getPath().endsWith(".json"));
        contracts.forEach((resourceLocation, resource) -> {
            try {
                var open = resource.open();
                int index = resourceLocation.getPath().indexOf("/", 0);
                var pathEdits = resourceLocation.getPath().substring(index + 1);

                var jsonString = new String(open.readAllBytes(), StandardCharsets.UTF_8);
                var contract = Contract.CODEC.parse(JsonOps.INSTANCE, new Gson().fromJson(jsonString, JsonElement.class)).getOrThrow(false, (err) -> {
                    throw new RuntimeException(err);
                });

                var rl = new ResourceLocation(resourceLocation.getNamespace(), pathEdits);
                WilderNature.info("registering contract {}", rl);
                this.contracts.put(rl, contract);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        contracts.clear();
        var tiers = manager.listResources("wildernature_tiers", path -> path.getPath().endsWith(".json"));
        tiers.forEach((resourceLocation, resource) -> {
            WilderNature.info("Found tier {}",resourceLocation);
            try {
                var namespace = resourceLocation.getNamespace();
                var pathEdits = resourceLocation.getPath();
                pathEdits = pathEdits.substring("wildernature_tiers/".length());
                pathEdits = pathEdits.substring(0,pathEdits.length()-".json".length());
                var open = resource.open();
                var jsonString = new String(open.readAllBytes(), StandardCharsets.UTF_8);
                var tier = BountyBoardTier.CODEC.parse(JsonOps.INSTANCE,new Gson().fromJson(jsonString, JsonElement.class)).getOrThrow(false,(err)->{throw new RuntimeException(err);});
                var rl = new ResourceLocation(namespace,pathEdits);
                this.tiers.put(rl,tier);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
