package satisfy.wildernature.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import satisfy.wildernature.WilderNature;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> WILDERNATURE_TABS = DeferredRegister.create(WilderNature.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> WILDERNATURE_TAB = WILDERNATURE_TABS.register("wildernature", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(ObjectRegistry.DEER_SPAWN_EGG.get()))
            .title(Component.translatable("creative_tab.wildernature"))
            .displayItems((parameters, out) -> {
                out.accept(ObjectRegistry.BISON_MEAT.get());
                out.accept(ObjectRegistry.COOKED_BISON_MEAT.get());
                out.accept(ObjectRegistry.VENISON.get());
                out.accept(ObjectRegistry.COOKED_VENISON.get());
                out.accept(ObjectRegistry.TURKEY_MEAT.get());
                out.accept(ObjectRegistry.COOKED_TURKEY_MEAT.get());
                out.accept(ObjectRegistry.PELICAN_MEAT.get());
                out.accept(ObjectRegistry.COOKED_PELICAN_MEAT.get());
                out.accept(ObjectRegistry.CHESTNUT.get());

                out.accept(ObjectRegistry.BISON_HORN.get());
                out.accept(ObjectRegistry.FISH_OIL.get());
                out.accept(ObjectRegistry.LOOT_BAG.get());

                out.accept(ObjectRegistry.RED_WOLF_TROPHY.get());
                out.accept(ObjectRegistry.DEER_TROPHY.get());
                out.accept(ObjectRegistry.BISON_TROPHY.get());

                out.accept(ObjectRegistry.BLUNDERBUSS.get());
                out.accept(ObjectRegistry.AMMO_BAG.get());

                out.accept(ObjectRegistry.FUR_CLOAK.get());

                out.accept(ObjectRegistry.WILDERNATURE_STANDARD.get());
                out.accept(ObjectRegistry.DEER_SPAWN_EGG.get());
                out.accept(ObjectRegistry.RED_WOLF_SPAWN_EGG.get());
                out.accept(ObjectRegistry.RACCOON_SPAWN_EGG.get());
                out.accept(ObjectRegistry.SQUIRREL_SPAWN_EGG.get());
                out.accept(ObjectRegistry.PELICAN_SPAWN_EGG.get());
                out.accept(ObjectRegistry.BOAR_SPAWN_EGG.get());
                out.accept(ObjectRegistry.OWL_SPAWN_EGG.get());
                out.accept(ObjectRegistry.BISON_SPAWN_EGG.get());
                out.accept(ObjectRegistry.TURKEY_SPAWN_EGG.get());


            })
            .build());

    public static void init() {
        WILDERNATURE_TABS.register();
    }
}
