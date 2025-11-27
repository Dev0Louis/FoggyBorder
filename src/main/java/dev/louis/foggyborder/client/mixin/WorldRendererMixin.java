package dev.louis.foggyborder.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.louis.foggyborder.client.FoggyBorder;
import net.minecraft.client.render.WorldBorderRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.state.WorldBorderRenderState;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = WorldRenderer.class, priority = 400)
public class WorldRendererMixin {
    @WrapWithCondition(
            method = "method_62216",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldBorderRendering;render(Lnet/minecraft/client/render/state/WorldBorderRenderState;Lnet/minecraft/util/math/Vec3d;DD)V")

    )
    public boolean doNotRenderWorldborder(WorldBorderRendering instance, WorldBorderRenderState state, Vec3d cameraPos, double viewDistanceBlocks, double farPlaneDistance) {
        return !FoggyBorder.config.disableWorldborder;
    }
}
