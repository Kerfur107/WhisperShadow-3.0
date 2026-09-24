package ru.whispershadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Random;

public final class SignIllusionEvent {

    private static final Random RANDOM = new Random();

    private static boolean active = false;
    private static int ticks = 0;
    private static int maxTicks = 0;

    private static String line1 = "";
    private static String line2 = "";
    private static String line3 = "";

    private static float fade = 0.0f;

    private SignIllusionEvent() {
    }

    public static boolean isActive() {
        return active;
    }

    /**
     * Запускает случайную иллюзорную табличку.
     *
     * ВАЖНО:
     * Это только клиентский рендер.
     * Никаких блоков в мире не создаётся.
     * Другие игроки табличку не увидят.
     */
    public static void start(MinecraftClient client) {
        if (client == null || client.player == null || client.world == null) {
            return;
        }

        if (active) {
            return;
        }

        active = true;

        maxTicks = 80 + RANDOM.nextInt(100);
        ticks = maxTicks;

        chooseMessage(client);

        fade = 0.0f;

        client.player.playSound(
                ModSounds.STATIC,
                0.12f,
                0.45f + RANDOM.nextFloat() * 0.15f
        );

        InsanityManager.add(
                0.5f + RANDOM.nextFloat() * 1.5f
        );
    }

    /**
     * Запускает конкретную надпись.
     */
    public static void startMessage(
            MinecraftClient client,
            String first,
            String second,
            String third
    ) {
        if (client == null || client.player == null || client.world == null) {
            return;
        }

        if (active) {
            return;
        }

        active = true;

        maxTicks = 100;
        ticks = maxTicks;

        line1 = first == null ? "" : first;
        line2 = second == null ? "" : second;
        line3 = third == null ? "" : third;

        fade = 0.0f;

        client.player.playSound(
                ModSounds.STATIC,
                0.10f,
                0.50f
        );

        InsanityManager.add(1.0f);
    }

    private static void chooseMessage(MinecraftClient client) {
        int level = InsanityManager.getLevelNumber();

        /*
         * Более мягкие сообщения на низком уровне.
         */
        if (level <= 1) {

            switch (RANDOM.nextInt(6)) {
                case 0 -> {
                    line1 = "HELLO";
                    line2 = "";
                    line3 = "";
                }

                case 1 -> {
                    line1 = "ARE YOU";
                    line2 = "LOST?";
                    line3 = "";
                }

                case 2 -> {
                    line1 = "KEEP";
                    line2 = "WALKING";
                    line3 = "";
                }

                case 3 -> {
                    line1 = "DON'T";
                    line2 = "WORRY";
                    line3 = "";
                }

                case 4 -> {
                    line1 = "LOOK";
                    line2 = "AROUND";
                    line3 = "";
                }

                default -> {
                    line1 = "WELCOME";
                    line2 = "";
                    line3 = "";
                }
            }

            return;
        }

        /*
         * Средний уровень.
         */
        if (level == 2) {

            switch (RANDOM.nextInt(8)) {
                case 0 -> {
                    line1 = "DON'T";
                    line2 = "TURN";
                    line3 = "AROUND";
                }

                case 1 -> {
                    line1 = "WE";
                    line2 = "SAW";
                    line3 = "YOU";
                }

                case 2 -> {
                    line1 = "KEEP";
                    line2 = "MOVING";
                    line3 = "";
                }

                case 3 -> {
                    line1 = "YOU WERE";
                    line2 = "HERE";
                    line3 = "BEFORE";
                }

                case 4 -> {
                    line1 = "CAN YOU";
                    line2 = "HEAR";
                    line3 = "US?";
                }

                case 5 -> {
                    line1 = "DON'T";
                    line2 = "LOOK";
                    line3 = "BACK";
                }

                case 6 -> {
                    line1 = "WE ARE";
                    line2 = "CLOSER";
                    line3 = "";
                }

                default -> {
                    line1 = "WELCOME";
                    line2 = "BACK";
                    line3 = "";
                }
            }

            return;
        }

        /*
         * Высокий уровень Insanity.
         */
        switch (RANDOM.nextInt(10)) {
            case 0 -> {
                line1 = "WE FOUND";
                line2 = "YOU";
                line3 = "";
            }

            case 1 -> {
                line1 = "DON'T";
                line2 = "MOVE";
                line3 = "";
            }

            case 2 -> {
                line1 = "WE ARE";
                line2 = "BEHIND";
                line3 = "YOU";
            }

            case 3 -> {
                line1 = "THIS IS";
                line2 = "NOT";
                line3 = "YOUR WORLD";
            }

            case 4 -> {
                line1 = "YOU";
                line2 = "SHOULDN'T";
                line3 = "BE HERE";
            }

            case 5 -> {
                line1 = "WE KNOW";
                line2 = "YOU CAN";
                line3 = "SEE THIS";
            }

            case 6 -> {
                line1 = "TOO";
                line2 = "LATE";
                line3 = "";
            }

            case 7 -> {
                line1 = "TURN";
                line2 = "AROUND";
                line3 = "NOW";
            }

            case 8 -> {
                line1 = "RUN";
                line2 = "";
                line3 = "";
            }

            default -> {
                line1 = "FIND";
                line2 = "US";
                line3 = "";
            }
        }
    }

