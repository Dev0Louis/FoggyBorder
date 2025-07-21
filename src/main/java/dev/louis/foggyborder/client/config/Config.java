package dev.louis.foggyborder.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.louis.foggyborder.client.FoggyBorder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Config {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("foggyborder.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Config DEFAULT_CONFIG = new Config();

    public boolean disableWorldborder = true;
    public boolean dontRenderBehindBorder = true;
    public float fogEndDistanceMultiplier = 1;
    public float minimumFogEndDistance = 2f;

    public void write() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            FoggyBorder.LOGGER.error("Error writing config, skipping writing.", e);
        }
    }

    public static Config read() {
        if(!Files.exists(CONFIG_PATH)) {
            DEFAULT_CONFIG.write();
            return DEFAULT_CONFIG;
        }
        try {
            return GSON.fromJson(Files.readString(CONFIG_PATH), Config.class);
        } catch (IOException e) {
            FoggyBorder.LOGGER.debug("Error reading config, using default.");
            return DEFAULT_CONFIG;
        }
    }

    public Screen generateScreen(Screen parent) {
        if(!FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) throw new IllegalStateException("Not YACL?");
        var screen = YetAnotherConfigLib.createBuilder()
                .title(yaclText("title", "main"))
                .category(
                    ConfigCategory.createBuilder()
                            .name(category("client"))
                            .option(
                                    booleanOption(
                                            "disable-worldborder",
                                            true,
                                            () -> this.disableWorldborder,
                                            (value) -> this.disableWorldborder = value
                                    )
                            )
                            .option(
                                    floatSlideOption(
                                            "fog-end-distance-multiplier",
                                            1f,
                                            0.1f,
                                            2f,
                                            () -> this.fogEndDistanceMultiplier,
                                            (value) -> this.fogEndDistanceMultiplier = value
                                    )
                            )
                            .option(
                                    floatSlideOption(
                                            "minimum-fog-end-distance",
                                            1.5f,
                                            0.1f,
                                            20f,
                                            () -> this.minimumFogEndDistance,
                                            (value) -> this.minimumFogEndDistance = value
                                    )
                            )
                            .option(
                                    booleanOption(
                                            "dont-render-behind-order",
                                            true,
                                            () -> this.dontRenderBehindBorder,
                                            (value) -> {
                                                if (MinecraftClient.getInstance().worldRenderer != null) {
                                                    MinecraftClient.getInstance().worldRenderer.reload();
                                                }
                                                this.dontRenderBehindBorder = value;
                                            }
                                    )
                            )
                            .build()
                )
                .save(this::write)
                .build()
                .generateScreen(parent);
        return screen;
    }


    private static Option<Boolean> booleanOption(String optionName, boolean defaultValue, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        return Option.<Boolean>createBuilder()
                .name(option(optionName))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaultValue, getter, setter)
                .build();
    }


    private static Option<Float> floatSlideOption(String optionName, float defaultValue, float min, float max, Supplier<Float> getter, Consumer<Float> setter) {
        return Option.<Float>createBuilder()
                .name(option(optionName))
                .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(min, max).step(0.1f))
                .binding(defaultValue, getter, setter)
                .build();
    }


    private static MutableText option(String key) {
        return yaclText("option", key);
    }

    private static MutableText category(String key) {
        return yaclText("category", key);
    }

    private static MutableText yaclText(String category, String key) {
        return Text.translatable("foggyborder.yacl.%s.%s".formatted(category, key));
    }
}
