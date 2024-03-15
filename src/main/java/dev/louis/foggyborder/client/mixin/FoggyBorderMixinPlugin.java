package dev.louis.foggyborder.client.mixin;

import dev.louis.foggyborder.client.FoggyBorder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class FoggyBorderMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        var array = mixinClassName.split("\\.");
        if(array[array.length - 3].equals("compatibility")) {
            if(array[array.length - 2].equals("foglooksgoodnow")) {
                var modContainer = FabricLoader.getInstance().getModContainer("fog-looks-good-now");
                if(modContainer.isPresent()) {
                    try {
                        return modContainer.get().getMetadata().getVersion().compareTo(SemanticVersion.parse("2.2.0")) <= 0;
                    } catch (VersionParsingException e) {
                        FoggyBorder.LOGGER.error("Could not parse version.", e);
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
