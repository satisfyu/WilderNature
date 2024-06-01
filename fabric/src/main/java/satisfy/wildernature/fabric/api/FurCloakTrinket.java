package satisfy.wildernature.fabric.api;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

@SuppressWarnings("unused")
public class FurCloakTrinket extends TrinketItem {
    public FurCloakTrinket(Properties settings) {
        super(settings);
    }

    public static boolean isEquippedBy(Player player) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        return component.map(trinketComponent -> trinketComponent.isEquipped(stack -> stack.getItem() instanceof FurCloakTrinket)).orElse(false);
    }
}
