package net.satisfy.wildernature.forge.mixin;

//import io.github.mortuusars.salt.Salt;
//import io.github.mortuusars.salt.Salting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.satisfy.wildernature.util.Truffling;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @Shadow
    public void eat(int foodLevel, float saturationModifier) {
    }

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"), cancellable = true)
    private void onEat(Item pItem, ItemStack pStack, LivingEntity entity, CallbackInfo ci) {
        if (!Truffling.isTruffled(pStack))
            return;

        FoodProperties foodProperties = pStack.getFoodProperties(entity);
        Truffling.FoodValue additionalFoodValues = Truffling.getAdditionalFoodValue(pStack);

        eat(foodProperties.getNutrition() + additionalFoodValues.nutrition(),
                foodProperties.getSaturationModifier() + additionalFoodValues.saturationModifier());

        // if (entity instanceof ServerPlayer serverPlayer && !serverPlayer.isCreative())
        //     Salt.Advancements.SALTED_FOOD_CONSUMED.trigger(serverPlayer);

        ci.cancel();
    }
}
