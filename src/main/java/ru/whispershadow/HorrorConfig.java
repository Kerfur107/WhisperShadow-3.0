package ru.whispershadow;

public final class HorrorConfig {
    public static final int CHAT_CHANCE = 1100;
    public static final int GLITCH_CHANCE = 850;
    public static final int WHISPER_CHANCE = 1200;
    public static final int FIGURE_CHANCE = 3000;

    public static final int GLITCH_MIN_TICKS = 2;
    public static final int GLITCH_MAX_TICKS = 8;

    public static final int FIGURE_LIFETIME_TICKS = 180;
    public static final double FIGURE_MIN_DISTANCE = 8.0;
    public static final double FIGURE_MAX_DISTANCE = 24.0;

    public static final boolean ENABLE_TOUCH_DISCONNECT = true;
    public static final boolean SHOW_DEBUG_LEVEL = false;

    private HorrorConfig() {}

    public static int scaleChance(int base, int level) {
        // Higher insanity means a lower denominator = more frequent events.
        double multiplier = switch (level) {
            case 0 -> 1.0;
            case 1 -> 0.9;
            case 2 -> 0.72;
            case 3 -> 0.55;
            case 4 -> 0.38;
            default -> 0.25;
        };
        return Math.max(1, (int) Math.round(base * multiplier));
    }
}
