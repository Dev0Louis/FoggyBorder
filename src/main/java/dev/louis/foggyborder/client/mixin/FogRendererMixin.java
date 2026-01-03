package dev.louis.foggyborder.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.louis.foggyborder.client.FoggyBorder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(value = FogRenderer.class)
public class FogRendererMixin {
    @Inject(
            method = "applyFog(Lnet/minecraft/client/render/Camera;ILnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/fog/FogData;renderDistanceEnd:F", ordinal = 0)
    )
    private static void modifyFogStartAndEnd(Camera camera, int viewDistance, RenderTickCounter renderTickCounter, float f, ClientWorld clientWorld, CallbackInfoReturnable<Vector4f> cir, @Local FogData fogData, @Local Vector4f color) {
        var worldBorder = MinecraftClient.getInstance().world.getWorldBorder();
        var distance = worldBorder.getDistanceInsideBorder(camera.getCameraPos().x, camera.getCameraPos().z);

        fogData.environmentalEnd =
                (float) Math.min(Math.max(distance * FoggyBorder.config.fogEndDistanceMultiplier, FoggyBorder.config.minimumFogEndDistance), fogData.environmentalEnd);
        //fogData.renderDistanceEnd =
        //        (float) Math.min(Math.max(distance * FoggyBorder.config.fogEndDistanceMultiplier, FoggyBorder.config.minimumFogEndDistance), fogData.renderDistanceEnd);
        //fogData.skyEnd = (float) Math.min(Math.max(distance * FoggyBorder.config.fogEndDistanceMultiplier, FoggyBorder.config.minimumFogEndDistance), fogData.skyEnd);
        //fogData.cloudEnd = (float) Math.min(Math.max(distance * FoggyBorder.config.fogEndDistanceMultiplier, FoggyBorder.config.minimumFogEndDistance), fogData.cloudEnd);
        //fogData.environmentalEnd = (float) Math.min(Math.max(distance * FoggyBorder.config.fogEndDistanceMultiplier, FoggyBorder.config.minimumFogEndDistance), fogData.environmentalEnd);

        if (distance < 32) {
            //float red = 1 - (float) (distance / 32f);
            float mult = Math.min((float) (distance + 2 / 16f), 1);
            //color.x = red;
            //x|red y|green z|blue
            color.y = color.y * mult;
            color.z = color.z * mult;

            //color.w = Math.max(color.w, 0.75f * mult);
        }
    }


}
