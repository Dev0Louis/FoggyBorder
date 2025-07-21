package dev.louis.foggyborder.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.louis.foggyborder.client.FoggyBorder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.RenderedChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.PalettedContainer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(RenderedChunk.class)
public class RenderedChunkMixin {
    @WrapMethod(
            method = "getBlockEntity"
    )
    public BlockEntity dontHaveBlockEntitiesOutsideOfBorder(BlockPos pos, Operation<BlockEntity> original) {
        if (FoggyBorder.config.dontRenderBehindBorder && !MinecraftClient.getInstance().player.getWorld().getWorldBorder().contains(pos)) {
            return null;
        }
        return original.call(pos);
    }

    @ModifyExpressionValue(
            method = "getBlockState",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/chunk/RenderedChunk;blockPalette:Lnet/minecraft/world/chunk/PalettedContainer;")
    )
    public @Nullable PalettedContainer<BlockState> dontHaveBlockEntitiesOutsideOfBorder(@Nullable PalettedContainer<BlockState> original, @Local(argsOnly = true) BlockPos pos) {
        if (FoggyBorder.config.dontRenderBehindBorder && !MinecraftClient.getInstance().player.getWorld().getWorldBorder().contains(pos.getX(), pos.getZ(), 4)) {
            return null;
        }
        return original;
    }
}
