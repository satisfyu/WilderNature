package satisfy.wildernature.util;

import dev.architectury.event.CompoundEventResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class Truffling {

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
//        @Nullable FoodValue foodValue = Configuration.FOOD_VALUES.get(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString());
//        return foodValue != null ? foodValue : new FoodValue(Configuration.TRUFFLING_ADDITIONAL_NUTRITION.get(), Configuration.TRUFFLING_ADDITIONAL_SATURATION_MODIFIER.get().floatValue());

        return new FoodValue(2, 2.0F);
    }

    /**
     *
     */
    public static CompoundEventResult<ItemStack> onPlayerUseEat(Player player, InteractionHand hand) {

        player.sendSystemMessage(Component.literal("Used item in " + hand.toString()));

        return CompoundEventResult.interruptFalse(player.getItemInHand(hand));
    }

    public record FoodValue(int nutrition, float saturationModifier) {
        @Override
        public String toString() {
            return "{Nutrition:" + nutrition + ",Saturation:" + saturationModifier + "}";
        }
    }
}
