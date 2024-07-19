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
import net.minecraft.client.renderer.entity.IllusionerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.monster.Illusioner;
import net.satisfy.wildernature.WilderNature;
import net.satisfy.wildernature.entity.RaccoonEntity;
import net.satisfy.wildernature.entity.animation.RaccoonAnimation;
import net.satisfy.wildernature.util.WilderNatureIdentifier;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class RaccoonModel<T extends RaccoonEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new WilderNatureIdentifier("raccoon"), "main");
    private final ModelPart root;

    //    private final ModelPart body;
//    private final ModelPart head;
//    private final ModelPart leftHindLeg;
//    private final ModelPart rightHindLeg;
//    private final ModelPart leftFrontLeg;
//    private final ModelPart rightFrontLeg;
//    private final ModelPart tail;
    private float legMotionPos;



    public RaccoonModel(ModelPart root) {
        this.root = root;
//        this.body = root.getChild("body");
//        this.head = root.getChild("head");
//        this.leftHindLeg = root.getChild("leftHindLeg");
//        this.rightHindLeg = root.getChild("rightHindLeg");
//        this.leftFrontLeg = root.getChild("leftFrontLeg");
//        this.rightFrontLeg = root.getChild("rightFrontLeg");
//        this.tail = root.getChild("tail");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.5F));

        PartDefinition animroot = root.addOrReplaceChild("animroot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = animroot.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -5.5F, -3.0F, 8.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 1.5708F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 13).addBox(-4.0F, -6.0F, -2.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.0F, -6.0F, -2.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 13).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, 24).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.5F, 0.0F));

        PartDefinition leftHindLeg = animroot.addOrReplaceChild("leftHindLeg", CubeListBuilder.create().texOffs(8, 30).addBox(-0.005F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 0.0F, 3.0F));

        PartDefinition rightHindLeg = animroot.addOrReplaceChild("rightHindLeg", CubeListBuilder.create().texOffs(0, 30).addBox(0.005F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 3.0F));

        PartDefinition leftFrontLeg = animroot.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(8, 30).addBox(-1.005F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, -4.0F));

        PartDefinition rightFrontLeg = animroot.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(0, 30).addBox(-0.995F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -4.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    public void prepareMobModel(T raccoon, float f, float g, float h) {
//        this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
//        this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + 3.1415927F) * 1.4F * g;
//        this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + 3.1415927F) * 1.4F * g;
//        this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
//        this.rightHindLeg.visible = true;
//        this.leftHindLeg.visible = true;
//        this.rightFrontLeg.visible = true;
//        this.leftFrontLeg.visible = true;
//        if (raccoon.isSleeping()) {
//            this.body.zRot = -1.5707964F;
//            this.body.setPos(0.0F, 21.0F, -6.0F);
//            this.tail.xRot = -2.6179938F;
//            if (this.young) {
//                this.tail.xRot = -2.1816616F;
//                this.body.setPos(0.0F, 21.0F, -2.0F);
//            }

//            this.head.setPos(1.0F, 19.49F, -3.0F);
//            this.head.xRot = 0.0F;
//            this.head.yRot = -2.0943952F;
//            this.head.zRot = 0.0F;
//            this.rightHindLeg.visible = false;
//            this.leftHindLeg.visible = false;
//            this.rightFrontLeg.visible = false;
//            this.leftFrontLeg.visible = false;
//        }
    }

    public void setupAnim(T raccoon, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        WilderNature.infoDebug("ageIntTicks: %f".formatted(ageInTicks));
        if(raccoon.isRaccoonRunning()) {
            this.animateWalk(RaccoonAnimation.run,limbSwing,limbSwingAmount,1f, 2.5f);
        } else {
            this.animateWalk(RaccoonAnimation.walk,limbSwing,limbSwingAmount,2f, 2.5f);
        }
        this.animate(raccoon.openDoorState, RaccoonAnimation.opening_door, ageInTicks,1.0f);
        this.animate(raccoon.washingState, RaccoonAnimation.wash, ageInTicks,1.0f);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//        leftHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//        rightHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//        leftFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//        rightFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public @NotNull ModelPart root() {
        return root;
    }
}