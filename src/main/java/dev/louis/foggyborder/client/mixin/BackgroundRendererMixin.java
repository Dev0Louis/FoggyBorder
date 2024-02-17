package dev.louis.foggyborder.client.mixin;

import dev.louis.foggyborder.client.FoggyBorder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Debug(export = true)
@Mixin(value = BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @ModifyArg(
            method = "applyFog",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", remap = false)
    )
    private static float modifyFogStart(float shaderFogStart) {
        var player = MinecraftClient.getInstance().player;
        var distance = MinecraftClient.getInstance().world.getWorldBorder().getDistanceInsideBorder(player.getX(), player.getZ());
        return (float) Math.min(Math.max(distance * FoggyBorder.config.fogStartDistanceMultiplier, FoggyBorder.config.minimumFogStartDistance), shaderFogStart);
    }

    @ModifyArg(
            method = "applyFog",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V",  remap = false)
    )
    private static float modifyFogEnd(float shaderFogEnd) {
        var player = MinecraftClient.getInstance().player;
        var distance = MinecraftClient.getInstance().world.getWorldBorder().getDistanceInsideBorder(player.getX(), player.getZ());
        return (float) Math.min(Math.max(distance * FoggyBorder.config.fogEndDistanceMultiplier, FoggyBorder.config.minimumFogEndDistance), shaderFogEnd);
    }
}
