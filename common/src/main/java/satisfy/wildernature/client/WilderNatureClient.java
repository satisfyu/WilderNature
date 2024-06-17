package satisfy.wildernature.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import satisfy.wildernature.bountyboard.BountyBlockScreen;
import satisfy.wildernature.bountyboard.BountyBlockScreenHandler;
import satisfy.wildernature.bountyboard.BountyEntrypoints;
import satisfy.wildernature.client.model.*;
import satisfy.wildernature.client.render.block.CompletionistBannerRenderer;
import satisfy.wildernature.client.render.entity.*;
import satisfy.wildernature.registry.ObjectRegistry;
import satisfy.wildernature.util.WilderNatureUtil;

import static satisfy.wildernature.registry.EntityRegistry.*;
import static satisfy.wildernature.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class WilderNatureClient {
    public static final ModelLayerLocation WOLF_FUR_CHESTPLATE_LAYER = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "wolf_fur_chestplate");

    public static void preInitClient() {
        RenderTypeRegistry.register(RenderType.cutout(), DEER_TROPHY.get(), HAZELNUT_BUSH.get(), BOUNTY_BOARD.get());
        registerEntityRenderers();
        registerEntityModelLayer();
        registerScreens();
        WilderNatureUtil.init();
        BountyEntrypoints.clientEntry();
        makeHorn(ObjectRegistry.BISON_HORN.get());

    }

    //taken from WilderNatureUtil
    private static void makeHorn(Item item) {
        ItemProperties.register(item, new ResourceLocation("blowing"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null) {
                return 0.0F;
            } else {
                return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float)(p_174635_.getUseDuration() -
                        p_174637_.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemProperties.register(item, new ResourceLocation("using"), (p_174630_, p_174631_, p_174632_, p_174633_) -> p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F);
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(BULLET, ThrownItemRenderer::new);
        EntityRendererRegistry.register(RED_WOLF, RedWolfRenderer::new);
        EntityRendererRegistry.register(PELICAN, PelicanRenderer::new);
        EntityRendererRegistry.register(RACCOON, RaccoonRenderer::new);
        EntityRendererRegistry.register(SQUIRREL, SquirrelRenderer::new);
        EntityRendererRegistry.register(DEER, DeerRenderer::new);
        EntityRendererRegistry.register(OWL, OwlRenderer::new);
        EntityRendererRegistry.register(BOAR, BoarRenderer::new);
        EntityRendererRegistry.register(BISON, BisonRenderer::new);
        EntityRendererRegistry.register(TURKEY, TurkeyRenderer::new);
        EntityRendererRegistry.register(MINISHEEP, MiniSheepRenderer::new);
        EntityRendererRegistry.register(DOG, DogRenderer::new);
        BlockEntityRendererRegistry.register(COMPLETIONIST_BANNER_ENTITY.get(), CompletionistBannerRenderer::new);
    }

    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(RedWolfModel.LAYER_LOCATION, RedWolfModel::getTexturedModelData);
        EntityModelLayerRegistry.register(PelicanModel.LAYER_LOCATION, PelicanModel::getTexturedModelData);
        EntityModelLayerRegistry.register(RaccoonModel.LAYER_LOCATION, RaccoonModel::getTexturedModelData);
        EntityModelLayerRegistry.register(SquirrelModel.LAYER_LOCATION, SquirrelModel::getTexturedModelData);
        EntityModelLayerRegistry.register(DeerModel.LAYER_LOCATION, DeerModel::getTexturedModelData);
        EntityModelLayerRegistry.register(OwlModel.LAYER_LOCATION, OwlModel::getTexturedModelData);
        EntityModelLayerRegistry.register(BoarModel.LAYER_LOCATION, BoarModel::getTexturedModelData);
        EntityModelLayerRegistry.register(BisonModel.LAYER_LOCATION, BisonModel::getTexturedModelData);
        EntityModelLayerRegistry.register(TurkeyModel.LAYER_LOCATION, TurkeyModel::getTexturedModelData);
        EntityModelLayerRegistry.register(DogModel.LAYER_LOCATION, DogModel::getTexturedModelData);
        EntityModelLayerRegistry.register(MiniSheepModel.LAYER_LOCATION, MiniSheepModel::getTexturedModelData);
        EntityModelLayerRegistry.register(CompletionistBannerRenderer.LAYER_LOCATION, CompletionistBannerRenderer::createBodyLayer);
    }

    public static void registerScreens(){
        MenuRegistry.registerScreenFactory(BountyBlockScreenHandler.BOUNTY_BLOCK.get(),BountyBlockScreen::new);
    }
}
