package ru.whispershadow;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public final class SignIllusionEvent {

    private static final Random RANDOM = new Random();

    private static boolean active = false;

    private static int ticks = 0;
    private static int maxTicks = 0;

    private static int entityId = -1;

    private static DisplayEntity.BlockDisplayEntity signEntity;

    private static String line1 = "";
    private static String line2 = "";
    private static String line3 = "";

    private static float fade = 0.0f;

    private SignIllusionEvent() {
    }

    public static boolean isActive() {
        return active;
    }

    public static void start(MinecraftClient client) {
        if (client == null ||
                client.player == null ||
                client.world == null) {
            return;
        }

        if (active) {
            return;
        }

        active = true;

        maxTicks = 120 + RANDOM.nextInt(100);
        ticks = maxTicks;

        chooseMessage();

        fade = 0.0f;

        spawnSign(client);

        client.player.playSound(
                ModSounds.STATIC,
                0.10f,
                0.45f + RANDOM.nextFloat() * 0.15f
        );

        InsanityManager.add(
                0.5f + RANDOM.nextFloat() * 1.5f
        );
    }

    public static void startMessage(
            MinecraftClient client,
            String first,
            String second,
            String third
    ) {
        if (client == null ||
                client.player == null ||
                client.world == null) {
            return;
        }

        if (active) {
            return;
        }

        active = true;

        maxTicks = 120;
        ticks = maxTicks;

        line1 = first == null ? "" : first;
        line2 = second == null ? "" : second;
        line3 = third == null ? "" : third;

        fade = 0.0f;

        spawnSign(client);

        client.player.playSound(
                ModSounds.STATIC,
                0.10f,
                0.50f
        );

        InsanityManager.add(1.0f);
    }

    private static void chooseMessage() {
        int level = InsanityManager.getLevelNumber();

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

    private static void spawnSign(MinecraftClient client) {

        if (client.world == null ||
                client.player == null) {
            return;
        }

        ClientWorld world = client.world;

        Vec3d look = client.player.getRotationVec(1.0f);

        /*
         * Табличка появляется примерно в 5 блоках
         * перед игроком.
         */
        Vec3d position = client.player.getEyePos()
                .add(look.multiply(5.0));

        /*
         * Ставим её немного ниже уровня глаз.
         */
        position = position.add(
                0.0,
                -1.0,
                0.0
        );

        /*
         * Создаём клиентскую BlockDisplayEntity.
         *
         * Это НЕ настоящий блок мира.
         * Сервер о нём ничего не знает.
         */
        signEntity =
                new DisplayEntity.BlockDisplayEntity(
                        net.minecraft.entity.EntityType.BLOCK_DISPLAY,
                        world
                );

        signEntity.setPosition(
                position.x,
                position.y,
                position.z
        );

        /*
         * Используем модель обычной дубовой таблички.
         */
        signEntity.setBlockState(
                Blocks.OAK_SIGN.getDefaultState()
        );

        /*
         * Поворачиваем табличку к игроку.
         */
        float yaw =
                client.player.getYaw() + 180.0f;

        signEntity.setYaw(yaw);
        signEntity.setBodyYaw(yaw);

        /*
         * Уникальный клиентский ID.
         */
        entityId =
                200000 +
                        RANDOM.nextInt(50000);

        world.addEntity(signEntity);
    }

    public static void tick(MinecraftClient client) {

        if (!active) {
            return;
        }

        if (client == null ||
                client.player == null ||
                client.world == null) {

            stop();
            return;
        }

        ticks--;

        /*
         * Плавное появление.
         */
        if (ticks > maxTicks - 15) {

            fade += 1.0f / 15.0f;

            if (fade > 1.0f) {
                fade = 1.0f;
            }
        }

        /*
         * Плавное исчезновение.
         */
        if (ticks < 15) {

            fade -= 1.0f / 15.0f;

            if (fade < 0.0f) {
                fade = 0.0f;
            }
        }

        /*
         * Если игрок слишком далеко —
         * иллюзия исчезает.
         */
        if (signEntity != null &&
                signEntity.isRemoved()) {

            stop();
            return;
        }

        if (ticks <= 0) {
            stop();
        }
    }

    /*
     * Пока оставляем этот метод пустым,
     * чтобы старый HorrorManager продолжал
     * нормально вызывать renderOverlay().
     *
     * Сама табличка теперь находится В МИРЕ,
     * поэтому здесь ничего рисовать не нужно.
     */
    public static void renderOverlay(
            DrawContext context,
            RenderTickCounter tickCounter
    ) {
    }

    public static void stop() {

        if (signEntity != null) {

            signEntity.remove(
                    Entity.RemovalReason.DISCARDED
            );

            signEntity = null;
        }

        active = false;

        ticks = 0;
        maxTicks = 0;

        entityId = -1;

        fade = 0.0f;

        line1 = "";
        line2 = "";
        line3 = "";
    }
}
