package satisfy.wildernature.bountyboard.contract;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ContractButton extends Button {
    private Contract contract;

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract){
        this.contract = contract;
        this.setTooltip(contract == null ? null : Tooltip.create(
                Component.translatable(contract.name()).append("\n")
                        .append(Component.translatable(contract.description()))));
    }

    public ContractButton(int x, int y, Contract contract, OnPress onPress) {
        super(x, y, 18,18, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        setContract(contract);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        if(contract==null)
            return;
        //guiGraphics.blit(contract.texture(),getX()+1,getY()+1,0,0,16,16,16,16);
        guiGraphics.renderItem(contract.contractStack(), getX()+1,getY()+1);
        if(this.isHoveredOrFocused()){
            guiGraphics.fill( getX()+1,getY()+1,getX()+17,getY()+17,0x7fFFFFFF);
        }

    }
}
