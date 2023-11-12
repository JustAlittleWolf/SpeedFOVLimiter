package me.wolfii.speedfovlimiter;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.Boolean;
import dev.isxander.yacl3.config.v2.api.autogen.CustomDescription;
import dev.isxander.yacl3.config.v2.api.autogen.FloatSlider;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

public class Config {
    private static final ConfigClassHandler<Config> configInstance = ConfigClassHandler.createBuilder(Config.class)
            .id(new Identifier("speedfovlimiter"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("speedfovlimiter.json"))
                    .build())
            .build();

    static {
        configInstance.load();
    }

    public static Config getConfig() {
        return Config.configInstance.instance();
    }

    public static Screen createScreen(Screen parent) {
        return Config.configInstance.generateGui().generateScreen(parent);
    }

    @AutoGen(category = "settings")
    @Boolean(formatter = Boolean.Formatter.ON_OFF)
    @CustomDescription("Toggles the mod.")
    @SerialEntry
    public boolean modEnabled = true;

    @AutoGen(category = "settings")
    @FloatSlider(min = 0.0f, max = 1.0f, step = 0.01F, format = "%.2f")
    @CustomDescription("Caps how narrow your view gets when having the slowness effect.")
    @SerialEntry
    public float lowestFovMultiplier = 0.75f;

    @AutoGen(category = "settings")
    @FloatSlider(min = 1.0f, max = 5.0f, step = 0.01F, format = "%.2f")
    @CustomDescription("Caps how wide your view gets when having the speed effect.")
    @SerialEntry
    public float highestFovMultiplier = 1.25f;
}
