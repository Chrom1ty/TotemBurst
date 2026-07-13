package totem.burst.client;

public class ChromaticAberrationEffect {
    public static boolean isActive = false;
    public static float intensity = 0f;
    private static long startTime = 0;
    public static float DURATION = 3000f;

    public static void trigger() {
        isActive = true;
        intensity = 1.0f;
        startTime = System.currentTimeMillis();
    }

    public static void tick() {
        if (!isActive) return;
        float elapsed = System.currentTimeMillis() - startTime;
        intensity = Math.max(0f, 1f - (elapsed / DURATION));
        if (intensity <= 0.005f) {
            isActive = false;
            intensity = 0f;
        }
    }
}