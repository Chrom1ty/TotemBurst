package totem.burst.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KillTracker {
    private static final long ATTACK_WINDOW_MS = 5000;

    public static long COMBO_CHAIN_WINDOW_MS = 3000;

    private static final Map<Integer, Long> recentAttacks = new ConcurrentHashMap<>();

    private static long lastKillTime = 0;

    public static void onAttack(int entityId) {
        pruneStale();
        recentAttacks.put(entityId, System.currentTimeMillis());
    }

    public static void onEntityDeath(int entityId) {
        tryTrigger(entityId);
    }

    public static void onTotemPopped(int entityId) {
        tryTrigger(entityId);
    }

    private static void tryTrigger(int entityId) {
        Long attackedAt = recentAttacks.remove(entityId);
        if (attackedAt == null) return;

        long now = System.currentTimeMillis();
        long elapsed = now - attackedAt;
        if (elapsed > ATTACK_WINDOW_MS) return;

        boolean isComboContinuation = (now - lastKillTime) <= COMBO_CHAIN_WINDOW_MS;

        if (isComboContinuation) {
            ComboEffect.trigger();
            RadialBlurEffect.trigger();
        } else {
            ComboEffect.comboCount = 0;
            RadialBlurEffect.trigger();
        }

        lastKillTime = now;
        playKillSound();
    }

    private static void playKillSound() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        mc.getSoundManager().play(
                SimpleSoundInstance.forUI(TotemBurstSounds.KILL_BLAST, 1.0f, 1.0f)
        );
    }

    private static void pruneStale() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, Long>> it = recentAttacks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Long> entry = it.next();
            if (now - entry.getValue() > ATTACK_WINDOW_MS) {
                it.remove();
            }
        }
    }
}