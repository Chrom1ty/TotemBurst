package totem.burst.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KillTracker {
    private static final long ATTACK_WINDOW_MS = 5000;

    private static final Map<Integer, Long> recentAttacks = new ConcurrentHashMap<>();

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

        long elapsed = System.currentTimeMillis() - attackedAt;
        if (elapsed <= ATTACK_WINDOW_MS) {
            RadialBlurEffect.trigger();
            playKillSound();
        }
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