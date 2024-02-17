package dev.louis.foggyborder.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.louis.foggyborder.client.FoggyBorder;

public class ModMenuIntegration implements ModMenuApi {

    static {
        System.out.println("??????");
    }
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        System.out.println("???");
        return FoggyBorder.config::generateScreen;
    }
}
