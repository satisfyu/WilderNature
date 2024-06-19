package satisfy.wildernature.bountyboard;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import satisfy.wildernature.util.WilderNatureIdentifier;

public class BountyBlockNetworking {
    public static final ResourceLocation ID_SCREEN_UPDATE = new WilderNatureIdentifier("screen_update");
    public static final ResourceLocation ID_SCREEN_ACTION = new WilderNatureIdentifier("screen_action");
    public static final int MAX_SIZE = 32768;

    static void c_onServerUpdate(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player.containerMenu instanceof BountyBlockScreenHandler screenHandler) {
            screenHandler.c_onServerUpdate(player, buf);
        }
    }
    public enum BountyServerUpdateType{
        SEND_BOARD_DATA,
        UPDATE_CONTRACTS,
        SET_ACTIVE_CONTRACT,
        CLEAR_ACTIVE_CONTRACT,
        MULTI
    }
    static void s_handleClientAction(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player.containerMenu instanceof BountyBlockScreenHandler screenHandler) {
            screenHandler.s_handleClientAction((ServerPlayer) player, buf);
        }
    }

    public enum BountyClientActionType {
        REROLL,
        CONFIRM_CONTRACT,
        FINISH_CONTRACT
    }
}
