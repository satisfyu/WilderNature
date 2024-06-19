package satisfy.wildernature.bountyboard.contract;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ContractItem extends Item {

    public static final String TAG_PLAYER = "player_uuid";
    public static final String TAG_CONTRACT_ID = "contract_id";
    public static final String TAG_NAME = "contract_name";
    public static final String TAG_DESCRIPTION = "contract_description";
    public static final String TAG_COUNT_LEFT = "count_left";
    public static final String TAG_COUNT_TOTAL = "count_total";
    public ContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if(itemStack.getTag() == null){
            list.add(Component.literal("Error: no data in item"));
            return;
        }
        var nameSplit = Component.translatable(itemStack.getTag().getString(TAG_NAME));
        var descriptionSplit = Component.translatable(itemStack.getTag().getString(TAG_DESCRIPTION));
        var progressSplit = Component.translatable("text.gui.wildernature.bounty.progress", itemStack.getTag().getInt(TAG_COUNT_TOTAL)-itemStack.getTag().getInt(TAG_COUNT_LEFT), itemStack.getTag().getInt(TAG_COUNT_TOTAL));
        list.add(nameSplit);
        list.add(descriptionSplit);
        list.add(progressSplit);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if(level.isClientSide())
            return;
        if(itemStack.getTag() == null)
        {
            return;
        }
        if(entity instanceof Player player){
            if(player.getUUID()!=itemStack.getTag().getUUID(TAG_PLAYER))
                return;
            var progress = ContractInProgress.progressPerPlayer.get(itemStack.getTag().getUUID(TAG_PLAYER));
            if(progress==null){
                itemStack.setCount(0);
                return;
            }
            itemStack.getTag().putString(TAG_CONTRACT_ID,progress.s_contract.toString());
            itemStack.getTag().putString(TAG_NAME,progress.s_getContract().name());
            itemStack.getTag().putString(TAG_DESCRIPTION,progress.s_getContract().description());
            itemStack.getTag().putInt(TAG_COUNT_TOTAL,progress.s_getContract().count());
            itemStack.getTag().putInt(TAG_COUNT_LEFT,progress.count);
        }
    }
}
