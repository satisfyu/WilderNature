package net.satisfy.wildernature;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.satisfy.wildernature.registry.*;
import dev.architectury.platform.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.satisfy.wildernature.network.BountyEntrypoints;
import net.satisfy.wildernature.util.contract.ContractInProgress;
import net.satisfy.wildernature.registry.EntityRegistry;
import net.satisfy.wildernature.registry.ObjectRegistry;
import net.satisfy.wildernature.registry.SoundRegistry;
import net.satisfy.wildernature.registry.TabRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WilderNature {
    public static final String MOD_ID = "wildernature";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void info(String info, Object... objects){
        LOGGER.info(info, objects);
    }
    public static void infoDebug(String info, Object... objects){
        if(Platform.isDevelopmentEnvironment()){
            info("_"+info, objects);
        }
        //Minecraft.getInstance().gui.getChat().addMessage(Component.literal(("_"+info).formatted(objects)));
    }

    public static void init() {
        ObjectRegistry.init();
        EntityRegistry.init();
        RecipeRegistry.init();
        TabRegistry.init();
        SoundRegistry.init();
        BountyEntrypoints.serverEntry();
    }

    public static void commonInit() {
        ObjectRegistry.FuelRegistry();
        LifecycleEvent.SERVER_BEFORE_START.register(instance -> ContractInProgress.progressPerPlayer.clear());
        LifecycleEvent.SERVER_BEFORE_START.register(instance -> {
            ContractInProgress.progressPerPlayer.clear();
        });
    }
}

