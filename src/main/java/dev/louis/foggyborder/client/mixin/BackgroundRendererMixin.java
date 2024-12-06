package dev.louis.foggyborder.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.louis.foggyborder.client.FoggyBorder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(
            method = "applyFog",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/FogShape;CYLINDER:Lnet/minecraft/client/render/FogShape;", ordinal = 2)
    )
    private static void modifyFogStartAndEnd(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir, @Local BackgroundRenderer.FogData fogData) {
        var worldBorder = MinecraftClient.getInstance().world.getWorldBorder();
        var distance = worldBorder.getDistanceInsideBorder(camera.getPos().x, camera.getPos().z);
        //FogStart
        fogData.fogStart = (float) Math.min(Math.max(distance * FoggyBorder.config.fogStartDistanceMultiplier, FoggyBorder.config.minimumFogStartDistance), fogData.fogStart);
        //FogEnd
        fogData.fogEnd = (float) Math.min(Math.max(distance * FoggyBorder.config.fogEndDistanceMultiplier, FoggyBorder.config.minimumFogEndDistance), fogData.fogEnd);

        double blockViewDistance = MinecraftClient.getInstance().options.getClampedViewDistance() * 16;

        if (distance < 32) {
            //float red = 1 - (float) (distance / 32f);
            float mult = Math.min((float) (distance + 2 / 16f), 1);
            //color.x = red;
            color.y = color.y * mult;
            color.z = color.z * mult;
        }
    }


}
