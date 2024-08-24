package dev.louis.foggyborder.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.louis.foggyborder.client.FoggyBorder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @ModifyArg(
            method = "applyFog",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", remap = false)
    )
    private static float modifyFogStart(float shaderFogStart, @Local(argsOnly = true) Camera camera) {
        var worldBorder = MinecraftClient.getInstance().world.getWorldBorder();
        var distance = worldBorder.getDistanceInsideBorder(camera.getPos().x, camera.getPos().z);
        return (float) Math.min(Math.max(distance * FoggyBorder.config.fogStartDistanceMultiplier, FoggyBorder.config.minimumFogStartDistance), shaderFogStart);
    }

    @ModifyArg(
            method = "applyFog",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V",  remap = false)
    )
    private static float modifyFogEnd(float shaderFogEnd, @Local(argsOnly = true) Camera camera) {
        var worldBorder = MinecraftClient.getInstance().world.getWorldBorder();
        var distance = worldBorder.getDistanceInsideBorder(camera.getPos().x, camera.getPos().z);

        return (float) Math.min(Math.max(distance * FoggyBorder.config.fogEndDistanceMultiplier, FoggyBorder.config.minimumFogEndDistance), shaderFogEnd);
    }

    @ModifyArgs(
            method = "applyFogColor",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogColor(FFF)V")
    )
    private static void makeFogWorldborderColor(Args args) {
        var client = MinecraftClient.getInstance();
        var worldBorder = client.world.getWorldBorder();
        var camera = client.gameRenderer.getCamera();
        double blockViewDistance = client.options.getClampedViewDistance() * 16;

        if (!(camera.getPos().x < worldBorder.getBoundEast() - blockViewDistance) || !(camera.getPos().x > worldBorder.getBoundWest() + blockViewDistance)
                || !(camera.getPos().z < worldBorder.getBoundSouth() - blockViewDistance) || !(camera.getPos().z > worldBorder.getBoundNorth() + blockViewDistance)
        ) {
            int color = worldBorder.getStage().getColor();
            if (color == 2138367) return;
            float red = (float)(color >> 16 & 255) / 255.0F;
            float green = (float)(color >> 8 & 255) / 255.0F;
            float blue = (float)(color & 255) / 255.0F;
            args.setAll(red, green, blue);
        }
    }
}
