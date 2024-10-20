package net.satisfy.wildernature.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.satisfy.wildernature.util.contract.Contract;
import net.satisfy.wildernature.util.contract.ContractInProgress;

public class ContractButton extends Button {

    private Contract contract;
    public ContractInProgress progress;

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        setContractSelected(contract, false);
    }

    public void setContractSelected(Contract contract, boolean selected) {
        setContract(contract, null, selected);
    }

    public void setContractProgress(Contract contract, ContractInProgress progress) {
        setContract(contract, progress, false);
    }

    private void setContract(Contract contract, ContractInProgress progress, boolean selected) {
        this.contract = contract;
        this.progress = progress;
        if (contract == null) {
            setTooltip(null);
            return;
        }
        MutableComponent text = Component.empty();

        if (selected) {
            text.append(Component.translatable("text.gui.wildernature.bounty.selectedcontract")).append("\n");
        }
        if (progress != null) {
            if (progress.count == 0) {
                text.append(Component.translatable("text.gui.wildernature.bounty.readytofinishcontract")).append("\n");
            } else {
                text.append(Component.translatable("text.gui.wildernature.bounty.currentcontract")).append("\n");
            }
        }
        text
                .append(Component.translatable(contract.name()))
                .append("\n")
                .append(Component.translatable(contract.description()));

        if (progress != null) {
            text.append("\n");
            text.append(Component.translatable("text.gui.wildernature.bounty.progress", contract.count() - progress.count, contract.count()));
        }
        setTooltip(Tooltip.create(text));
    }

    public ContractButton(int x, int y, Contract contract, OnPress onPress) {
        super(x, y, 18, 18, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        setContract(contract);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        if (contract == null)
            return;
        guiGraphics.renderItem(contract.contractStack(), getX() + 1, getY() + 1);
        if (this.isHoveredOrFocused()) {
            guiGraphics.fill(getX() + 1, getY() + 1, getX() + 17, getY() + 17, 0x7fFFFFFF);
        }
    }

}
