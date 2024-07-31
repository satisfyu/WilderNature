package net.satisfy.wildernature.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.SnifferModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SnifferRenderer;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.client.model.entity.RaccoonModel;
import net.satisfy.wildernature.entity.RaccoonEntity;
import net.satisfy.wildernature.util.WilderNatureIdentifier;
import org.jetbrains.annotations.NotNull;


@Environment(value = EnvType.CLIENT)
public class RaccoonRenderer extends MobRenderer<RaccoonEntity, RaccoonModel<RaccoonEntity>> {
    private static final ResourceLocation RACCOON_TEXTURE = new WilderNatureIdentifier("textures/entity/raccoon.png");
    private static final ResourceLocation RACOON_SLEEP_TEXTURE = new WilderNatureIdentifier("textures/entity/raccoon.png");

    public RaccoonRenderer(EntityRendererProvider.Context context) {
        super(context, new RaccoonModel(context.bakeLayer(RaccoonModel.LAYER_LOCATION)), 0.7f);
    }

    protected void setupRotations(RaccoonEntity raccoon, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(raccoon, poseStack, f, g, h);
    }


    public @NotNull ResourceLocation getTextureLocation(RaccoonEntity entity) {
        return entity.isSleeping() ? RACOON_SLEEP_TEXTURE : RACCOON_TEXTURE;
    }

    @Override
    public void render(RaccoonEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.4f, 0.4f, 0.4f);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}

