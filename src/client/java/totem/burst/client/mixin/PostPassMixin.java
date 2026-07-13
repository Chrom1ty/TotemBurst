package totem.burst.client.mixin;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import totem.burst.client.ChromaticAberrationEffect;
import totem.burst.client.RadialBlurEffect;
import totem.burst.client.TotemBurstConfig;

import java.util.Map;

@Mixin(PostPass.class)
public class PostPassMixin {

    @Shadow
    private Map<String, GpuBuffer> customUniforms;

    @Shadow
    private String name;

    @Unique
    private GpuBuffer intensityBuffer = null;

    @Inject(at = @At("HEAD"), method = "addToFrame")
    private void onAddToFrame(FrameGraphBuilder frameGraphBuilder, Map<ResourceLocation, ResourceHandle<RenderTarget>> map, GpuBufferSlice gpuBufferSlice, CallbackInfo ci) {
        if (!this.name.contains("totem-burst")) return;
        if (!this.customUniforms.containsKey("IntensityConfig")) return;

        float value;
        if (this.name.contains("radial_blur")) {
            value = RadialBlurEffect.intensity * (TotemBurstConfig.radialBlurStrength / 0.07f);
        } else {
            value = ChromaticAberrationEffect.intensity * (TotemBurstConfig.effectStrength / 0.03f);
        }

        if (intensityBuffer == null) {
            intensityBuffer = RenderSystem.getDevice().createBuffer(
                    () -> "totem-burst intensity",
                    128 | 8,
                    16
            );
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            var data = Std140Builder.onStack(stack, 16)
                    .putFloat(value)
                    .get();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(intensityBuffer.slice(), data);
        }

        this.customUniforms.put("IntensityConfig", intensityBuffer);
    }
}
