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
import totem.burst.client.ComboEffect;
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

        float value = resolveIntensity(this.name);

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

    @Unique
    private float resolveIntensity(String fullName) {
        int lastSlash = fullName.lastIndexOf('/');
        String effectId = lastSlash >= 0 ? fullName.substring(0, lastSlash) : fullName;
        int passIndex = -1;
        if (lastSlash >= 0) {
            try {
                passIndex = Integer.parseInt(fullName.substring(lastSlash + 1));
            } catch (NumberFormatException ignored) {}
        }

        if (effectId.endsWith("combo_stack")) {
            if (passIndex <= 1) {
                return RadialBlurEffect.intensity * (TotemBurstConfig.radialBlurStrength / 0.07f);
            } else {
                return ComboEffect.intensity * (TotemBurstConfig.comboContrastStrength / 1.0f);
            }
        } else if (effectId.endsWith("radial_blur")) {
            return RadialBlurEffect.intensity * (TotemBurstConfig.radialBlurStrength / 0.07f);
        } else {
            return ChromaticAberrationEffect.intensity * (TotemBurstConfig.effectStrength / 0.03f);
        }
    }
}