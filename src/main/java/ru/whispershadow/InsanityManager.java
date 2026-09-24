package ru.whispershadow;

import net.minecraft.client.MinecraftClient;

/**
 * Five-stage local insanity state.
 * It rises after horror events and slowly decays during calm periods.
 */
public final class InsanityManager {
    public enum Level {
        CALM, UNEASY, WATCHED, HUNTED, PANIC, CONTACT
    }

    private static float value = 0.0f;
    private static int calmTicks = 0;

    private InsanityManager() {}

    public static void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            value = 0;
            calmTicks = 0;
            return;
        }

        calmTicks++;

        // Slow natural recovery after about 30 seconds without a major event.
        if (calmTicks >= 600 && value > 0) {
            value = Math.max(0, value - 0.0025f);
        }
    }

    public static void add(float amount) {
        value = Math.min(100.0f, value + amount);
        calmTicks = 0;
    }

    public static float getValue() {
        return value;
    }

    public static Level getLevel() {
        if (value >= 90) return Level.CONTACT;
        if (value >= 70) return Level.PANIC;
        if (value >= 50) return Level.HUNTED;
        if (value >= 30) return Level.WATCHED;
        if (value >= 10) return Level.UNEASY;
        return Level.CALM;
    }

    public static int getLevelNumber() {
        return getLevel().ordinal();
    }
}
