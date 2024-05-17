package satisfy.wildernature.player.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import satisfy.wildernature.client.WilderNatureClient;
import satisfy.wildernature.player.model.WolfFurChestplateModel;

public class WolfFurChestplateLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final WolfFurChestplateModel<T> model;

    public WolfFurChestplateLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);

        model = new WolfFurChestplateModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(WilderNatureClient.WOLF_FUR_CHESTPLATE_LAYER));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {

        // we need to check if our player is actually wearing the chestplate
        boolean shouldRender = false;

        for (ItemStack stack : ((Player) entity).getInventory().armor) {
            // change this to check for the actual item, not against the item's name
            if (stack.getItem().getDescriptionId().toLowerCase().contains("fur")) {
                shouldRender = true;
            }
        }

        if (shouldRender) {

            poseStack.pushPose();

            poseStack.translate(0.0d, 0.0d, 0.0d); // helpful for fixing offset issues

            renderColoredCutoutModel(model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, 1.0f, 1.0f, 1.0f);

            poseStack.popPose();
        }

    }

    @Override
    protected ResourceLocation getTextureLocation(T entity) {
        return WolfFurChestplateModel.WOLF_FUR_CHESTPLATE_TEXTURE;
    }
}
