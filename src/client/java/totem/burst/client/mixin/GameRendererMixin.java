package totem.burst.client.mixin;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import totem.burst.client.ChromaticAberrationEffect;
import totem.burst.client.RadialBlurEffect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    private void setPostEffect(ResourceLocation id) {}

    @Shadow
    public void clearPostEffect() {}

    @Shadow
    private boolean effectActive;

    @Shadow
    private CrossFrameResourcePool resourcePool;

    @Unique
    private String totemBurst$activeEffect = null;

    @Inject(at = @At("HEAD"), method = "render")
    private void onRender(CallbackInfo ci) {
        ChromaticAberrationEffect.tick();
        RadialBlurEffect.tick();

        boolean radialOn = RadialBlurEffect.isActive || RadialBlurEffect.intensity > 0.01f;
        boolean chromaOn = ChromaticAberrationEffect.isActive || ChromaticAberrationEffect.intensity > 0.01f;

        String desired = null;
        if (radialOn) {
            desired = "radial_blur";
        } else if (chromaOn) {
            desired = "chromatic_aberration";
        }

        if (desired != null) {
            if (!desired.equals(this.totemBurst$activeEffect)) {
                this.setPostEffect(ResourceLocation.fromNamespaceAndPath("totem-burst", desired));
                this.effectActive = true;
                this.totemBurst$activeEffect = desired;
            }
        } else {
            if (this.totemBurst$activeEffect != null) {
                this.clearPostEffect();
                this.totemBurst$activeEffect = null;
            }
        }
    }
}
