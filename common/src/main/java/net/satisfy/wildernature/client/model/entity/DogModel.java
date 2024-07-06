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
import net.satisfy.wildernature.entity.DogEntity;
import net.satisfy.wildernature.entity.animation.DogAnimation;
import net.satisfy.wildernature.util.WilderNatureIdentifier;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class DogModel<T extends DogEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new WilderNatureIdentifier("dog"), "main");
    private final ModelPart dog;
    private final ModelPart head;
    private final ModelPart body;

    public DogModel(ModelPart root) {
        this.dog = root.getChild("dog");
        this.head = dog.getChild("head");
        ModelPart mouth = head.getChild("mouth");
        ModelPart ears = head.getChild("ears");
        ModelPart ear_left = ears.getChild("ear_left");
        ModelPart ear_right = ears.getChild("ear_right");
        ModelPart tail = dog.getChild("tail");
        ModelPart realTail = tail.getChild("realTail");
        ModelPart rightHindLeg = dog.getChild("rightHindLeg");
        ModelPart leftHindLeg = dog.getChild("leftHindLeg");
        ModelPart rightFrontLeg = dog.getChild("rightFrontLeg");
        ModelPart leftFrontLeg = dog.getChild("leftFrontLeg");
        this.body = dog.getChild("body");
        ModelPart upper_body = dog.getChild("upper_body");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition dog = partdefinition.addOrReplaceChild("dog", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = dog.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -5.0F, -6.0F, 7.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, -8.0F));

        PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(27, 0).addBox(-2.0F, -2.01F, -2.5F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, -6.0F));

        PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(-0.5F, -5.0F, -2.0F));

        PartDefinition ear_left = ears.addOrReplaceChild("ear_left", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7489F));

        PartDefinition ear_left_r1 = ear_left.addOrReplaceChild("ear_left_r1", CubeListBuilder.create().texOffs(26, 57).addBox(-1.0F, -2.0F, -1.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -0.5F, 3.1416F, 0.0F, 0.0F));

        PartDefinition ear_right = ears.addOrReplaceChild("ear_right", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7489F));

        PartDefinition ear_right_r1 = ear_right.addOrReplaceChild("ear_right_r1", CubeListBuilder.create().texOffs(26, 57).mirror().addBox(-1.0F, -2.0F, -1.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, -0.5F, 3.1416F, 0.0F, 0.0F));

        PartDefinition tail = dog.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, 15.0F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(46, 19).addBox(-1.0F, 2.0F, -5.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition realTail = tail.addOrReplaceChild("realTail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail_r2 = realTail.addOrReplaceChild("tail_r2", CubeListBuilder.create().texOffs(46, 10).addBox(-1.0F, 2.0F, -5.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition rightHindLeg = dog.addOrReplaceChild("rightHindLeg", CubeListBuilder.create().texOffs(13, 54).addBox(-1.0F, -1.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3F, -6.0F, 7.0F));

        PartDefinition leftHindLeg = dog.addOrReplaceChild("leftHindLeg", CubeListBuilder.create().texOffs(13, 54).mirror().addBox(-1.0F, -1.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.3F, -6.0F, 7.0F));

        PartDefinition rightFrontLeg = dog.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(0, 54).addBox(-1.0F, -1.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -6.0F, -5.0F));

        PartDefinition leftFrontLeg = dog.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(0, 54).mirror().addBox(-1.0F, -1.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, -6.0F, -5.0F));

        PartDefinition body = dog.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-3.5F, -3.0F, 7.0F, 7.0F, 8.0F, 10.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -12.0F, -7.0F));

        PartDefinition upper_body = dog.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -12.0F, -7.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        dog.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
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