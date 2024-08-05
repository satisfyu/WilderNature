package net.satisfy.wildernature.util;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
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

    public static ItemStack setTruffled(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean(TRUFFLED_KEY, true);
        return itemStack;
    }

    public static FoodValue getAdditionalFoodValue() {
        return new FoodValue(0, 0.0F);
    }

    public static void addTruffledTooltip(ItemStack itemStack, List<Component> tooltip) {
        if (isTruffled(itemStack)) {
            tooltip.add(Component.translatable("tooltip.wildernature.truffled").withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.translatable("tooltip.wildernature.truffled.nutrition").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("tooltip.wildernature.truffled.saturationModifier").withStyle(ChatFormatting.GREEN));
        }
    }
}
