package net.satisfy.wildernature.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public abstract class CompendiumGui extends Screen {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation("wildernature:textures/gui/compendium/page1.png"),
            new ResourceLocation("wildernature:textures/gui/compendium/page2.png"),
            new ResourceLocation("wildernature:textures/gui/compendium/page3.png"),
            new ResourceLocation("wildernature:textures/gui/compendium/page4.png"),
            new ResourceLocation("wildernature:textures/gui/compendium/page5.png"),
            new ResourceLocation("wildernature:textures/gui/compendium/page6.png")

    };

    private int currentPage;

    protected CompendiumGui() {
        super(Component.translatable("compendium.wildernature.title"));
        this.currentPage = 0;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        int textureWidth = 316;
        int textureHeight = 190;
        int x = (this.width - textureWidth) / 2;
        int y = (this.height - textureHeight) / 2;
        guiGraphics.blit(TEXTURES[currentPage], x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        if (currentPage == 0) {
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 101, x + 131, y + 113)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page1_left_health");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 27, x + 51, y + 80)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page1_left_alignment");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 54, y + 139, x + 111, y + 156)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page1_left_spawns");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 184, y + 103, x + 281, y + 113)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page1_right_health");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 184, y + 27, x + 201, y + 89)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page1_right_alignment");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 214, y + 139, x + 251, y + 156)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page1_right_spawns");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }

            Component fixedTextSquirrel = Component.translatable("compendium.wildernature.page1_squirrel");
            int fixedTextSquirrelX = x + 62;
            int fixedTextSquirrelY = y + 126;
            guiGraphics.drawString(this.font, fixedTextSquirrel, fixedTextSquirrelX, fixedTextSquirrelY, 0xFFFFFF);

            Component fixedTextRaccoon = Component.translatable("compendium.wildernature.page1_raccoon");
            int fixedTextRaccoonX = x + 212;
            int fixedTextRaccoonY = y + 126;
            guiGraphics.drawString(this.font, fixedTextRaccoon, fixedTextRaccoonX, fixedTextRaccoonY, 0xFFFFFF);
        }

        if (currentPage == 1) {
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 101, x + 131, y + 113)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page2_left_health");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 27, x + 51, y + 80)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page2_left_alignment");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 74, y + 139, x + 91, y + 156)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page2_left_spawns");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 184, y + 103, x + 281, y + 113)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page2_right_health");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 184, y + 27, x + 201, y + 89)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page2_right_alignment");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 224, y + 139, x + 241, y + 156)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page2_right_spawns");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }

            Component fixedTextRedWolf = Component.translatable("compendium.wildernature.page2_red_wolf");
            int fixedTextRedWolfX = x + 62;
            int fixedTextRedWolfY = y + 126;
            guiGraphics.drawString(this.font, fixedTextRedWolf, fixedTextRedWolfX, fixedTextRedWolfY, 0xFFFFFF);

            Component fixedTextMinisheep = Component.translatable("compendium.wildernature.page2_minisheep");
            int fixedTextMinisheepX = x + 212;
            int fixedTextMinisheepY = y + 126;
            guiGraphics.drawString(this.font, fixedTextMinisheep, fixedTextMinisheepX, fixedTextMinisheepY, 0xFFFFFF);
        }

        if (currentPage == 2) {
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 101, x + 131, y + 113)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page3_left_health");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 27, x + 51, y + 80)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page3_left_alignment");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 64, y + 139, x + 101, y + 156)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page3_left_spawns");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 184, y + 103, x + 281, y + 113)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page3_right_health");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 184, y + 27, x + 201, y + 89)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page3_right_alignment");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 224, y + 139, x + 241, y + 156)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page3_right_spawns");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }

            Component fixedTextBoar = Component.translatable("compendium.wildernature.page3_boar");
            int fixedTextBoarX = x + 72;
            int fixedTextBoarY = y + 126;
            guiGraphics.drawString(this.font, fixedTextBoar, fixedTextBoarX, fixedTextBoarY, 0xFFFFFF);

            Component fixedTextOwl = Component.translatable("compendium.wildernature.page3_owl");
            int fixedTextOwlX = x + 225;
            int fixedTextOwlY = y + 126;
            guiGraphics.drawString(this.font, fixedTextOwl, fixedTextOwlX, fixedTextOwlY, 0xFFFFFF);
        }

        if (currentPage == 3) {
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 101, x + 131, y + 113)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page4_left_health");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 27, x + 51, y + 80)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page4_left_alignment");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 74, y + 139, x + 91, y + 156)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page4_left_spawns");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 184, y + 103, x + 281, y + 113)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page4_right_health");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 184, y + 27, x + 201, y + 89)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page4_right_alignment");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }


            Component fixedTextBoar = Component.translatable("compendium.wildernature.page4_deer");
            int fixedTextDeerX = x + 72;
            int fixedTextDeerY = y + 126;
            guiGraphics.drawString(this.font, fixedTextBoar, fixedTextDeerX, fixedTextDeerY, 0xFFFFFF);

            Component fixedTextOwl = Component.translatable("compendium.wildernature.page4_dog");
            int fixedTextDogX = x + 225;
            int fixedTextDogY = y + 126;
            guiGraphics.drawString(this.font, fixedTextOwl, fixedTextDogX, fixedTextDogY, 0xFFFFFF);
        }


            if (currentPage == 4) {
                if (isMouseOverArea(mouseX, mouseY, x + 34, y + 101, x + 131, y + 113)) {
                    Component tooltipText = Component.translatable("compendium.wildernature.page5_left_health");
                    guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
                }
                if (isMouseOverArea(mouseX, mouseY, x + 34, y + 27, x + 51, y + 80)) {
                    Component tooltipText = Component.translatable("compendium.wildernature.page5_left_alignment");
                    guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
                }
                if (isMouseOverArea(mouseX, mouseY, x + 54, y + 139, x + 111, y + 156)) {
                    Component tooltipText = Component.translatable("compendium.wildernature.page5_left_spawns");
                    guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
                }
                if (isMouseOverArea(mouseX, mouseY, x + 184, y + 103, x + 281, y + 113)) {
                    Component tooltipText = Component.translatable("compendium.wildernature.page5_right_health");
                    guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
                }
                if (isMouseOverArea(mouseX, mouseY, x + 184, y + 27, x + 201, y + 89)) {
                    Component tooltipText = Component.translatable("compendium.wildernature.page5_right_alignment");
                    guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
                }
                if (isMouseOverArea(mouseX, mouseY, x + 224, y + 139, x + 241, y + 156)) {
                    Component tooltipText = Component.translatable("compendium.wildernature.page5_right_spawns");
                    guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
                }

                Component fixedTextTurkey = Component.translatable("compendium.wildernature.page5_turkey");
                int fixedTextTurkeyX = x + 65;
                int fixedTextTurkeyY = y + 126;
                guiGraphics.drawString(this.font, fixedTextTurkey, fixedTextTurkeyX, fixedTextTurkeyY, 0xFFFFFF);

                Component fixedTextBison = Component.translatable("compendium.wildernature.page5_bison");
                int fixedTextBisonX = x + 220;
                int fixedTextBisonY = y + 126;
                guiGraphics.drawString(this.font, fixedTextBison, fixedTextBisonX, fixedTextBisonY, 0xFFFFFF);
            }

        if (currentPage == 5) {
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 101, x + 131, y + 113)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page6_left_health");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 34, y + 27, x + 51, y + 80)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page6_left_alignment");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }
            if (isMouseOverArea(mouseX, mouseY, x + 64, y + 139, x + 101, y + 156)) {
                Component tooltipText = Component.translatable("compendium.wildernature.page6_left_spawns");
                guiGraphics.renderTooltip(this.font, tooltipText, mouseX, mouseY);
            }

            Component fixedTextPelican = Component.translatable("compendium.wildernature.page6_pelican");
            int fixedTextPelicanX = x + 65;
            int fixedTextPelicanY = y + 126;
            guiGraphics.drawString(this.font, fixedTextPelican, fixedTextPelicanX, fixedTextPelicanY, 0xFFFFFF);

        }




        if (isMouseOverArea(mouseX, mouseY, 356, 43, 371, 227)) {
            guiGraphics.fill(356, 43, 371, 227, 0x80000000);
        }

        if (currentPage > 0 && isMouseOverArea(mouseX, mouseY, 61, 43, 76, 227)) {
            guiGraphics.fill(61, 43, 76, 227, 0x80000000);
        }

        super.render(guiGraphics, mouseX, mouseY, delta);
    }



    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverArea((int)mouseX, (int)mouseY, 356, 43, 371, 227)) {
            nextPage();
            return true;
        }
        if (currentPage > 0 && isMouseOverArea((int)mouseX, (int)mouseY, 61, 43, 76, 227)) {
            previousPage();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
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
