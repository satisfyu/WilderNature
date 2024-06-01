package satisfy.wildernature.item;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import satisfy.wildernature.client.gui.CompendiumGui;

public class CompendiumItem extends Item {
    public CompendiumItem(Properties settings) {
        super(settings);
    }

    public static void setCompendiumScreen() {
        Minecraft.getInstance().setScreen(new CompendiumGui() {
            @Override
            public @NotNull Component getTitle() {
                return super.getTitle();
            }
        });
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (world.isClientSide()) {
            setCompendiumScreen();
        } else {
            user.containerMenu.broadcastChanges();
        }
        user.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
}
