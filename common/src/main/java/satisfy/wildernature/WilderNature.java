package satisfy.wildernature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import satisfy.wildernature.bountyboard.BountyEntrypoints;
import satisfy.wildernature.registry.EntityRegistry;
import satisfy.wildernature.registry.ObjectRegistry;
import satisfy.wildernature.registry.SoundRegistry;
import satisfy.wildernature.registry.TabRegistry;

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
        // InteractionEvent.RIGHT_CLICK_ITEM.register(Truffling::onPlayerUseEat); // same as: (player, hand) -> Truffling.onPlayerUseItem(player, hand)
    }
}

