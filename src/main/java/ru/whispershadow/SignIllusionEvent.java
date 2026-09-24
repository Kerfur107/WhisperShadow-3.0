package ru.whispershadow;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public final class SignIllusionEvent {

    private static final Random RANDOM = new Random();

    private static boolean active = false;

    private static int ticks = 0;
    private static int maxTicks = 0;

    private static int entityId = -1;
    private static int textEntityId = -1;

    private static DisplayEntity.BlockDisplayEntity signEntity;
    private static TextDisplayEntity textEntity;

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

        Vec3d position = client.player.getEyePos()
                .add(look.multiply(5.0));

        position = position.add(
                0.0,
                -1.0,
                0.0
        );

        /*
         * ДЕРЕВЯННАЯ ЧАСТЬ ТАБЛИЧКИ
         */
        signEntity =
                new DisplayEntity.BlockDisplayEntity(
                        EntityType.BLOCK_DISPLAY,
                        world
                );

        signEntity.setPosition(
                position.x,
                position.y,
                position.z
        );

        signEntity.setBlockState(
                Blocks.OAK_SIGN.getDefaultState()
        );

        float yaw =
                client.player.getYaw() + 180.0f;

        signEntity.setYaw(yaw);
        signEntity.setBodyYaw(yaw);

        entityId =
                200000 +
                        RANDOM.nextInt(50000);

        world.addEntity(signEntity);

        /*
         * ТЕКСТ НА ТАБЛИЧКЕ
         */
        textEntity =
                new TextDisplayEntity(
                        EntityType.TEXT_DISPLAY,
                        world
                );

        /*
         * Немного выдвигаем текст вперёд,
         * чтобы он не оказался внутри дерева.
         */
        Vec3d textPosition =
                position.add(
                        0.0,
                        0.05,
                        -0.06
                );

        textEntity.setPosition(
                textPosition.x,
                textPosition.y,
                textPosition.z
        );

        String fullText = buildText();

        textEntity.setText(
                Text.literal(fullText)
                        .formatted(Formatting.BOLD)
        );

        /*
         * Текст смотрит на игрока.
         */
        textEntity.setBillboardMode(
                DisplayEntity.BillboardMode.CENTER
        );

        /*
         * Размер текста.
         */
        
        textEntityId =
                250000 +
                        RANDOM.nextInt(50000);

        world.addEntity(textEntity);
    }

    private static String buildText() {

        StringBuilder text =
                new StringBuilder();

        if (!line1.isEmpty()) {
            text.append(line1);
        }

        if (!line2.isEmpty()) {

            if (text.length() > 0) {
                text.append("\n");
            }

            text.append(line2);
        }

        if (!line3.isEmpty()) {

            if (text.length() > 0) {
                text.append("\n");
            }

            text.append(line3);
        }

        return text.toString();
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
         * Проверяем деревянную часть.
         */
        if (signEntity != null &&
                signEntity.isRemoved()) {

            stop();
            return;
        }

        /*
         * Проверяем текст.
         */
        if (textEntity != null &&
                textEntity.isRemoved()) {

            stop();
            return;
        }

        if (ticks <= 0) {
            stop();
        }
    }

    /*
     * Табличка теперь полностью находится
     * в игровом мире.
     *
     * HUD здесь больше ничего не рисует.
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

        if (textEntity != null) {

            textEntity.remove(
                    Entity.RemovalReason.DISCARDED
            );

            textEntity = null;
        }

        active = false;

        ticks = 0;
        maxTicks = 0;

        entityId = -1;
        textEntityId = -1;

        fade = 0.0f;

        line1 = "";
        line2 = "";
        line3 = "";
    }
}
