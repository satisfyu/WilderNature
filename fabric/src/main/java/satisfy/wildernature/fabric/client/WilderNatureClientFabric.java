package satisfy.wildernature.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import satisfy.wildernature.client.WilderNatureClient;
import satisfy.wildernature.player.layer.WolfFurChestplateLayer;
import satisfy.wildernature.player.model.WolfFurChestplateModel;

public class WilderNatureClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WilderNatureClient.preInitClient();

        EntityModelLayerRegistry.registerModelLayer(WilderNatureClient.WOLF_FUR_CHESTPLATE_LAYER, WolfFurChestplateModel::createBodyLayer);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerRenderer renderer) {
                registrationHelper.register(new WolfFurChestplateLayer<>(renderer));
            }
        });
    }
}
