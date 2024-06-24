package net.satisfy.wildernature.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.satisfy.wildernature.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class LootBagItem extends Item {
    public LootBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world.isClientSide || user.isCrouching()) {
            return super.use(world, user, hand);
        }

        ItemStack itemStack = user.getItemInHand(hand);
        ItemStack spawnedItem;
        int randomNumber = world.random.nextInt(100);

        if (randomNumber < 2) {
            spawnedItem = new ItemStack(Items.DIAMOND);
        } else if (randomNumber < 7) {
            spawnedItem = new ItemStack(Items.EMERALD);
        } else if (randomNumber < 17) {
            spawnedItem = new ItemStack(Items.BEETROOT);
        } else if (randomNumber < 47) {
            spawnedItem = new ItemStack(Items.CARROT);
        } else if (randomNumber < 97) {
            spawnedItem = new ItemStack(Items.APPLE);
        } else {
            spawnedItem = new ItemStack(ObjectRegistry.HAZELNUT.get());
        }

        world.addFreshEntity(new ItemEntity(world, user.getX(), user.getY(), user.getZ(), spawnedItem));

        if (!user.isCreative()) {
            itemStack.shrink(1);
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
}
