package totem.burst.client.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import totem.burst.client.ChromaticAberrationEffect;
import totem.burst.client.KillTracker;

@Mixin(ClientPacketListener.class)
public class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "handleEntityEvent")
    private void onEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        if (packet.getEventId() != 35) return;

        Entity entity = packet.getEntity(Minecraft.getInstance().level);
        if (entity == null) return;

        if (entity == Minecraft.getInstance().player) {
            ChromaticAberrationEffect.trigger();
        } else if (entity instanceof Player) {
            KillTracker.onTotemPopped(entity.getId());
        }
    }
}