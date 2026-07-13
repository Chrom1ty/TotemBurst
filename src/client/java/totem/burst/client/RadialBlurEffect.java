package totem.burst.client;

public class RadialBlurEffect {
    public static boolean isActive = false;
    public static float intensity = 0f;
    private static long startTime = 0;

    public static float DURATION = 900f;

    public static void trigger() {
        isActive = true;
        intensity = 1.0f;
        startTime = System.currentTimeMillis();
    }

    public static void tick() {
        if (!isActive) return;

        float elapsed = System.currentTimeMillis() - startTime;
        float t = Math.min(1f, elapsed / DURATION);

        float easedOut = 1f - t;
        intensity = easedOut * easedOut * easedOut;

        if (intensity <= 0.005f) {
            isActive = false;
            intensity = 0f;
        }
    }
}
