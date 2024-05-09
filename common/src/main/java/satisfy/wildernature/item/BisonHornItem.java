package satisfy.wildernature.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BisonHornItem extends Item {
    private final SoundEvent soundEvent;

    public BisonHornItem(Item.Properties properties, SoundEvent soundEvent) {
        super(properties);
        this.soundEvent = soundEvent;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (!player.getCooldowns().isOnCooldown(this)) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.NEUTRAL, 1.0F, 1.5F);
            player.startUsingItem(interactionHand);
            player.getCooldowns().addCooldown(this, 200);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }
}
