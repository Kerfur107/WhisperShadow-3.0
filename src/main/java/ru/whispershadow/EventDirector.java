package ru.whispershadow;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public final class EventDirector {

    private static final Random RANDOM = new Random();

    private static final Deque<ScheduledEvent> queue =
            new ArrayDeque<>();

    private static int cooldown;

    private EventDirector() {}

    public static void tick(MinecraftClient client) {

        if (client.player == null ||
                client.world == null) {

            queue.clear();
            cooldown = 0;

            return;
        }

        if (cooldown > 0)
            cooldown--;

        while (!queue.isEmpty() &&
                queue.peek().delay <= 0) {

            execute(
                    client,
                    queue.poll().type
            );
        }

        for (ScheduledEvent event : queue)
            event.delay--;

        if (cooldown == 0 &&
                RANDOM.nextInt(
                        dynamicBaseChance()
                ) == 0) {

            startRandomEvent(client);

            cooldown =
                    100 +
                            RANDOM.nextInt(520);
        }
    }

    private static int dynamicBaseChance() {

        return Math.max(
                65,
                980 -
                        InsanityManager.getLevelNumber()
                                * 135
        );
    }

    private static void startRandomEvent(
            MinecraftClient client
    ) {

        int level =
                InsanityManager.getLevelNumber();

        int roll =
                RANDOM.nextInt(100);

        if (roll < 18) {

            queue.add(
                    new ScheduledEvent(
                            EventType.LOCAL_MESSAGE,
                            delay(0, 80)
                    )
            );

        } else if (roll < 31) {

            queue.add(
                    new ScheduledEvent(
                            EventType.GLITCH,
                            delay(0, 110)
                    )
            );

        } else if (roll < 43) {

            queue.add(
                    new ScheduledEvent(
                            EventType.WHISPER,
                            delay(20, 180)
                    )
            );

        } else if (roll < 53) {

            queue.add(
                    new ScheduledEvent(
                            EventType.FOOTSTEPS,
                            delay(20, 180)
                    )
            );

        } else if (roll < 63) {

            queue.add(
                    new ScheduledEvent(
                            EventType.TAB_MESSAGE,
                            delay(30, 220)
                    )
            );

        } else if (roll < 71) {

            queue.add(
                    new ScheduledEvent(
                            EventType.EYES,
                            delay(10, 220)
                    )
            );

        } else if (roll < 78) {

            queue.add(
                    new ScheduledEvent(
                            EventType.PERIPHERAL,
                            delay(20, 180)
                    )
            );

        } else if (roll < 84) {

            queue.add(
                    new ScheduledEvent(
                            EventType.TORCHES,
                            delay(30, 260)
                    )
            );

        } else if (roll < 89 &&
                level >= 2) {

            queue.add(
                    new ScheduledEvent(
                            EventType.FIGURE,
                            delay(20, 260)
                    )
            );

        } else if (roll < 93 &&
                level >= 2) {

            queue.add(
                    new ScheduledEvent(
                            EventType.RUN,
                            delay(20, 180)
                    )
            );

        } else if (roll < 96 &&
                level >= 2) {

            queue.add(
                    new ScheduledEvent(
                            EventType.SIGN,
                            delay(40, 260)
                    )
            );

        } else if (level >= 3) {

            queue.add(
                    new ScheduledEvent(
                            EventType.DOPPELGANGER,
                            delay(40, 420)
                    )
            );

        } else {

            queue.add(
                    new ScheduledEvent(
                            EventType.WORLD_DISTORT,
                            delay(20, 180)
                    )
            );
        }

        /*
         * RARE VHS
         */

        if (RANDOM.nextInt(85) == 0) {

            queue.add(
                    new ScheduledEvent(
                            EventType.VHS,
                            delay(30, 500)
                    )
            );
        }

        /*
         * VERY RARE DON'T MOVE
         */

        if (level >= 2 &&
                RANDOM.nextInt(140) == 0) {

            queue.add(
                    new ScheduledEvent(
                            EventType.DONT_MOVE,
                            delay(60, 260)
                    )
            );
        }

        /*
         * EXTREMELY RARE FIND US
         */

        if (level >= 3 &&
                RANDOM.nextInt(220) == 0) {

            queue.add(
                    new ScheduledEvent(
                            EventType.FIND_US,
                            delay(80, 500)
                    )
            );
        }

        /*
         * RARE SIGN
         */

        if (level >= 2 &&
                RANDOM.nextInt(160) == 0) {

            queue.add(
                    new ScheduledEvent(
                            EventType.SIGN,
                            delay(80, 360)
                    )
            );
        }

        /*
         * RARE CHAIN
         */

        if (RANDOM.nextInt(19) == 0) {

            queue.add(
                    new ScheduledEvent(
                            EventType.SILENCE,
                            delay(60, 220)
                    )
            );

            queue.add(
                    new ScheduledEvent(
                            EventType.GLITCH,
                            delay(140, 480)
                    )
            );

            if (level >= 3) {

                queue.add(
                        new ScheduledEvent(
                                EventType.FIGURE,
                                delay(220, 700)
                        )
                );
            }
        }
    }

    private static void execute(
            MinecraftClient client,
            EventType type
    ) {

        switch (type) {

            case LOCAL_MESSAGE ->
                    HorrorManager.fireDirectorMessage(
                            client
                    );

            case GLITCH ->
                    HorrorManager.fireDirectorGlitch();

            case WHISPER ->
                    HorrorManager.fireDirectorWhisper(
                            client
                    );

            case FIGURE ->
                    HorrorManager.fireDirectorFigure(
                            client
                    );

            case DECOY ->
                    HorrorManager.fireDecoy(
                            client
                    );

            case VHS ->
                    HorrorManager.fireDirectorVhs();

            case TAB_MESSAGE ->
                    HorrorManager.fireTabIllusion(
                            client
                    );

            case EYES ->
                    HorrorManager.fireEyes();

            case FOOTSTEPS ->
                    HorrorManager.fireFootsteps(
                            client
                    );

            case TORCHES ->
                    HorrorManager.fireTorchIllusion(
                            client
                    );

            case DOPPELGANGER ->
                    HorrorManager.fireDoppelganger(
                            client
                    );

            case PERIPHERAL ->
                    HorrorManager.firePeripheral();

            case WORLD_DISTORT ->
                    HorrorManager.fireWorldDistortion();

            case RUN ->
                    HorrorManager.fireRun(
                            client
                    );

            case DONT_MOVE ->
                    HorrorManager.fireDontMove(
                            client
                    );

            case FIND_US ->
                    FindUsEvent.fire(
                            client
                    );

            case SIGN ->
                    SignIllusionEvent.start(
                            client
                    );

            case SILENCE -> {
                // Intentionally empty.
            }
        }
    }

    private static int delay(
            int min,
            int max
    ) {

        return max <= min
                ? min
                : min +
                        RANDOM.nextInt(
                                max - min + 1
                        );
    }

    private enum EventType {

        LOCAL_MESSAGE,
        GLITCH,
        WHISPER,
        FIGURE,
        DECOY,
        VHS,

        TAB_MESSAGE,
        EYES,
        FOOTSTEPS,
        TORCHES,

        DOPPELGANGER,
        PERIPHERAL,

        WORLD_DISTORT,

        RUN,

        DONT_MOVE,

        FIND_US,

        SIGN,

        SILENCE
    }

    private static final class ScheduledEvent {

        private final EventType type;
        private int delay;

        private ScheduledEvent(
                EventType type,
                int delay
        ) {

            this.type = type;
            this.delay = delay;
        }
    }
}
