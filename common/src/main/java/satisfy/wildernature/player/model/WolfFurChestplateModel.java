package satisfy.wildernature.player.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import satisfy.wildernature.WilderNature;

public class WolfFurChestplateModel<T extends Entity> extends EntityModel<T> {

    public static final ResourceLocation WOLF_FUR_CHESTPLATE_TEXTURE = new ResourceLocation(WilderNature.MOD_ID, "textures/models/armor/fur_cloak.png");

    private final ModelPart chestplate;
    private final ModelPart cape;

    public WolfFurChestplateModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.chestplate = root.getChild("chestplate");
        this.cape = this.chestplate.getChild("cape");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            float forwardVelocity = (float) livingEntity.getDeltaMovement().horizontalDistance();
            if (forwardVelocity > 0) {
                this.cape.xRot = 0.2F + (0.1F * forwardVelocity);
            } else {
                this.cape.xRot = 0.0F;
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        chestplate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition chestplate = part.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(0, 0).addBox(-17.0F, -3.0F, -1.0F, 18.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 23).addBox(-12.0F, -4.0F, 4.0F, 8.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 23).addBox(-10.0F, 0.0F, 4.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 3.0F, -2.0F));

        PartDefinition cape = chestplate.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(0, 8).addBox(-6.0F, 0.0F, -0.5F, 12.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, 3.5F));

        return LayerDefinition.create(mesh, 64, 64);
    }
}
