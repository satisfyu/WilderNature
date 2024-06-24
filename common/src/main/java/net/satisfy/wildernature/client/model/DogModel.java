package net.satisfy.wildernature.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.satisfy.wildernature.entity.DogEntity;
import net.satisfy.wildernature.entity.animation.DogAnimation;
import net.satisfy.wildernature.util.WilderNatureIdentifier;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class DogModel<T extends DogEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new WilderNatureIdentifier("dog"), "main");
    private final ModelPart head;
    private final ModelPart upper_body;
    private final ModelPart body;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart tail;
    private final ModelPart mouth;
    private final ModelPart ears;
    private final ModelPart ear_left;
    private final ModelPart ear_right;
    private final ModelPart realTail;

    public DogModel(ModelPart root) {
        this.head = root.getChild("head");
        this.mouth = this.head.getChild("mouth");
        this.ears = this.head.getChild("ears");
        this.ear_left = this.ears.getChild("ear_left");
        this.ear_right = this.ears.getChild("ear_right");
        this.upper_body = root.getChild("upper_body");
        this.body = root.getChild("body");
        this.leftFrontLeg = root.getChild("leftFrontLeg");
        this.rightFrontLeg = root.getChild("rightFrontLeg");
        this.leftHindLeg = root.getChild("leftHindLeg");
        this.rightHindLeg = root.getChild("rightHindLeg");
        this.tail = root.getChild("tail");
        this.realTail = this.tail.getChild("realTail");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -5.0F, -6.0F, 7.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, -8.0F));

        PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(27, 0).addBox(-2.0F, -2.01F, -2.5F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, -6.0F));

        PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(-0.5F, -5.0F, -2.0F));

        PartDefinition ear_left = ears.addOrReplaceChild("ear_left", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7489F));

        PartDefinition ear_left_r1 = ear_left.addOrReplaceChild("ear_left_r1", CubeListBuilder.create().texOffs(26, 57).addBox(-1.0F, -2.0F, -1.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -0.5F, 3.1416F, 0.0F, 0.0F));

        PartDefinition ear_right = ears.addOrReplaceChild("ear_right", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7489F));

        PartDefinition ear_right_r1 = ear_right.addOrReplaceChild("ear_right_r1", CubeListBuilder.create().texOffs(26, 57).mirror().addBox(-1.0F, -2.0F, -1.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, -0.5F, 3.1416F, 0.0F, 0.0F));

        PartDefinition upper_body = partdefinition.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 12.0F, -7.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-3.5F, -3.0F, 7.0F, 7.0F, 8.0F, 10.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 12.0F, -7.0F));

        PartDefinition leftFrontLeg = partdefinition.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(0, 54).mirror().addBox(-1.0F, -1.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, 18.0F, -5.0F));

        PartDefinition rightFrontLeg = partdefinition.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(0, 54).addBox(-1.0F, -1.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 18.0F, -5.0F));

        PartDefinition leftHindLeg = partdefinition.addOrReplaceChild("leftHindLeg", CubeListBuilder.create().texOffs(13, 54).mirror().addBox(-1.0F, -1.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.3F, 18.0F, 7.0F));

        PartDefinition rightHindLeg = partdefinition.addOrReplaceChild("rightHindLeg", CubeListBuilder.create().texOffs(13, 54).addBox(-1.0F, -1.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3F, 18.0F, 7.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 15.0F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(46, 19).addBox(-1.0F, 2.0F, -5.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition realTail = tail.addOrReplaceChild("realTail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail_r2 = realTail.addOrReplaceChild("tail_r2", CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, 2.0F, -5.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        upper_body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public @NotNull ModelPart root() {
        return body;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * 0.017453292F;

        this.animateWalk(DogAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
    }
}
