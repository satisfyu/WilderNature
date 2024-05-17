package satisfy.wildernature.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;

public class WolfFurChestplateItem extends Item implements Equipable {

    protected final ArmorItem.Type type;

    public WolfFurChestplateItem(ArmorMaterial pMaterial, ArmorItem.Type pType, Properties pProperties) {
        super(pProperties.defaultDurability(pMaterial.getDurabilityForType(pType)));
        this.type = pType;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }
}
