package satisfy.wildernature.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class CompendiumGui extends Screen {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation("wildernature:textures/gui/compendium_page1.png"),
            new ResourceLocation("wildernature:textures/gui/compendium_page2.png"),
            new ResourceLocation("wildernature:textures/gui/compendium_page3.png"),
            new ResourceLocation("wildernature:textures/gui/compendium_page4.png"),
            new ResourceLocation("wildernature:textures/gui/compendium_page5.png")
    };

    private int currentPage;

    protected CompendiumGui() {
        super(Component.translatable("compendium.wildernature.title"));
        this.currentPage = 0;
    }

    @Override
    protected void init() {
        super.init();
        int buttonWidth = 80;
        int buttonHeight = 20;
        int nextButtonX = this.width - buttonWidth - 10;
        int nextButtonY = this.height - buttonHeight - 10;
        int prevButtonX = 10;
        int prevButtonY = this.height - buttonHeight - 10;

        this.addRenderableWidget(new Button.Builder(Component.translatable("compendium.wildernature.button.next_page"), button -> nextPage())
                .bounds(nextButtonX, nextButtonY, buttonWidth, buttonHeight).build());
        this.addRenderableWidget(new Button.Builder(Component.translatable("compendium.wildernature.button.previous_page"), button -> previousPage())
                .bounds(prevButtonX, prevButtonY, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        int textureWidth = 256;
        int textureHeight = 182;
        int x = (this.width - textureWidth) / 2;
        int y = (this.height - textureHeight) / 2;
        guiGraphics.blit(TEXTURES[currentPage], x, y, 0, 0, textureWidth, textureHeight, 256, 256);

        if (isMouseOverArea(mouseX, mouseY, x + 31, y + 15, x + 45, y + 29)) {
            Component tooltipText = Component.translatable("compendium.wildernature.page1_line1");

            guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
        }

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    private boolean isMouseOverArea(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
        return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("compendium.wildernature.title");
    }

    public void nextPage() {
        if (currentPage < TEXTURES.length - 1) {
            currentPage++;
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
        }
    }
}