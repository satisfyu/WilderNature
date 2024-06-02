package satisfy.wildernature.bountyboard;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import satisfy.wildernature.WilderNature;
import satisfy.wildernature.util.WilderNatureIdentifier;

public class BountyBlockScreen extends AbstractContainerScreen<BountyBlockScreenHandler> {

    public BountyBlockScreen(BountyBlockScreenHandler abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new Button.Builder(Component.literal("test button"),(b)->{}).pos(10,10).width(120).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        guiGraphics.fill(0,0,width,height, FastColor.ARGB32.color(64,0,0,0));
        guiGraphics.blit(new WilderNatureIdentifier("textures/gui/bounty_board.png"), width/2 - (176 / 2), height/2 - (169 / 2),0,0,176,169);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j) {
    }
}
