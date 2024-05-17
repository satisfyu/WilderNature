package satisfy.wildernature.player.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import satisfy.wildernature.WilderNature;

public class WolfFurChestplateModel<T extends Entity> extends EntityModel<T> {

    // vanilla textures are kept in 'minecraft\textures\models\armor'
    // we're locating files with an additional folder to ease grouping of related textures
    public static final ResourceLocation WOLF_FUR_CHESTPLATE_TEXTURE = new ResourceLocation(WilderNature.MOD_ID, "textures/models/armor/wolf_fur/wolf_fur_chestplate.png");

    private final ModelPart chestplate; // this should feel similar to entity models (Bison, Boar, Deer, etc.)

    public WolfFurChestplateModel(ModelPart root) {
         super(RenderType::entityCutoutNoCull); // method reference
         // super(resourceLocation -> RenderType.entityCutoutNoCull(resourceLocation)); // lambda
        // super(resourceLocation -> {
        //     return RenderType.armorCutoutNoCull(resourceLocation);
        // }); // also a lambda, these all do the same thing

        // ^^^
        // super is calling to the SUPER-class of WolfFurChestplateModel, which is the EntityModel class that it's extending.
        // we're essentially setting how we want to render our model through the class we're extending.

        this.chestplate = root.getChild("chestplate");
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {
        // nothing is animated, currently
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float redChannel, float greenChannel, float blueChannel, float alphaChannel) {
        chestplate.render(poseStack, vertexConsumer, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, 1.0f); // renders, handles applying overlaid textures as well
    }

    // check out LayerDefinitions

    // public for use in the client initializer of each loader
    public static LayerDefinition createBodyLayer() {

        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        part.addOrReplaceChild("chestplate", CubeListBuilder.create()
                .texOffs(0, 0).addBox(0.0f, 0.0f, 0.0f, 16.0f, 16.0f, 16.0f), // create a 16x16 cube, begin drawing the texture from pixel (0, 0)

                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)); // offsetX, offsetY, offsetZ, rotX, rotY, rotZ

        return LayerDefinition.create(mesh, 64, 64);
    }
}
