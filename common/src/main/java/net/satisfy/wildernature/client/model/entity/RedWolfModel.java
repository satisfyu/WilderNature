package net.satisfy.wildernature.client.model.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.entity.RedWolfEntity;
import net.satisfy.wildernature.entity.animation.RedWolfAnimation;
import net.satisfy.wildernature.util.WilderNatureIdentifier;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class RedWolfModel<T extends RedWolfEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new WilderNatureIdentifier("red_wolf"), "main");
    private static final String REAL_TAIL = "real_tail";
    private final ModelPart root;

    public RedWolfModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(1.0F, 16.0F, 8.0F));

        PartDefinition animroot = root.addOrReplaceChild("animroot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightFrontLeg = animroot.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(26, 33).addBox(0.0F, 0.0F, -0.99F, 2.0F, 8.0F, 2.01F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, -12.0F));

        PartDefinition leftFrontLeg = animroot.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, 0.0F, -0.99F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 0.0F, -12.0F));

        PartDefinition rightHindLeg = animroot.addOrReplaceChild("rightHindLeg", CubeListBuilder.create().texOffs(26, 33).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, -1.0F));

        PartDefinition leftHindLeg = animroot.addOrReplaceChild("leftHindLeg", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 0.0F, -1.0F));

        PartDefinition body = animroot.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -7.0F, -5.0F, 6.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, -6.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 22).addBox(-3.0F, -3.0F, -4.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 0).addBox(-3.0F, -7.0F, 0.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.0F, -7.0F, 0.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 27).addBox(-1.0F, 0.0F, -7.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.5F, -1.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, 0.0F, -2.25F, 4.0F, 9.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(23, 17).addBox(-1.0F, 9.0F, -2.25F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 7.0F, -1.0F));

        PartDefinition real_tail = tail.addOrReplaceChild("real_tail", CubeListBuilder.create(), PartPose.offset(1.0F, 12.0F, -6.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public @NotNull ModelPart root() {
        return root;
    }
//
//    protected Iterable<ModelPart> headParts() {
//        return ImmutableList.of(this.head);
//    }
//
//    protected Iterable<ModelPart> bodyParts() {
//        return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.tail, this.upperBody);
//    }

//    public void prepareMobModel(T wolf, float f, float g, float h) {
//        if (wolf.isAngry()) {
//            this.tail.yRot = 0.0F;
//        } else {
//            this.tail.yRot = Mth.cos(f * 0.6662F) * 1.4F * g;
//        }
//
//        if (wolf.isInSittingPose()) {
//            this.upperBody.setPos(-1.0F, 16.0F, -3.0F);
//            this.upperBody.xRot = 1.2566371F;
//            this.upperBody.yRot = 0.0F;
//            this.body.setPos(0.0F, 18.0F, 0.0F);
//            this.body.xRot = 0.7853982F;
//            this.tail.setPos(-1.0F, 21.0F, 6.0F);
//            this.rightHindLeg.setPos(-2.5F, 22.7F, 2.0F);
//            this.rightHindLeg.xRot = 4.712389F;
//            this.leftHindLeg.setPos(0.5F, 22.7F, 2.0F);
//            this.leftHindLeg.xRot = 4.712389F;
//            this.rightFrontLeg.xRot = 5.811947F;
//            this.rightFrontLeg.setPos(-2.49F, 17.0F, -4.0F);
//            this.leftFrontLeg.xRot = 5.811947F;
//            this.leftFrontLeg.setPos(0.51F, 17.0F, -4.0F);
//        } else {
//            this.body.setPos(0.0F, 14.0F, 2.0F);
//            this.body.xRot = 1.5707964F;
//            this.upperBody.setPos(-1.0F, 14.0F, -3.0F);
//            this.upperBody.xRot = this.body.xRot;
//            this.tail.setPos(-1.0F, 12.0F, 8.0F);
//            this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
//            this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
//            this.rightFrontLeg.setPos(-2.5F, 16.0F, -4.0F);
//            this.leftFrontLeg.setPos(0.5F, 16.0F, -4.0F);
//            this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
//            this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + 3.1415927F) * 1.4F * g;
//            this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + 3.1415927F) * 1.4F * g;
//            this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
//        }
//
//        this.head.zRot = wolf.getHeadRollAngle(h) + wolf.getBodyRollAngle(h, 0.0F);
//        this.upperBody.zRot = wolf.getBodyRollAngle(h, -0.08F);
//        this.body.zRot = wolf.getBodyRollAngle(h, -0.16F);
//        this.realTail.zRot = wolf.getBodyRollAngle(h, -0.2F);
//    }

    public void setupAnim(T wolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if(wolf.isSneaking()){
            animateWalk(RedWolfAnimation.sneak, limbSwing, limbSwingAmount, 2f, 2.5f);
        }
        else {
            animateWalk(RedWolfAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
        }
        animate(wolf.attackState,RedWolfAnimation.attack,ageInTicks);
    }
}
