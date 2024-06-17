package satisfy.wildernature.bountyboard.contract;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class ContractButton extends Button {
    private Contract contract;

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract){
        setContract(contract,false);
    }
    public void setContract(Contract contract, boolean selected){
        this.contract = contract;
        if(contract==null)
        {
            setTooltip(null);
            return;
        }
        var text = Component.translatable(contract.name()).append("\n")
                .append(Component.translatable(contract.description()));
        if(selected){
            text = Component.translatable("text.gui.wildernature.bounty.selectedcontract").append(text);
        }
        setTooltip(Tooltip.create(text));
    }

    public ContractButton(int x, int y, Contract contract, OnPress onPress) {
        super(x, y, 18,18, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        setContract(contract);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        if(contract==null)
            return;
        guiGraphics.renderItem(contract.contractStack(), getX()+1,getY()+1);
        if(this.isHoveredOrFocused()){
            guiGraphics.fill( getX()+1,getY()+1,getX()+17,getY()+17,0x7fFFFFFF);
        }

    }
}
