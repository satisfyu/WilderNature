package net.satisfy.wildernature.fabric.mixin;

import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.satisfy.wildernature.util.Truffling;

@Mixin(FoodData.class)
public class FoodDataMixin {

    @Shadow
    public void eat(int foodLevel, float saturationModifier) {}

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"), cancellable = true)
    private void onEat(Item item, ItemStack itemStack, CallbackInfo ci) {

        if (!Truffling.isTruffled(itemStack)) {
            return;
        }

        FoodProperties foodProperties = itemStack.getItem().getFoodProperties();

        Truffling.FoodValue additionalFoodValues = Truffling.getAdditionalFoodValue(itemStack);

        if (foodProperties != null) {
            eat((foodProperties.getNutrition() + (int) (foodProperties.getNutrition() * 0.20F) ) + additionalFoodValues.nutrition(), (foodProperties.getSaturationModifier() + (foodProperties.getSaturationModifier() * .20F)) + additionalFoodValues.saturationModifier());
        }

        ci.cancel();
    }
}
