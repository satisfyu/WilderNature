package net.satisfy.wildernature.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.wildernature.block.entity.BountyBoardBlockEntity;
import net.satisfy.wildernature.client.model.block.BountyBoardModel;
import net.satisfy.wildernature.util.WilderNatureIdentifier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;

public class BountyBoardRenderer implements BlockEntityRenderer<BountyBoardBlockEntity> {
    private static final ResourceLocation TEXTURE = new WilderNatureIdentifier("textures/entity/bounty_board.png");
    private final BountyBoardModel<?> model;

    public BountyBoardRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new BountyBoardModel<>(context.bakeLayer(BountyBoardModel.LAYER_LOCATION));
    }

    @Override
    public void render(BountyBoardBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        BlockState blockState = blockEntity.getBlockState();
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(new Quaternionf().rotateZ((float)Math.toRadians(180.0F)));
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
