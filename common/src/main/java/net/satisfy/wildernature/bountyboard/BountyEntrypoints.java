package net.satisfy.wildernature.bountyboard;

import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.networking.NetworkManager;
import net.satisfy.wildernature.bountyboard.contract.ContractInProgress;

public class BountyEntrypoints {

    public static void clientEntry(){
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, BountyBlockNetworking.ID_SCREEN_UPDATE, BountyBlockNetworking::c_onServerUpdate);
    }

    public static void serverEntry(){
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, BountyBlockNetworking.ID_SCREEN_ACTION, BountyBlockNetworking::s_handleClientAction);
        EntityEvent.LIVING_DEATH.register(ContractInProgress::onEntityDeath);
    }

}
