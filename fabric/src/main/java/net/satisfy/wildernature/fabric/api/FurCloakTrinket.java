package net.satisfy.wildernature.fabric.api;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.satisfy.wildernature.registry.ObjectRegistry;

import java.util.Optional;

@SuppressWarnings("unused")
public class FurCloakTrinket extends TrinketItem {
    public FurCloakTrinket(Properties settings) {
        super(settings);
    }

//    public static boolean isEquippedBy(Player player) {
//        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
//        return component.map(trinketComponent -> trinketComponent.isEquipped(stack -> stack.getItem() instanceof FurCloakTrinket)).orElse(false);
//    }

    public static boolean isEquippedBy(Player player) {

        if (TrinketsApi.getTrinketComponent(player).isPresent()) {
            Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
            assert component.isPresent();
            return component.get().isEquipped(ObjectRegistry.FUR_CLOAK.get());
        }

        return false;
    }
}
