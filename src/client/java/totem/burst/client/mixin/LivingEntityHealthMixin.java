package totem.burst.client.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import totem.burst.client.KillTracker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(LivingEntity.class)
public class LivingEntityHealthMixin {

    @Unique
    private static final Map<Integer, Float> totemBurst$lastKnownHealth = new ConcurrentHashMap<>();

    @Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
    private void totemBurst$onSyncedDataUpdated(EntityDataAccessor<?> key, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self instanceof Player)) return;
        if (self.level() == null || !self.level().isClientSide) return;

        int id = self.getId();
        float newHealth = self.getHealth();
        Float prev = totemBurst$lastKnownHealth.get(id);
        float prevHealth = (prev == null) ? newHealth : prev;

        if (prevHealth > 0f && newHealth <= 0f) {
            KillTracker.onEntityDeath(id);
        }

        totemBurst$lastKnownHealth.put(id, newHealth);
    }
}