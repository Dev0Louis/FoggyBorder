package dev.louis.foggyborder.client.mixin.compatibility.foglooksgoodnow;

import com.bawnorton.mixinsquared.TargetHandler;
import dev.louis.foggyborder.client.FoggyBorder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * This is a mixin for old versions of Fog Looks good now.
 * It is not be needed when my pr is merged which will hopefully be >2.2.0.
 */
@Mixin(value = BackgroundRenderer.class, priority = 2000)
public class FogLooksGoodNowCompatibilityMixin {


    @TargetHandler(
            mixin = "com.mineblock11.foglooksgoodnow.mixin.FogRendererMixin",
            name = "fogRenderEvent"
    )
    @ModifyArg(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V")
    )
    private static float modifyFogStart(float shaderFogStart) {
        var player = MinecraftClient.getInstance().player;
        var distance = MinecraftClient.getInstance().world.getWorldBorder().getDistanceInsideBorder(player.getX(), player.getZ());
        return (float) Math.min(distance * FoggyBorder.config.fogStartDistanceMultiplier, shaderFogStart);
    }


    @TargetHandler(
            mixin = "com.mineblock11.foglooksgoodnow.mixin.FogRendererMixin",
            name = "fogRenderEvent"
    )
    @ModifyArg(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V")
    )
    private static float modifyFogEnd(float shaderFogStart) {
        var player = MinecraftClient.getInstance().player;
        var distance = MinecraftClient.getInstance().world.getWorldBorder().getDistanceInsideBorder(player.getX(), player.getZ());
        return (float) Math.min(distance * FoggyBorder.config.fogStartDistanceMultiplier, shaderFogStart);
    }


}
