package net.satisfy.wildernature.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.util.contract.ContractReloader;
import net.satisfy.wildernature.forge.registry.WilderNatureBiomeModifiers;

@Mod(WilderNature.MOD_ID)
public class WilderNatureForge {
    public WilderNatureForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(WilderNature.MOD_ID, modEventBus);
        WilderNature.init();
        WilderNatureBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::resourceLoaderEvent);
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        WilderNature.commonInit();
    }
    private void resourceLoaderEvent(AddReloadListenerEvent event){
        event.addListener(new ContractReloader());
    }
}