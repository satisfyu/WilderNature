package net.satisfy.wildernature.client.gui.screens;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.wildernature.client.gui.components.ContractButton;
import net.satisfy.wildernature.client.gui.handlers.BountyBlockScreenHandler;
import net.satisfy.wildernature.network.BountyBlockNetworking;
import net.satisfy.wildernature.util.WilderNatureIdentifier;
import net.satisfy.wildernature.util.contract.Contract;

public class BountyBlockScreen extends AbstractContainerScreen<BountyBlockScreenHandler> {

    private final WilderNatureIdentifier TEX_BACKGROUND = new WilderNatureIdentifier("textures/gui/bounty_board/background.png");
    private final WilderNatureIdentifier TEX_REROLL = new WilderNatureIdentifier("textures/gui/bounty_board/reroll.png");
    private final WilderNatureIdentifier TEX_ACCEPT = new WilderNatureIdentifier("textures/gui/bounty_board/accept.png");
    private final WilderNatureIdentifier TEX_ACCEPTLOCK = new WilderNatureIdentifier("textures/gui/bounty_board/accept_locked.png");
    private final WilderNatureIdentifier TEX_FINISHEDSLOT = new WilderNatureIdentifier("textures/gui/bounty_board/finished_bg.png");
    private final WilderNatureIdentifier TEX_BAR = new WilderNatureIdentifier("textures/gui/bounty_board/bar.png");
    private ImageButton rerollButton;
    private ImageButton acceptButton;
    private Button finishButton;
    private final ContractButton[] contractButtons = new ContractButton[3];
    private ContractButton targetContractButton;

    public BountyBlockScreen(BountyBlockScreenHandler abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        var centerX = width/2;
        var centerY = height/2;
        var guiX = width/2-176/2;
        var guiY = height/2-169/2;
        rerollButton = new ImageButton(centerX-74,centerY-52,14,14,0,0,14, TEX_REROLL,14,42,this::onReroll);
        acceptButton = new ImageButton(centerX-176/2+135,centerY-169/2+51,14,14,0,0,14, TEX_ACCEPT,14,42,this::onAccept);
        new ImageButton(centerX - 176 / 2 + 135, centerY - 169 / 2 + 51, 14, 14, 0, 0, 14, TEX_ACCEPTLOCK, 14, 42, (i) -> {
        });
        finishButton = new ImageButton(centerX-176/2+135,centerY-169/2+51,14,14,0,0,14, TEX_ACCEPT,14,42,(button)->onFinish());
        finishButton.setTooltip(Tooltip.create(Component.translatable("text.gui.wildernature.bounty.finish")));

        addRenderableWidget(rerollButton);
        addRenderableWidget(acceptButton);
        addRenderableWidget(finishButton);
        acceptButton.setTooltip(Tooltip.create(Component.translatable("text.gui.wildernature.bounty.accept")));
        targetContractButton = new ContractButton(centerX-176/2+97,centerY-169/2+49,null,(button)->{});
        for(int i=0;i<3;i++){
            var contract = menu.c_contracts[i];
            contractButtons[i] = addRenderableWidget(new ContractButton(centerX-176/2+25+i*18,centerY-169/2+49,contract,(button)-> this.setSelectedContract(((ContractButton)button).getContract())));
        }
        addRenderableWidget(targetContractButton);
        menu.c_onContractUpdate.subscribe(()->{
            targetContractButton.setContract(null);
            finishButton.visible = menu.c_activeContractProgress !=null && menu.c_activeContractProgress.isFinished();
            for(int i=0;i<3;i++){
                contractButtons[i].setContract(menu.c_contracts[i]);
            }
        });
        addRenderableOnly((guiGraphics,mx,my,f)->{
            if(menu.c_activeContractProgress != null && menu.c_activeContractProgress.isFinished()){
                guiGraphics.blit(TEX_FINISHEDSLOT,centerX-176/2+97-2,centerY-169/2+49-2,0,0,22,22,22,22);
            }
        });
        addRenderableOnly((guiGraphics,mx,my,f)->{
            int xPos = centerX+176/2-4;
            int yPos = centerY-169/2+30-1;
            if(menu.c_activeContractProgress !=null){
                var tooltipBorders = 9;
                var tooltipTextWidth = 120-tooltipBorders;
                if(menu.c_boardId != menu.c_activeContractProgress.boardId) {
                    assert minecraft != null;
                    var tip = minecraft.font.split(Component.translatable("text.gui.wildernature.bounty.finish.warning"), tooltipTextWidth);
                    guiGraphics.renderTooltip(minecraft.font, tip, xPos, yPos);
                }
            }
        });
        addRenderableOnly(((guiGraphics, i, j, f) -> {
            assert minecraft != null;
            guiGraphics.drawCenteredString(minecraft.font,Component.translatable(menu.c_tierId.toLanguageKey()),centerX,guiY-15,0xFFFFFFFF);
            var progress = menu.c_progress;
            guiGraphics.blit(TEX_BAR,guiX+12,guiY+5,0f,0f, (int) (153*progress),12, 153,12);
        }));
    }

