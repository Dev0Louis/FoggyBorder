package dev.louis.foggyborder.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.louis.foggyborder.client.FoggyBorder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
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
    private static float modifyFogStart(float shaderFogStart, @Local(argsOnly = true) Camera camera) {
        var player = MinecraftClient.getInstance().player;
        var distance = MinecraftClient.getInstance().world.getWorldBorder().getDistanceInsideBorder(camera.getPos().x, camera.getPos().z);
        return (float) Math.min(Math.max(distance * FoggyBorder.config.fogStartDistanceMultiplier, FoggyBorder.config.minimumFogStartDistance), shaderFogStart);
    }

    @ModifyArg(
            method = "applyFog",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V",  remap = false)
    )
    private static float modifyFogEnd(float shaderFogEnd, @Local(argsOnly = true) Camera camera) {
        var distance = MinecraftClient.getInstance().world.getWorldBorder().getDistanceInsideBorder(camera.getPos().x, camera.getPos().z);
        return (float) Math.min(Math.max(distance * FoggyBorder.config.fogEndDistanceMultiplier, FoggyBorder.config.minimumFogEndDistance), shaderFogEnd);
    }
}
