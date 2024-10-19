package net.satisfy.wildernature.network;

import dev.architectury.event.events.common.TickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.event.events.common.EntityEvent;
import net.satisfy.wildernature.util.contract.ContractInProgress;

public class BountyEntrypoints {
    public static void clientEntry(){
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, BountyBlockNetworking.ID_SCREEN_UPDATE, BountyBlockNetworking::c_onServerUpdate);
    }

    public static void serverEntry(){
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, BountyBlockNetworking.ID_SCREEN_ACTION, BountyBlockNetworking::s_handleClientAction);
        EntityEvent.LIVING_DEATH.register(ContractInProgress::onEntityDeath);
        TickEvent.SERVER_POST.register(ContractInProgress::onServerTick);
    }
}
