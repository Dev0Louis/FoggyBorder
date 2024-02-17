package dev.louis.foggyborder.client.mixin;

import dev.louis.foggyborder.client.FoggyBorder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = 400)
public class WorldRendererMixin {
    @Inject(
            method = "renderWorldBorder",
            at = @At("HEAD"),
            cancellable = true
    )
    public void doNotRenderWorldborder(Camera camera, CallbackInfo ci) {
        if(FoggyBorder.config.disableWorldborder) ci.cancel();
    }
}
