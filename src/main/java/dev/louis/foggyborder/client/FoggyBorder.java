package dev.louis.foggyborder.client;

import com.mojang.logging.LogUtils;
import dev.louis.foggyborder.client.config.Config;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;

public class FoggyBorder implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Config config = Config.read();

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        LOGGER.info("Foggy Border initialised");
    }
}
