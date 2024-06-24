package net.satisfy.wildernature;

import dev.architectury.event.events.common.LifecycleEvent;
import net.satisfy.wildernature.bountyboard.BountyEntrypoints;
import net.satisfy.wildernature.bountyboard.contract.ContractInProgress;
import net.satisfy.wildernature.registry.EntityRegistry;
import net.satisfy.wildernature.registry.ObjectRegistry;
import net.satisfy.wildernature.registry.SoundRegistry;
import net.satisfy.wildernature.registry.TabRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WilderNature {
    public static final String MOD_ID = "wildernature";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void info(String info, Object... objects){
        LOGGER.info(info, objects);
    }

    public static void init() {
        ObjectRegistry.init();
        EntityRegistry.init();
        TabRegistry.init();
        SoundRegistry.init();
        BountyEntrypoints.serverEntry();
    }

    public static void commonInit() {
        LifecycleEvent.SERVER_BEFORE_START.register(instance -> {
            ContractInProgress.progressPerPlayer.clear();
        });
        // InteractionEvent.RIGHT_CLICK_ITEM.register(Truffling::onPlayerUseEat); // same as: (player, hand) -> Truffling.onPlayerUseItem(player, hand)
    }
}

