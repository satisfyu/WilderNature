package satisfy.wildernature.bountyboard;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import satisfy.wildernature.WilderNature;

public class BountyBlockScreenHandler extends AbstractContainerMenu {
    private static DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(WilderNature.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<BountyBlockScreenHandler>> BOUNTY_BLOCK = MENUS.register("bounty_menu", () -> MenuRegistry.ofExtended((id, inventory, buf) -> {
        return new BountyBlockScreenHandler(id,inventory,buf);
    }));

    public static void registerMenuTypes(){
        MENUS.register();
    }
    protected BountyBlockScreenHandler(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    public BountyBlockScreenHandler(int i, Inventory inventory, Player player, BountyBoardBlockEntity bountyBoardBlockEntity) {
        this(i,inventory);
    }

    public BountyBlockScreenHandler(int i, Inventory inventory) {
        this(BOUNTY_BLOCK.get(),i);
    }

    public BountyBlockScreenHandler(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
