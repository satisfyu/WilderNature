package satisfy.wildernature.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FurCloakItem extends Item implements Equipable {

    protected final ArmorItem.Type type;

    public FurCloakItem(ArmorMaterial pMaterial, ArmorItem.Type pType, Properties pProperties) {
        super(pProperties.defaultDurability(pMaterial.getDurabilityForType(pType)));
        this.type = pType;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    public static boolean isEquippedBy(Player player) {
        for (ItemStack itemStack : player.getArmorSlots()) {
            if (itemStack.getItem() instanceof FurCloakItem) {
                return true;
            }
        }
        return false;
    }
}
