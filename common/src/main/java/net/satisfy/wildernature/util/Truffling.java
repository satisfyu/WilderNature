package net.satisfy.wildernature.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class Truffling {

    public record FoodValue(int nutrition, float saturationModifier) {
        @Override
        public String toString() {
            return "{Nutrition:" + nutrition + ",Saturation:" + saturationModifier + "}";
        }
    }

    private static final String TRUFFLED_KEY = "Truffled";

    public static boolean isTruffled(ItemStack itemStack) {
        return itemStack.hasTag() && Objects.requireNonNull(itemStack.getTag()).contains(TRUFFLED_KEY);
    }

    /**
     * Same ItemStack is returned.
     */
    public static ItemStack setTruffled(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean(TRUFFLED_KEY, true);
        return itemStack;
    }

    public static FoodValue getAdditionalFoodValue(ItemStack stack) {

        // here we are adding a flat value to the amount already on the food.
        // satisfy requested 20% opposed to a flat amount, so we're leaving these as 0
        return new FoodValue(0, 0.0F);
        
        // original implementation relying on config
        // @Nullable FoodValue foodValue = Configuration.FOOD_VALUES.get(Objects.requireNonNull( BuiltInRegistries.ITEM.getKey(stack.getItem()) ).toString());
        // return foodValue != null ? foodValue : new FoodValue(Configuration.TRUFFLING_ADDITIONAL_NUTRITION.get(), Configuration.TRUFFLING_ADDITIONAL_SATURATION_MODIFIER.get().floatValue());
    }
}
