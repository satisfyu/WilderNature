package satisfy.wildernature.bountyboard;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import satisfy.wildernature.WilderNature;

public class ContractRegistry {
    public static ResourceKey<Registry<Contract[]>> CONTRACT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(WilderNature.MOD_ID,"contract"));
    public static DeferredRegister<Contract[]> CONTRACTS = DeferredRegister.create(WilderNature.MOD_ID, CONTRACT_KEY);

    public static ResourceKey<Contract[]> TIER1 = ResourceKey.create(CONTRACT_KEY, new ResourceLocation("tier1"));
    public static ResourceKey<Contract[]> TIER2 = ResourceKey.create(CONTRACT_KEY, new ResourceLocation("tier2"));
    public static ResourceKey<Contract[]> TIER3 = ResourceKey.create(CONTRACT_KEY, new ResourceLocation("tier3"));
    public static ResourceKey<Contract[]> TIER4 = ResourceKey.create(CONTRACT_KEY, new ResourceLocation("tier4"));
    static {
    }
}