    private void onFinish() {
        var buf = new FriendlyByteBuf(new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT,0, BountyBlockNetworking.MAX_SIZE));
        buf.writeEnum(BountyBlockNetworking.BountyClientActionType.FINISH_CONTRACT);
        NetworkManager.sendToServer(BountyBlockNetworking.ID_SCREEN_ACTION,buf);
    }


    private void onAccept(Button button) {
        for(int i=0;i<3;i++){
            if(contractButtons[i].getContract() == targetContractButton.getContract()){
                var buf = new FriendlyByteBuf(new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT,0,BountyBlockNetworking.MAX_SIZE));
                buf.writeEnum(BountyBlockNetworking.BountyClientActionType.CONFIRM_CONTRACT);
                buf.writeByte(i);
                NetworkManager.sendToServer(BountyBlockNetworking.ID_SCREEN_ACTION,buf);
                return;
            }
        }
        throw new RuntimeException("not found contract");
    }

    private void setSelectedContract(Contract contract) {
        targetContractButton.setContractSelected(contract,true);
    }

    private void onReroll(Button button) {
        var buf = new FriendlyByteBuf(new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT,0,BountyBlockNetworking.MAX_SIZE));
        buf.writeEnum(BountyBlockNetworking.BountyClientActionType.REROLL);
        NetworkManager.sendToServer(BountyBlockNetworking.ID_SCREEN_ACTION,buf);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mx, int my, float f) {
        if(menu.c_activeContractProgress != null){
            targetContractButton.setContractProgress(menu.c_activeContract,menu.c_activeContractProgress);
            if(!menu.c_activeContractProgress.isFinished()){
                targetContractButton.setContract(null);
            }
        }
        if(menu.c_activeContractProgress==null && targetContractButton.progress != null){
            targetContractButton.setContract(null);
        }
        finishButton.visible = menu.c_activeContractProgress !=null && menu.c_activeContractProgress.isFinished();
        updateTooltip();
        acceptButton.visible = menu.c_activeContractProgress == null && (targetContractButton != null && targetContractButton.getContract()!=null);
        acceptButton.active = menu.c_activeContractProgress == null;
        super.render(guiGraphics, mx, my, f);
        renderTooltip(guiGraphics, mx, my);

    }

    private void updateTooltip() {
        var comp = Component.translatable("text.gui.wildernature.bounty.reroll.left",menu.c_rerolls);
        if(menu.c_time>0){
            comp = comp.append("\n").append(Component.translatable("text.gui.wildernature.bounty.reroll.time",menu.c_time/20/60,menu.c_time/20%60));
        }
        Tooltip tooltip = Tooltip.create(comp);
        rerollButton.setTooltip(tooltip);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        guiGraphics.fill(0,0,width,height, FastColor.ARGB32.color(64,0,0,0));
        guiGraphics.blit(TEX_BACKGROUND,
                width/2 - (176 / 2),
                height/2 - (169 / 2),
                0,
                0,
                176,
                169);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j) {
    }
}