    public static void tick(MinecraftClient client) {
        if (!active) {
            return;
        }

        if (client == null || client.player == null || client.world == null) {
            stop();
            return;
        }

        ticks--;

        /*
         * Плавное появление.
         */
        if (ticks > maxTicks - 12) {
            fade += 1.0f / 12.0f;

            if (fade > 1.0f) {
                fade = 1.0f;
            }
        }

        /*
         * Плавное исчезновение.
         */
        if (ticks < 12) {
            fade -= 1.0f / 12.0f;

            if (fade < 0.0f) {
                fade = 0.0f;
            }
        }

        if (ticks <= 0) {
            stop();
        }
    }

    /**
     * Рисует иллюзорную табличку.
     *
     * Она существует только на клиенте.
     */
    public static void renderOverlay(
            DrawContext context,
            RenderTickCounter tickCounter
    ) {
        if (!active) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null || client.textRenderer == null) {
            return;
        }

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        /*
         * Небольшое случайное смещение,
         * чтобы табличка не выглядела абсолютно статичной.
         */
        float pulse = (float) Math.sin(
                (System.currentTimeMillis() % 1800L) / 1800.0 * Math.PI * 2.0
        );

        int wobbleX = Math.round(pulse * 1.5f);
        int wobbleY = Math.round(pulse * 0.5f);

        int centerX = width / 2 + wobbleX;
        int centerY = height / 2 + wobbleY;

        int signWidth = 230;
        int signHeight = 130;

        int left = centerX - signWidth / 2;
        int top = centerY - signHeight / 2;

        /*
         * Альфа-канал зависит от fade.
         */
        int alpha = (int) (Math.max(0.0f, Math.min(1.0f, fade)) * 255.0f);

        /*
         * Деревянная часть таблички.
         */
        int boardColor = (alpha << 24) | 0x6B4726;

        /*
         * Более тёмная рамка.
         */
        int borderColor = (alpha << 24) | 0x2A1A0E;

        /*
         * Тень.
         */
        int shadowAlpha = Math.max(0, alpha / 2);
        int shadowColor = (shadowAlpha << 24);

        context.fill(
                left + 5,
                top + 6,
                left + signWidth + 5,
                top + signHeight + 6,
                shadowColor
        );

        /*
         * Рамка.
         */
        context.fill(
                left,
                top,
                left + signWidth,
                top + signHeight,
                borderColor
        );

        /*
         * Доска.
         */
        context.fill(
                left + 4,
                top + 4,
                left + signWidth - 4,
                top + signHeight - 4,
                boardColor
        );

        /*
         * Небольшие полосы, имитирующие деревянную текстуру.
         */
        int lineColor = (alpha << 24) | 0x52351E;

        context.fill(
                left + 8,
                top + 25,
                left + signWidth - 8,
                top + 27,
                lineColor
        );

        context.fill(
                left + 8,
                top + 62,
                left + signWidth - 8,
                top + 64,
                lineColor
        );

        context.fill(
                left + 8,
                top + 99,
                left + signWidth - 8,
                top + 101,
                lineColor
        );

        drawCenteredText(
                context,
                client,
                line1,
                centerX,
                centerY - 40,
                alpha
        );

        drawCenteredText(
                context,
                client,
                line2,
                centerX,
                centerY - 12,
                alpha
        );

        drawCenteredText(
                context,
                client,
                line3,
                centerX,
                centerY + 16,
                alpha
        );
    }

    private static void drawCenteredText(
            DrawContext context,
            MinecraftClient client,
            String text,
            int centerX,
            int y,
            int alpha
    ) {
        if (text == null || text.isEmpty()) {
            return;
        }

        int textWidth = client.textRenderer.getWidth(text);

        int textColor =
                (alpha << 24)
                        | 0xE8E8E8;

        /*
         * Лёгкая красная тень делает текст более неприятным.
         */
        int shadowColor =
                (alpha << 24)
                        | 0x180000;

        context.drawText(
                client.textRenderer,
                Text.literal(text).formatted(Formatting.BOLD),
                centerX - textWidth / 2 + 1,
                y + 1,
                shadowColor,
                false
        );

        context.drawText(
                client.textRenderer,
                Text.literal(text).formatted(Formatting.BOLD),
                centerX - textWidth / 2,
                y,
                textColor,
                false
        );
    }

    public static void stop() {
        active = false;
        ticks = 0;
        maxTicks = 0;
        fade = 0.0f;

        line1 = "";
        line2 = "";
        line3 = "";
    }
}
