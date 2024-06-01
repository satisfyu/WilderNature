package satisfy.wildernature.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import satisfy.wildernature.client.model.MiniSheepModel;
import satisfy.wildernature.entity.MiniSheepEntity;
import satisfy.wildernature.util.WilderNatureIdentifier;

@Environment(value = EnvType.CLIENT)
public class MiniSheepRenderer extends MobRenderer<MiniSheepEntity, MiniSheepModel<MiniSheepEntity>> {
    private static final ResourceLocation TEXTURE = new WilderNatureIdentifier("textures/entity/minisheep.png");

    public MiniSheepRenderer(EntityRendererProvider.Context context) {
        super(context, new MiniSheepModel<>(context.bakeLayer(MiniSheepModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(MiniSheepEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(MiniSheepEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pMatrixStack.scale(0.4f, 0.4f, 0.4f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
