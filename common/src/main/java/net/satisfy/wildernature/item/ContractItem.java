package net.satisfy.wildernature.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.satisfy.wildernature.util.contract.ContractInProgress;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ContractItem extends Item {
    public static final String TAG_PLAYER = "player_uuid";
    public static final String TAG_CONTRACT_ID = "contract_id";
    public static final String TAG_NAME = "contract_name";
    public static final String TAG_DESCRIPTION = "contract_description";
    public static final String TAG_COUNT_LEFT = "count_left";
    public static final String TAG_COUNT_TOTAL = "count_total";
    public static final String TAG_EXPIRY_TICK = "expiry_tick";

    public ContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if (itemStack.getTag() == null) {
            list.add(Component.translatable("tooltip.wildernature.contract_error"));
            return;
        }

        var name = Component.translatable(itemStack.getTag().getString(TAG_NAME));
        var description = Component.translatable(itemStack.getTag().getString(TAG_DESCRIPTION));
        var progress = Component.translatable("text.gui.wildernature.bounty.progress",
                itemStack.getTag().getInt(TAG_COUNT_TOTAL) - itemStack.getTag().getInt(TAG_COUNT_LEFT),
                itemStack.getTag().getInt(TAG_COUNT_TOTAL));

        list.add(name);
        list.add(description);
        list.add(progress);

        if (itemStack.getTag().contains(TAG_EXPIRY_TICK) && level != null) {
            long expiryTick = itemStack.getTag().getLong(TAG_EXPIRY_TICK);
            long currentTick = level.getGameTime();
            long remainingTicks = expiryTick - currentTick;
            if (remainingTicks > 0) {
                long remainingSeconds = remainingTicks / 20;
                long minutes = remainingSeconds / 60;
                long seconds = remainingSeconds % 60;
                list.add(Component.empty());
                list.add(Component.translatable("text.gui.wildernature.bounty.time_remaining", minutes, seconds));
            } else {
                list.add(Component.translatable("text.gui.wildernature.bounty.time_remaining", 0, 0));
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (level.isClientSide() || itemStack.getTag() == null) {
            return;
        }

        if (entity instanceof Player player) {
            if (!player.getUUID().equals(itemStack.getTag().getUUID(TAG_PLAYER))) {
                return;
            }

            var progress = ContractInProgress.progressPerPlayer.get(player.getUUID());
            if (progress == null) {
                itemStack.setCount(0);
                return;
            }

            itemStack.getTag().putString(TAG_CONTRACT_ID, progress.contractResource.toString());
            itemStack.getTag().putString(TAG_NAME, progress.getContract().name());
            itemStack.getTag().putString(TAG_DESCRIPTION, progress.getContract().description());
            itemStack.getTag().putInt(TAG_COUNT_TOTAL, progress.getContract().count());
            itemStack.getTag().putInt(TAG_COUNT_LEFT, progress.count);
        }
    }
}
