package totem.burst.client;

public class ComboEffect {
    public static int comboCount = 0;
    public static boolean isActive = false;
    public static float intensity = 0f;
    private static long lastTriggerTime = 0;

    public static float DURATION = 1200f;
    public static float PER_COMBO_STEP = 0.25f;
    public static float MAX_INTENSITY = 1.0f;

    public static void trigger() {
        comboCount++;
        isActive = true;
        intensity = Math.min(MAX_INTENSITY, comboCount * PER_COMBO_STEP);
        lastTriggerTime = System.currentTimeMillis();
    }

    public static void tick() {
        if (!isActive) return;

        float elapsed = System.currentTimeMillis() - lastTriggerTime;
        float t = Math.min(1f, elapsed / DURATION);
        float base = Math.min(MAX_INTENSITY, comboCount * PER_COMBO_STEP);
        float easedOut = 1f - t;
        intensity = base * easedOut * easedOut * easedOut;

        if (intensity <= 0.005f) {
            isActive = false;
            intensity = 0f;
            comboCount = 0;
        }
    }
}