package net.satisfy.wildernature.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.satisfy.wildernature.registry.RecipeRegistry;
import net.satisfy.wildernature.registry.TagsRegistry;
import net.satisfy.wildernature.util.Truffling;
import org.jetbrains.annotations.NotNull;

public class TrufflingRecipe extends CustomRecipe {
    private final String group;
    private final NonNullList<Ingredient> ingredients;

    public TrufflingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients) {
        super(id, CraftingBookCategory.MISC);
        this.group = group;
        this.ingredients = ingredients;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer pContainer) {
        return NonNullList.withSize(pContainer.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, @NotNull Level level) {
        boolean hasFoodInput = false;
        NonNullList<Boolean> matches = NonNullList.withSize(this.ingredients.size(), false);
        int itemsCount = 0;

        for (int slotIndex = 0; slotIndex < craftingContainer.getContainerSize(); slotIndex++) {
            ItemStack stackInSlot = craftingContainer.getItem(slotIndex);
            if (stackInSlot.isEmpty())
                continue;

            itemsCount++;

            if ((stackInSlot.is(TagsRegistry.CAN_BE_TRUFFLED) || stackInSlot.getItem().isEdible()) && !hasFoodInput && !Truffling.isTruffled(stackInSlot))
                hasFoodInput = true;

            for (int ingredientIndex = 0; ingredientIndex < this.ingredients.size(); ingredientIndex++) {
                if (this.ingredients.get(ingredientIndex).test(stackInSlot) && !matches.get(ingredientIndex))
                    matches.set(ingredientIndex, true);
            }
        }

        return hasFoodInput && matches.stream().allMatch(match -> match) && itemsCount == this.ingredients.size() + 1;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer craftingContainer, @NotNull RegistryAccess registryAccess) {
        for (int index = 0; index < craftingContainer.getContainerSize(); index++) {
            ItemStack itemStack = craftingContainer.getItem(index);

            if (itemStack.is(TagsRegistry.CAN_BE_TRUFFLED) || itemStack.getItem().isEdible()) {
                ItemStack resultStack = itemStack.copy();
                resultStack.setCount(1);

                return Truffling.setTruffled(resultStack);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 || height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.TRUFFLING.get();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public static class Serializer implements RecipeSerializer<TrufflingRecipe> {
        public @NotNull TrufflingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            return new TrufflingRecipe(recipeId, group, getIngredients(json));
        }

        private NonNullList<Ingredient> getIngredients(JsonObject json) {
            JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty())
                    ingredients.add(ingredient);
            }

            if (ingredients.isEmpty())
                throw new JsonParseException("No ingredients for truffling recipe");
            else if (ingredients.size() > 3 * 3)
                throw new JsonParseException("Too many ingredients for truffling recipe. The maximum is 9");
            return ingredients;
        }

        public @NotNull TrufflingRecipe fromNetwork(@NotNull ResourceLocation recipeID, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            int ingredientsCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientsCount, Ingredient.EMPTY);

            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            return new TrufflingRecipe(recipeID, group, ingredients);
        }

        public void toNetwork(FriendlyByteBuf buffer, TrufflingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }
        }
    }
}
