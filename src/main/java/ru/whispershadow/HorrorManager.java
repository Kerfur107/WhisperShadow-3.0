package ru.whispershadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HorrorManager {

    private static final Random RANDOM =
            new Random();

    private static final String[] MESSAGES = {
            "I can see you.",
            "You are nothing.",
            "Don't turn around.",
            "I'm still here.",
            "Why did you look at me?",
            "You shouldn't be here.",
            "I know where you are.",
            "...",
            "Something is watching.",
            "It was closer a moment ago.",
            "Did you hear that?",
            "Don't look behind you.",
            "You missed something.",
            "I was here first.",
            "Can you hear me?",
            "Stop walking.",
            "Turn around.",
            "You are not alone.",
            "Keep your eyes open.",
            "I saw that.",
            "That wasn't there before.",
            "Look up.",
            "Look down.",
            "Don't answer.",
            "Why are you still here?",
            "I remember you.",
            "You forgot something.",
            "It's getting closer.",
            "I can hear you.",
            "You shouldn't have opened that.",
            "Something moved.",
            "Don't stay here.",
            "It knows.",
            "Not yet.",
            "Almost.",
            "Again.",
            "Did you see me?",
            "You looked right at me.",
            "I'm behind you.",
            "No, not there.",
            "Wrong way.",
            "You are being watched.",
            "You won't notice me.",
            "I don't like the light.",
            "Where are you going?",
            "Stay still.",
            "I found you.",
            "Hello?",
            "....",
            "I heard you breathe.",
            "Don't close the door.",
            "The dark is listening.",
            "You left something behind.",
            "That sound was mine.",
            "I was waiting.",
            "Not alone.",
            "Too late.",
            "Keep moving.",
            "No one is coming.",
            "I know what you saw.",
            "You shouldn't remember me.",
            "Look at the corner.",
            "Don't trust the silence.",
            "It is quieter when you are afraid.",
            "I am closer now.",
            "You almost found me.",
            "I don't need eyes.",
            "I know your footsteps.",
            "Stop looking for me.",
            "I can wait.",
            "You can't.",
            "The room changed.",
            "Was that your shadow?",
            "Don't blink.",
            "Something is standing there.",
            "You heard that.",
            "I know you did.",
            "Stay where the light reaches.",
            "The light won't save you.",
            "You are making noise.",
            "Quiet.",
            "Listen.",
            "Behind the wall.",
            "Under the floor.",
            "Above you.",
            "Not where you think.",
            "I was never far away.",
            "You invited me.",
            "You noticed.",
            "Now I notice you"
    };

    private static final String[] GENERIC_REPLIES = {
            "you said that out loud",
            "i heard you",
            "why are you asking me?",
            "say it again",
            "i know",
            "keep talking",
            "i'm listening",
            "you shouldn't tell me things",
            "that wasn't for you",
            "interesting",
            "go on",
            "don't stop",
            "i was waiting for that",
            "you really want an answer?",
            "you already know",
            "not yet",
            "closer",
            "wrong question",
            "try again",
            "i remember"
    };

    private static final String[] RARE_GLITCH_REPLIES = {
            "e̷r̷r̷.̷t̷y̷p̷e̷=̷{input}",
            "Y0U_SH0ULD_N0T_H4VE_WR1TTEN_TH4T",
            "b̴e̴h̴i̴n̴d̴ ̴y̴o̴u̴",
            "i̷ ̷s̷e̷e̷ ̷y̷o̷u̷",
            "N̸O̸T̸ ̸A̸ ̸P̸E̸R̸S̸O̸N̸",
            "// connection: WHISPER_SHADOW",
            "y̷o̷u̷r̷ ̷m̷i̷c̷ ̷i̷s̷ ̷o̷n̷",
            "[redacted]",
            "0xBEHIND_YOU",
            "d̶o̶ ̶n̶o̶t̶ ̶l̶o̶o̶k̶"
    };

    private static final String[] PLAYER_CHAT_GLITCH_MESSAGES = {

            "that's not me",
            "I didn't type that",
            "I didn't send that",
            "I never said that",
            "I never wrote that",
            "I don't remember saying that",
            "I don't remember typing that",
            "I don't remember sending that",
            "that's not what I said",
            "that's not what I typed",
            "that wasn't me",
            "that wasn't my message",
            "someone is using my name",
            "someone is typing for me",
            "someone is using my account",
            "something is typing for me",
            "something is pretending to be me",
            "I wasn't there",
            "I wasn't here",
            "I didn't go there",
            "I never went there",
            "who just sent that?",
            "did I just say that?",
            "did you see that message?",
            "wait, I didn't write that",
            "wait, that's not me",
            "I didn't press anything",
            "I didn't type anything",
            "my chat is acting weird",
            "something is wrong with my chat",
            "I think someone has my account",
            "I think someone is controlling me",
            "I'm not doing that",
            "that's not where I am",
            "I can see myself",
            "why can I see another me?",
            "there's another me",
            "I see someone with my skin",
            "someone has my skin",
            "someone looks exactly like me",
            "there's someone pretending to be me",
            "that's my name but that's not me",
            "that's my skin but that's not me",
            "I'm right here",
            "I'm still here",
            "I'm not over there",
            "that's not my player",
            "why is there another player with my name?",
            "there are two of me",
            "I think there's a copy of me",
            "something copied me",
            "it copied my skin",
            "it copied my name",
            "it copied everything",
            "it knows my name",
            "it is using my name",
            "it is using my skin",
            "it is pretending to be me",
            "don't trust the other me",
            "don't talk to the other me",
            "that's not me behind you",
            "if you see me, that's not me",
            "if I appear twice, leave",
            "if you see two of me, don't move",
            "don't follow the one that looks like me",
            "the other one isn't me",
            "the one behind you isn't me",
            "I don't know who that is",
            "I think something is copying us",
            "it's learning our names",
            "it's pretending to be us",
            "it can use our names",
            "it can send messages as us",
            "don't believe messages from me",
            "don't believe anything I say",
            "if I tell you to follow me, don't",
            "if I tell you to turn around, don't",
            "I don't know who is typing this",
            "I don't know if this is really me",
            "I'm scared to type anything",
            "I think it can read my chat",
            "I think it can control my chat",
            "it just typed something",
            "it just moved me",
            "it just looked at you",
            "it is standing where I was",
            "it was standing next to me",
            "it was wearing my skin",
            "it had my name",
            "it looked exactly like me",
            "I saw myself across the room",
            "I saw myself behind you",
            "I saw another me",
            "I don't think I'm the only one",
            "there's something pretending to be us",
            "I think it wants you to trust me",
            "I think it wants you to think it's me",
            "it wants you to follow me",
            "don't follow me",
            "I didn't ask you to come here",
            "I didn't tell you to follow me",
            "I didn't tell you anything",
            "I swear that wasn't me",
            "I swear I didn't type that",
            "I swear I'm not doing this",
            "please believe me",
            "please don't trust me",
            "don't trust my messages",
            "don't trust my name",
            "don't trust my skin",
            "don't trust the player that looks like me"
    };

    private static final String[] PLAYER_CHAT_MESSAGES = {

            "you are nothing",
            "don't look behind you",
            "I can see you",
            "where are you",
            "run",
            "leave",
            "we are watching",
            "you shouldn't be here",
            "don't trust them",
            "did you hear that?",
            "he is behind you",
            "I saw it too",
            "something is wrong",
            "can you see it?",
            "are you alone?",
            "why are you still here?",
            "get out",
            "turn around",
            "don't turn around",
            "keep moving",
            "don't stop",
            "hide",
            "stay away",
            "go back",
            "leave this place",
            "it's watching you",
            "it's getting closer",
            "I wouldn't stay there",
            "you need to leave",

            "what was that?",
            "did you see that?",
            "I just saw something",
            "there was someone behind you",
            "I saw someone near you",
            "something moved",
            "something is following you",
            "there is something behind you",
            "I think you're being followed",
            "I don't think you're alone",
            "look at the trees",
            "look behind you",
            "check your surroundings",
            "don't go there",
            "don't go that way",
            "something is waiting for you",
            "I wouldn't go inside",
            "don't enter that room",
            "there's someone here",
            "I heard footsteps",

            "why are you ignoring me?",
            "can you hear me?",
            "answer me",
            "say something",
            "why aren't you talking?",
            "I've been trying to reach you",
            "I've been watching you",
            "I know where you are",
            "I know what you're doing",
            "I know you're there",
            "you can't hide",
            "you can't run forever",
            "we know where you are",
            "we know what you did",
            "you shouldn't have come here",
            "you should have left earlier",

            "help",
            "HELP",
            "run",
            "RUN",
            "leave",
            "STOP",
            "don't",
            "behind you",
            "look behind you",
            "please",
            "please leave",
            "please run",

            "he knows you're here",
            "it knows you're here",
            "it can see you",
            "it can hear you",
            "it is getting closer",
            "it's right there",
            "it's standing behind you",
            "it's been here the whole time",
            "it never left",
            "you already saw it",
            "you just don't remember",
            "you've seen this before",
            "this has happened before",
            "you were warned",
            "you should not have joined",
            "you were not supposed to be here",
            "we tried to warn you",
            "we can't help you",
            "there's nothing we can do",
            "it's too late",

            "I can see what you're looking at",
            "I know where you're looking",
            "stop looking at the darkness",
            "don't look into the dark",
            "don't follow the voice",
            "don't answer it",
            "don't talk to it",
            "if you hear your name, don't answer",
            "if you see me, run",
            "if I disappear, don't look for me",
            "if I say run, run",
            "don't wait for me",
            "I'm not alone",
            "I'm not the one talking",
            "that's not my message",
            "something is typing for me"
    };

    // GENERAL HORROR STATE
    private static int glitchTicks = 0;
    private static int glitchStyle = 0;
    private static int vhsTicks = 0;
    private static int figureTicks = 0;
    private static int replyDelay = 0;
    private static String pendingReply = null;
    private static ShadowEntity figure = null;
    private static int doppelgangerTicks = 0;
    private static ShadowEntity doppelganger = null;
    private static int eyesTicks = 0;
    private static int peripheralTicks = 0;
    private static int distortionTicks = 0;
    private static int torchTicks = 0;

    private static int playerChatCooldown = 0;
    private static int playerChatGlitchCooldown = 0;

    private static final Map<BlockPos, BlockState> hiddenTorches =
            new HashMap<>();

    private static String tabIllusion = null;
    private static int tabIllusionTicks = 0;
    private static int tabMessageCooldown = 0;

    // SHOW YOURSELF
    private static int showYourselfCooldown = 0;
    private static int showYourselfDelay = 0;
    private static boolean showYourselfPending = false;

    private static final String[] SHOW_YOURSELF_REFUSALS = {
            "No.",
            "You are nothing to me.",
            "Go away.",
            "I don't want to see you.",
            "You cannot summon me.",
            "I am not here.",
            "Stop calling me.",
            "You are not ready.",
            "Why should I show myself?",
            "Not yet.",
            "You don't deserve to see me.",
            "Leave me alone.",
            "You cannot see me.",
            "Wrong question.",
            "I refuse.",
            "You really want to see me?",
            "No. You are nothing.",
            "I was never here.",
            "You should not ask that.",
            "Keep looking."
    };

    // RUN / CHASE
    private static boolean runActive = false;
    private static int runTicks = 0;
    private static final int RUN_DURATION_TICKS = 20 * 20;

    private static boolean brokenScriptActive = false;
    private static int brokenScriptStage = 0;
    private static int brokenScriptTicks = 0;
    private static int brokenScriptCooldown = 0;

    private static String brokenScriptPlayer = null;

    // Player skin
    private static com.mojang.authlib.GameProfile getRandomPlayerProfile(
            MinecraftClient client
    ) {

        List<PlayerListEntry> players =
                new ArrayList<>();

        for (PlayerListEntry entry :
                client.player.networkHandler
                        .getPlayerList()) {

            if (entry.getProfile() != null &&
                    !entry.getProfile()
                            .id()
                            .equals(
                                    client.player
                                            .getGameProfile()
                                            .id()
                            )) {

                players.add(entry);
            }
        }

        if (!players.isEmpty()) {

            PlayerListEntry selected =
                    players.get(
                            RANDOM.nextInt(
                                    players.size()
                            )
                    );

            return selected.getProfile();
        }

        return client.player.getGameProfile();
    }

    // DON'T MOVE
    private static boolean dontMoveActive = false;
    private static int dontMoveTicks = 0;

    private static double dontMoveStartX = 0.0;
    private static double dontMoveStartY = 0.0;
    private static double dontMoveStartZ = 0.0;

    private static final int DONT_MOVE_DURATION_TICKS = 20 * 3;

    private HorrorManager() {}

    public static void init() {}

    // PLAYER CHAT
    public static void handlePlayerChat(String message) {

        if (WatcherEvent.isActive())
            return;

        MinecraftClient client =
                MinecraftClient.getInstance();

        if (client.player == null ||
                message == null ||
                message.isBlank())
            return;

        String cleanMessage =
                message.trim()
                        .toLowerCase(Locale.ROOT);

        if (cleanMessage.equals("show yourself")) {

            if (showYourselfCooldown > 0)
                return;

            showYourselfCooldown = 20 * 30;

            boolean shadowAppears =
                    RANDOM.nextInt(100) < 15;

            if (shadowAppears) {

                showYourselfPending = true;
                showYourselfDelay = 40;

                client.player.sendMessage(
                        Text.literal("...")
                                .formatted(
                                        Formatting.DARK_GRAY
                                ),
                        false
                );

                InsanityManager.add(1.0f);

            } else {

                String refusal =
                        SHOW_YOURSELF_REFUSALS[
                                RANDOM.nextInt(
                                        SHOW_YOURSELF_REFUSALS.length
                                )
                        ];

                pendingReply = refusal;

                replyDelay =
                        25 +
                                RANDOM.nextInt(45);

                InsanityManager.add(
                        0.3f +
                                RANDOM.nextFloat() * 1.2f
                );
            }

            return;
        }

        int level =
                InsanityManager.getLevelNumber();

        int chance =
                level >= 4
                        ? 48
                        : level >= 2
                        ? 36
                        : 24;

        if (RANDOM.nextInt(100) >= chance)
            return;

        if (replyDelay > 0 ||
                pendingReply != null)
            return;

        pendingReply =
                chooseReply(message);

        replyDelay =
                25 +
                        RANDOM.nextInt(150);

        InsanityManager.add(
                0.7f +
                        RANDOM.nextFloat() * 2.0f
        );
    }

    // NORMAL REPLIES
    private static String chooseReply(String input) {

        String clean =
                input.trim();

        String lower =
                clean.toLowerCase(Locale.ROOT);

        String normalized =
                lower
                        .replaceAll(
                                "[^a-z0-9? ]",
                                ""
                        )
                        .trim();

        if (lower.contains("where are you") ||
                lower.contains("where r u"))
            return "behind you";

        if (lower.contains("who are you") ||
                lower.contains("what are you"))
            return "you already know";

        if (lower.contains("are you real") ||
                lower.contains("are you there"))
            return "does it matter?";

        if (lower.contains("hello") ||
                lower.equals("hi") ||
                lower.equals("hey"))
            return "err.type=" + clean;

        if (lower.contains("help"))
            return "i can help";

        if (lower.contains("go away") ||
                lower.contains("leave me") ||
                lower.contains("stop"))
            return "no";

        if (lower.contains("i see you"))
            return "look again";

        if (lower.contains("why me"))
            return "you noticed me";

        if (lower.contains("what are you doing"))
            return "watching";

        if (lower.contains("can you hear"))
            return "always";

        if (lower.contains("goodbye") ||
                lower.contains("bye"))
            return "not yet";

        if (lower.contains("behind you"))
            return "not anymore";

        if (lower.contains("fuck") ||
                lower.contains("kill"))
            return "language changes nothing";

        if (normalized.endsWith("?"))
            return GENERIC_REPLIES[
                    RANDOM.nextInt(
                            GENERIC_REPLIES.length
                    )
            ];

        return GENERIC_REPLIES[
                RANDOM.nextInt(
                        GENERIC_REPLIES.length
                )
        ];
    }

    // TICK
    public static void tick(MinecraftClient client) {

        /*
         * WATCHER EVENT LOCK
         *
         * Пока WatcherEvent активен:
         * - EventDirector не работает;
         * - FindUsEvent не работает;
         * - SignIllusionEvent не работает;
         * - ответы в чат не доставляются;
         * - RUN прекращается;
         * - фигуры удаляются;
         * - Doppelganger удаляется;
         * - BrokenScript прекращается;
         * - Don't Move прекращается;
         * - остальные таймеры HorrorManager не тикают.
         *
         * Сам WatcherEvent обновляется отдельно
         * в WhisperShadowClient.
         */
        if (WatcherEvent.isActive()) {

            stopChase(client);
            removeFigure(client);
            removeDoppelganger(client);
            restoreTorches(client);

            replyDelay = 0;
            pendingReply = null;

            showYourselfDelay = 0;
            showYourselfPending = false;

            dontMoveActive = false;
            dontMoveTicks = 0;

            brokenScriptActive = false;
            brokenScriptStage = 0;
            brokenScriptTicks = 0;
            brokenScriptPlayer = null;

            return;
        }

        InsanityManager.tick(client);
        EventDirector.tick(client);

        // FIND US EVENT
        FindUsEvent.tick(client);

        // SIGN ILLUSION EVENT
        SignIllusionEvent.tick(client);

        if (client.player == null ||
                client.world == null) {

            stopChase(client);
            removeFigure(client);

            replyDelay = 0;
            pendingReply = null;

            vhsTicks = 0;

            removeDoppelganger(client);
            restoreTorches(client);

            eyesTicks =
                    peripheralTicks =
                    distortionTicks =
                    torchTicks =
                    tabIllusionTicks =
                    dontMoveTicks = 0;

            dontMoveActive = false;

            showYourselfCooldown = 0;
            showYourselfDelay = 0;
            showYourselfPending = false;

            playerChatCooldown = 0;
            playerChatGlitchCooldown = 0;

            return;
        }

        if (showYourselfCooldown > 0)
            showYourselfCooldown--;

        if (showYourselfPending) {

            if (showYourselfDelay > 0)
                showYourselfDelay--;

            if (showYourselfDelay == 0) {

                showYourselfPending = false;

                if (client.player != null &&
                        client.world != null &&
                        !WatcherEvent.isActive()) {

                    fireDirectorFigure(client);

                    client.player.playSound(
                            ModSounds.WHISPER,
                            0.35f +
                                    RANDOM.nextFloat() * 0.25f,
                            0.55f +
                                    RANDOM.nextFloat() * 0.25f
                    );

                    InsanityManager.add(
                            5.0f +
                                    RANDOM.nextFloat() * 3.0f
                    );

                    fireDirectorGlitch();

                    client.player.sendMessage(
                            Text.literal(
                                    "You asked me to show myself."
                            ).formatted(
                                    Formatting.DARK_RED
                            ),
                            false
                    );
                }
            }
        }

        if (replyDelay > 0)
            replyDelay--;

        if (replyDelay == 0 &&
                pendingReply != null &&
                !WatcherEvent.isActive()) {

            deliverReply(
                    client,
                    pendingReply
            );

            pendingReply = null;
        }

        if (glitchTicks > 0)
            glitchTicks--;

        if (vhsTicks > 0)
            vhsTicks--;

        if (eyesTicks > 0)
            eyesTicks--;

        if (peripheralTicks > 0)
            peripheralTicks--;

        if (distortionTicks > 0)
            distortionTicks--;

        if (tabIllusionTicks > 0)
            tabIllusionTicks--;

        if (tabMessageCooldown > 0)
            tabMessageCooldown--;

        if (playerChatCooldown > 0)
            playerChatCooldown--;

        if (playerChatGlitchCooldown > 0)
            playerChatGlitchCooldown--;

        if (brokenScriptCooldown > 0)
            brokenScriptCooldown--;

        if (brokenScriptActive)
            tickBrokenScript(client);

        if (!WatcherEvent.isActive()) {

            if (playerChatCooldown <= 0) {

                if (RANDOM.nextInt(1000) < 2) {

                    firePlayerChat(client);

                    playerChatCooldown =
                            20 * (
                                    35 +
                                            RANDOM.nextInt(86)
                            );
                }
            }

            if (playerChatGlitchCooldown <= 0) {

                if (RANDOM.nextInt(2500) < 2) {

                    firePlayerChatGlitch(client);

                    playerChatGlitchCooldown =
                            20 * (
                                    90 +
                                            RANDOM.nextInt(151)
                            );
                }
            }
        }

        // DON'T MOVE
        if (dontMoveActive) {

            dontMoveTicks--;

            double dx =
                    client.player.getX() -
                            dontMoveStartX;

            double dy =
                    client.player.getY() -
                            dontMoveStartY;

            double dz =
                    client.player.getZ() -
                            dontMoveStartZ;

            double movedSq =
                    dx * dx +
                            dy * dy +
                            dz * dz;

            if (movedSq > 0.0025) {

                dontMoveActive = false;
                dontMoveTicks = 0;

                client.player.sendMessage(
                        Text.literal("YOU MOVED.")
                                .formatted(
                                        Formatting.DARK_RED,
                                        Formatting.BOLD
                                ),
                        false
                );

                client.player.playSound(
                        ModSounds.GLITCH,
                        0.45f,
                        0.55f +
                                RANDOM.nextFloat() * 0.25f
                );

                client.player.playSound(
                        ModSounds.STATIC,
                        0.30f,
                        0.45f +
                                RANDOM.nextFloat() * 0.25f
                );

                fireDirectorGlitch();

                InsanityManager.add(
                        8.0f +
                                RANDOM.nextFloat() * 6.0f
                );

            } else if (dontMoveTicks <= 0) {

                dontMoveActive = false;
                dontMoveTicks = 0;

                client.player.playSound(
                        ModSounds.WHISPER,
                        0.08f,
                        0.40f +
                                RANDOM.nextFloat() * 0.20f
                );

                if (RANDOM.nextInt(100) < 25)
                    fireEyes();
            }
        }

        if (doppelgangerTicks > 0 &&
                doppelganger != null) {

            doppelgangerTicks--;

            doppelganger.horrorTick(
                    client.player
            );

        } else if (doppelganger != null) {

            removeDoppelganger(client);
        }

        if (torchTicks > 0) {

            torchTicks--;

            if (torchTicks == 0)
                restoreTorches(client);
        }

        if (runActive) {

            runTicks--;

            if (figure != null) {

                figure.chasePlayer(
                        client.player
                );

                if (figure.getBoundingBox()
                        .expand(0.30)
                        .intersects(
                                client.player
                                        .getBoundingBox()
                        )) {

                    InsanityManager.add(20.0f);

                    if (RANDOM.nextInt(100) < 5) {

                        stopChase(client);
                        removeFigure(client);

                        client.world.disconnect(
                                Text.literal(
                                        "You were caught."
                                )
                        );

                        return;
                    }

                    client.player.sendMessage(
                            Text.literal("...")
                                    .formatted(
                                            Formatting.DARK_RED
                                    ),
                            false
                    );

                    fireDirectorGlitch();

                    stopChase(client);
                    removeFigure(client);

                    return;
                }
            }

            if (runTicks <= 0) {

                stopChase(client);
                removeFigure(client);

                client.player.sendMessage(
                        Text.literal("...")
                                .formatted(
                                        Formatting.DARK_GRAY
                                ),
                        false
                );
            }
        }

        if (!runActive) {

            if (figureTicks > 0) {

                figureTicks--;

                if (figure != null) {

                    figure.horrorTick(
                            client.player
                    );

                    if (HorrorConfig.ENABLE_TOUCH_DISCONNECT &&
                            figure.getBoundingBox()
                                    .expand(0.30)
                                    .intersects(
                                            client.player
                                                    .getBoundingBox()
                                    )) {

                        InsanityManager.add(20);

                        client.world.disconnect(
                                Text.literal(
                                        "You were touched."
                                )
                        );

                        return;
                    }
                }

            } else if (figure != null) {

                removeFigure(client);
            }
        }
    }

    // RUN EVENT
    public static void fireRun(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null)
            return;

        if (runActive)
            return;

        if (dontMoveActive) {
            dontMoveActive = false;
            dontMoveTicks = 0;
        }

        runActive = true;
        runTicks = RUN_DURATION_TICKS;

        removeFigure(client);

        spawnChaseFigure(client);

        client.player.sendMessage(
                Text.literal("RUN.")
                        .formatted(
                                Formatting.DARK_RED,
                                Formatting.BOLD
                        ),
                false
        );

        fireDirectorGlitch();

        playChaseSound(client);

        InsanityManager.add(
                8.0f +
                        RANDOM.nextFloat() * 5.0f
        );
    }

    private static void spawnChaseFigure(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null)
            return;

        double yaw =
                Math.toRadians(
                        client.player.getYaw() +
                                180.0
                );

        double distance =
                8.0 +
                        RANDOM.nextDouble() * 3.0;

        double x =
                client.player.getX() +
                        Math.sin(yaw) *
                                distance;

        double z =
                client.player.getZ() -
                        Math.cos(yaw) *
                                distance;

        ShadowEntity entity =
                new ShadowEntity(
                        client.world,
                        getRandomPlayerProfile(client),
                        false
                );

        entity.setId(
                -800000 -
                        RANDOM.nextInt(100000)
        );

        entity.refreshPositionAndAngles(
                x,
                client.player.getY(),
                z,
                client.player.getYaw(),
                0.0f
        );

        entity.setNoGravity(true);
        entity.setInvisible(false);

        client.world.addEntity(entity);

        figure = entity;

        figureTicks =
                RUN_DURATION_TICKS;
    }

    // CHASE MUSIC
    private static void playChaseSound(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null)
            return;

        client.player.playSound(
                ModSounds.CIRCUIT_CHASE,
                1.0f,
                1.0f
        );
    }

    private static void stopChaseSound(
            MinecraftClient client
    ) {

        client.getSoundManager().stopSounds(
                ModSounds.CIRCUIT_CHASE_ID,
                null
        );
    }

    private static void stopChase(
            MinecraftClient client
    ) {

        runActive = false;
        runTicks = 0;

        stopChaseSound(client);
    }

    // DON'T MOVE EVENT
    public static void fireDontMove(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null ||
                dontMoveActive ||
                runActive)
            return;

        if (InsanityManager.getLevelNumber() < 2)
            return;

        dontMoveActive = true;

        dontMoveTicks =
                DONT_MOVE_DURATION_TICKS;

        dontMoveStartX =
                client.player.getX();

        dontMoveStartY =
                client.player.getY();

        dontMoveStartZ =
                client.player.getZ();

        client.player.playSound(
                ModSounds.WHISPER,
                0.22f,
                0.45f +
                        RANDOM.nextFloat() * 0.20f
        );

        InsanityManager.add(
                1.5f +
                        RANDOM.nextFloat() * 1.5f
        );
    }

    // DELIVER REPLY
    private static void deliverReply(
            MinecraftClient client,
            String reply
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null)
            return;

        boolean rare =
                RANDOM.nextInt(100) < 10;

        if (rare) {

            String template =
                    RARE_GLITCH_REPLIES[
                            RANDOM.nextInt(
                                    RARE_GLITCH_REPLIES.length
                            )
                    ];

            reply =
                    template.replace(
                            "{input}",
                            reply
                    );

            client.player.sendMessage(
                    glitchText(reply),
                    false
            );

            fireDirectorGlitch();

            client.player.playSound(
                    ModSounds.GLITCH,
                    0.18f +
                            RANDOM.nextFloat() * 0.18f,
                    0.75f +
                            RANDOM.nextFloat() * 0.45f
            );

        } else {

            client.player.sendMessage(
                    coloredEntityText(reply),
                    false
            );

            if (RANDOM.nextInt(100) < 55) {

                client.player.playSound(
                        ModSounds.WHISPER,
                        0.10f +
                                RANDOM.nextFloat() * 0.20f,
                        0.72f +
                                RANDOM.nextFloat() * 0.45f
                );
            }
        }
    }

    // DIRECTOR EVENTS
    public static void fireDirectorMessage(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null)
            return;

        client.player.sendMessage(
                coloredEntityText(
                        MESSAGES[
                                RANDOM.nextInt(
                                        MESSAGES.length
                                )
                        ]
                ),
                false
        );

        InsanityManager.add(
                1.0f +
                        RANDOM.nextFloat() * 2.5f
        );
    }

    public static void fireDirectorGlitch() {

        if (WatcherEvent.isActive())
            return;

        int level =
                InsanityManager.getLevelNumber();

        glitchTicks =
                2 +
                        RANDOM.nextInt(
                                5 +
                                        level * 3
                        );

        glitchStyle =
                RANDOM.nextInt(6);

        InsanityManager.add(
                0.8f +
                        RANDOM.nextFloat() * 1.8f
        );
    }

    public static void fireDirectorWhisper(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null)
            return;

        client.player.playSound(
                ModSounds.WHISPER,
                0.20f +
                        RANDOM.nextFloat() * 0.45f,
                0.70f +
                        RANDOM.nextFloat() * 0.55f
        );

        if (RANDOM.nextInt(100) < 18)
            fireDirectorGlitch();

        InsanityManager.add(
                1.0f +
                        RANDOM.nextFloat() * 3.0f
        );
    }

    public static void fireDirectorFigure(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (figure == null &&
                client.player != null &&
                client.world != null &&
                !runActive) {

            spawnFigure(
                    client,
                    InsanityManager.getLevelNumber()
            );
        }
    }

    public static void fireDecoy(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        fireDirectorGlitch();

        if (client.player != null) {

            client.player.playSound(
                    ModSounds.STATIC,
                    0.12f,
                    0.6f +
                            RANDOM.nextFloat() * 0.7f
            );
        }
    }

    public static void fireDirectorVhs() {

        if (WatcherEvent.isActive())
            return;

        if (vhsTicks > 0)
            return;

        vhsTicks =
                160 +
                        RANDOM.nextInt(950);

        MinecraftClient client =
                MinecraftClient.getInstance();

        if (client.player != null) {

            client.player.playSound(
                    ModSounds.VHS,
                    0.10f +
                            RANDOM.nextFloat() * 0.12f,
                    0.82f +
                            RANDOM.nextFloat() * 0.18f
            );
        }

        InsanityManager.add(
                2.0f +
                        RANDOM.nextFloat() * 3.5f
        );
    }

    // SPAWN SHADOW
    public static void spawnFigure(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        spawnFigure(
                client,
                0
        );
    }

    private static void spawnFigure(
            MinecraftClient client,
            int level
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null)
            return;

        double angle;
        double distance;

        if (level >= 4 &&
                RANDOM.nextInt(100) < 45) {

            angle =
                    Math.toRadians(
                            client.player.getYaw() +
                                    180.0
                    );

            distance =
                    5.0 +
                            RANDOM.nextDouble() * 5.0;

        } else {

            angle =
                    RANDOM.nextDouble() *
                            Math.PI *
                            2.0;

            distance =
                    HorrorConfig.FIGURE_MIN_DISTANCE +
                            RANDOM.nextDouble() *
                                    (
                                            HorrorConfig.FIGURE_MAX_DISTANCE -
                                                    HorrorConfig.FIGURE_MIN_DISTANCE
                                    );
        }

        double x =
                client.player.getX() +
                        Math.cos(angle) *
                                distance;

        double z =
                client.player.getZ() +
                        Math.sin(angle) *
                                distance;

        ShadowEntity entity =
                new ShadowEntity(
                        client.world,
                        getRandomPlayerProfile(client),
                        false
                );

        entity.refreshPositionAndAngles(
                x,
                client.player.getY(),
                z,
                RANDOM.nextFloat() * 360.0f,
                0.0f
        );

        entity.setId(
                -700000 -
                        RANDOM.nextInt(100000)
        );

        entity.setNoGravity(true);
        entity.setInvisible(false);

        client.world.addEntity(entity);

        figure = entity;

        figureTicks =
                HorrorConfig.FIGURE_LIFETIME_TICKS +
                        level * 20;

        InsanityManager.add(
                4.0f +
                        level * 1.5f
        );
    }

    private static void removeFigure(
            MinecraftClient client
    ) {

        if (figure != null) {

            figure.remove(
                    net.minecraft.entity.Entity.RemovalReason
                            .DISCARDED
            );

            figure = null;
        }

        figureTicks = 0;
    }

    // CHAT COLORS
    private static MutableText coloredEntityText(
            String message
    ) {

        int roll =
                RANDOM.nextInt(100);

        Formatting color =
                roll < 58
                        ? Formatting.DARK_RED
                        : roll < 76
                        ? Formatting.RED
                        : roll < 91
                        ? Formatting.DARK_GRAY
                        : Formatting.BLACK;

        MutableText text =
                Text.literal(message)
                        .formatted(color);

        if (roll >= 72 &&
                RANDOM.nextBoolean()) {

            text.formatted(
                    Formatting.BOLD
            );
        }

        return text;
    }

    private static MutableText glitchText(
            String message
    ) {

        MutableText out =
                Text.empty();

        Formatting[] colors = {
                Formatting.DARK_RED,
                Formatting.RED,
                Formatting.DARK_GRAY,
                Formatting.BLACK,
                Formatting.GRAY
        };

        for (int i = 0;
             i < message.length();
             i++) {

            char c =
                    message.charAt(i);

            MutableText part =
                    Text.literal(
                            String.valueOf(c)
                    ).formatted(
                            colors[
                                    RANDOM.nextInt(
                                            colors.length
                                    )
                            ]
                    );

            if (RANDOM.nextInt(100) < 28) {

                part.formatted(
                        Formatting.OBFUSCATED
                );
            }

            if (RANDOM.nextInt(100) < 18) {

                part.formatted(
                        Formatting.BOLD
                );
            }

            out.append(part);
        }

        return out;
    }

    // TAB ILLUSION
    public static void fireTabIllusion(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                tabMessageCooldown > 0)
            return;

        List<String> names =
                new ArrayList<>();

        for (PlayerListEntry entry :
                client.player.networkHandler
                        .getPlayerList()) {

            if (entry.getProfile() != null &&
                    entry.getProfile().name() != null) {

                names.add(
                        entry.getProfile().name()
                );
            }
        }

        if (names.isEmpty()) {

            names.add(
                    client.player
                            .getGameProfile()
                            .name()
            );
        }

        String name =
                names.get(
                        RANDOM.nextInt(
                                names.size()
                        )
                );

        String[] patterns = {
                name + " was not found",
                name + " can't hide",
                name + " is behind you",
                name + " left something here",
                name + " was here first",
                name + " is watching",
                name + " doesn't exist",
                name + " is not alone",
                name + " saw you"
        };

        tabIllusion =
                patterns[
                        RANDOM.nextInt(
                                patterns.length
                        )
                ];

        tabIllusionTicks =
                55 +
                        RANDOM.nextInt(100);

        tabMessageCooldown =
                500 +
                        RANDOM.nextInt(1000);

        client.player.sendMessage(
                glitchText(tabIllusion),
                false
        );

        InsanityManager.add(
                2.0f +
                        RANDOM.nextFloat() * 3.0f
        );
    }

    // BROKEN SCRIPT
    public static void fireBrokenScript(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null)
            return;

        if (brokenScriptActive)
            return;

        if (runActive)
            return;

        List<PlayerListEntry> players =
                new ArrayList<>(
                        client.player.networkHandler
                                .getPlayerList()
                );

        players.removeIf(entry ->
                entry.getProfile() == null ||
                        entry.getProfile().name() == null
        );

        if (players.isEmpty())
            return;

        PlayerListEntry entry =
                players.get(
                        RANDOM.nextInt(
                                players.size()
                        )
                );

        brokenScriptPlayer =
                entry.getProfile().name();

        brokenScriptActive = true;
        brokenScriptStage = 0;
        brokenScriptTicks = 0;

        client.player.sendMessage(
                Text.literal(
                        brokenScriptPlayer +
                                " joined the game"
                ).formatted(
                        Formatting.GRAY
                ),
                false
        );
    }

    private static void tickBrokenScript(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive()) {
            stopBrokenScript();
            return;
        }

        if (client.player == null ||
                client.world == null) {

            stopBrokenScript();
            return;
        }

        brokenScriptTicks++;

        switch (brokenScriptStage) {

            case 0 -> {

                if (brokenScriptTicks >= 60) {

                    brokenScriptTicks = 0;
                    brokenScriptStage = 1;

                    sendBrokenChat(
                            client,
                            brokenScriptPlayer +
                                    ": can you see this?"
                    );
                }
            }

            case 1 -> {

                if (brokenScriptTicks >= 45) {

                    brokenScriptTicks = 0;
                    brokenScriptStage = 2;

                    sendBrokenChat(
                            client,
                            brokenScriptPlayer +
                                    ": can y█u s██ th██?"
                    );

                    client.player.playSound(
                            ModSounds.GLITCH,
                            0.12f,
                            0.70f +
                                    RANDOM.nextFloat() * 0.25f
                    );
                }
            }

            case 2 -> {

                if (brokenScriptTicks >= 50) {

                    brokenScriptTicks = 0;
                    brokenScriptStage = 3;

                    sendBrokenChat(
                            client,
                            brokenScriptPlayer +
                                    " left the game"
                    );
                }
            }

            case 3 -> {

                if (brokenScriptTicks >= 35) {

                    brokenScriptTicks = 0;
                    brokenScriptStage = 4;

                    sendBrokenChat(
                            client,
                            brokenScriptPlayer +
                                    ": █████████"
                    );

                    client.player.playSound(
                            ModSounds.GLITCH,
                            0.18f,
                            0.55f
                    );

                    fireDirectorGlitch();
                }
            }

            case 4 -> {

                if (brokenScriptTicks >= 50) {

                    brokenScriptTicks = 0;
                    brokenScriptStage = 5;

                    sendBrokenChat(
                            client,
                            brokenScriptPlayer +
                                    ": RUN"
                    );
                }
            }

            case 5 -> {

                if (brokenScriptTicks >= 25) {

                    brokenScriptTicks = 0;
                    brokenScriptStage = 6;

                    fireRun(client);
                }
            }

            case 6 -> {

                if (!runActive) {

                    brokenScriptTicks = 0;
                    brokenScriptStage = 7;
                }
            }

            case 7 -> {

                if (brokenScriptTicks >= 40) {

                    sendBrokenChat(
                            client,
                            brokenScriptPlayer +
                                    ": did you see that?"
                    );

                    InsanityManager.add(
                            3.0f +
                                    RANDOM.nextFloat() * 3.0f
                    );

                    stopBrokenScript();
                }
            }
        }
    }

    private static void sendBrokenChat(
            MinecraftClient client,
            String message
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null)
            return;

        MutableText text =
                Text.literal(message);

        int roll =
                RANDOM.nextInt(100);

        if (roll < 15) {

            text = glitchText(message)
                    .formatted(
                            Formatting.DARK_RED
                    );

        } else if (roll < 30) {

            text.formatted(
                    Formatting.RED
            );

        } else if (roll < 45) {

            text.formatted(
                    Formatting.DARK_PURPLE
            );

        } else if (roll < 60) {

            text.formatted(
                    Formatting.DARK_GRAY
            );

        } else {

            text.formatted(
                    Formatting.WHITE
            );
        }

        client.player.sendMessage(
                text,
                false
        );
    }

    private static void stopBrokenScript() {

        brokenScriptActive = false;
        brokenScriptStage = 0;
        brokenScriptTicks = 0;
        brokenScriptPlayer = null;

        brokenScriptCooldown =
                20 * (
                        8 +
                                RANDOM.nextInt(13)
                );
    }

    // FAKE PLAYER CHAT
    private static void firePlayerChat(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null)
            return;

        List<PlayerListEntry> players =
                new ArrayList<>(
                        client.player.networkHandler
                                .getPlayerList()
                );

        players.removeIf(entry ->
                entry.getProfile() == null ||
                        entry.getProfile().name() == null
        );

        if (players.isEmpty())
            return;

        PlayerListEntry entry =
                players.get(
                        RANDOM.nextInt(
                                players.size()
                        )
                );

        String playerName =
                entry.getProfile().name();

        String message =
                PLAYER_CHAT_MESSAGES[
                        RANDOM.nextInt(
                                PLAYER_CHAT_MESSAGES.length
                        )
                ];

        MutableText chat =
                Text.literal(
                        playerName +
                                ": "
                ).formatted(
                        Formatting.GRAY
                );

        int roll =
                RANDOM.nextInt(100);

        if (roll < 5) {

            chat.append(
                    glitchText(message)
            );

            client.player.playSound(
                    ModSounds.GLITCH,
                    0.08f +
                            RANDOM.nextFloat() * 0.12f,
                    0.75f +
                            RANDOM.nextFloat() * 0.35f
            );

            fireDirectorGlitch();

        } else if (roll < 18) {

            chat.append(
                    Text.literal(message)
                            .formatted(
                                    Formatting.DARK_RED
                            )
            );

        } else if (roll < 28) {

            chat.append(
                    Text.literal(message)
                            .formatted(
                                    Formatting.RED
                            )
            );

        } else if (roll < 38) {

            chat.append(
                    Text.literal(message)
                            .formatted(
                                    Formatting.DARK_GRAY
                            )
            );

        } else {

            chat.append(
                    Text.literal(message)
                            .formatted(
                                    Formatting.WHITE
                            )
            );
        }

        client.player.sendMessage(
                chat,
                false
        );

        InsanityManager.add(
                0.5f +
                        RANDOM.nextFloat() * 1.5f
        );
    }

    // FAKE PLAYER CHAT GLITCH
    private static void firePlayerChatGlitch(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null)
            return;

        List<PlayerListEntry> players =
                new ArrayList<>(
                        client.player.networkHandler
                                .getPlayerList()
                );

        players.removeIf(entry ->
                entry.getProfile() == null ||
                        entry.getProfile().name() == null
        );

        if (players.isEmpty())
            return;

        PlayerListEntry entry =
                players.get(
                        RANDOM.nextInt(
                                players.size()
                        )
                );

        String playerName =
                entry.getProfile().name();

        String message =
                PLAYER_CHAT_GLITCH_MESSAGES[
                        RANDOM.nextInt(
                                PLAYER_CHAT_GLITCH_MESSAGES.length
                        )
                ];

        MutableText chat =
                Text.literal(
                        playerName +
                                ": "
                ).formatted(
                        Formatting.GRAY
                );

        int colorRoll =
                RANDOM.nextInt(100);

        if (colorRoll < 25) {

            chat.append(
                    glitchText(message)
                            .formatted(
                                    Formatting.DARK_RED
                            )
            );

        } else if (colorRoll < 45) {

            chat.append(
                    glitchText(message)
                            .formatted(
                                    Formatting.RED
                            )
            );

        } else if (colorRoll < 65) {

            chat.append(
                    glitchText(message)
                            .formatted(
                                    Formatting.DARK_PURPLE
                            )
            );

        } else if (colorRoll < 80) {

            chat.append(
                    glitchText(message)
                            .formatted(
                                    Formatting.DARK_GRAY
                            )
            );

        } else {

            chat.append(
                    glitchText(message)
                            .formatted(
                                    Formatting.WHITE
                            )
            );
        }

        client.player.sendMessage(
                chat,
                false
        );

        client.player.playSound(
                ModSounds.GLITCH,
                0.10f +
                        RANDOM.nextFloat() * 0.15f,
                0.70f +
                        RANDOM.nextFloat() * 0.35f
        );

        fireDirectorGlitch();

        InsanityManager.add(
                2.0f +
                        RANDOM.nextFloat() * 3.0f
        );
    }

    // OTHER EVENTS
    public static void fireEyes() {

        if (WatcherEvent.isActive())
            return;

        eyesTicks =
                12 +
                        RANDOM.nextInt(35);

        InsanityManager.add(
                1.5f +
                        RANDOM.nextFloat() * 2.5f
        );
    }

    public static void firePeripheral() {

        if (WatcherEvent.isActive())
            return;

        peripheralTicks =
                5 +
                        RANDOM.nextInt(18);

        InsanityManager.add(
                1.0f +
                        RANDOM.nextFloat() * 2.0f
        );
    }

    public static void fireWorldDistortion() {

        if (WatcherEvent.isActive())
            return;

        distortionTicks =
                10 +
                        RANDOM.nextInt(55);

        glitchStyle =
                RANDOM.nextInt(8);

        if (RANDOM.nextInt(100) < 45)
            fireDirectorGlitch();

        MinecraftClient client =
                MinecraftClient.getInstance();

        if (client.player != null) {

            client.player.playSound(
                    ModSounds.STATIC,
                    0.08f,
                    0.55f +
                            RANDOM.nextFloat() * 0.65f
            );
        }

        InsanityManager.add(
                1.2f +
                        RANDOM.nextFloat() * 2.8f
        );
    }

    public static void fireFootsteps(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null)
            return;

        float yaw =
                client.player.getYaw();

        double angle =
                Math.toRadians(
                        yaw +
                                180.0 +
                                (
                                        RANDOM.nextDouble()
                                                * 28.0 -
                                                14.0
                                )
                );

        double distance =
                2.4 +
                        RANDOM.nextDouble() * 2.5;

        double x =
                client.player.getX() +
                        Math.sin(angle) *
                                distance;

        double z =
                client.player.getZ() -
                        Math.cos(angle) *
                                distance;

        client.world.playSoundClient(
                x,
                client.player.getY(),
                z,
                ModSounds.FOOTSTEPS,
                SoundCategory.AMBIENT,
                0.65f +
                        RANDOM.nextFloat() * 0.25f,
                0.72f +
                        RANDOM.nextFloat() * 0.2f,
                true
        );

        if (RANDOM.nextInt(100) < 45) {

            client.world.playSoundClient(
                    x + 0.35,
                    client.player.getY(),
                    z + 0.25,
                    ModSounds.FOOTSTEPS,
                    SoundCategory.AMBIENT,
                    0.5f,
                    0.65f +
                            RANDOM.nextFloat() * 0.2f,
                    true
            );
        }

        InsanityManager.add(
                1.0f +
                        RANDOM.nextFloat() * 2.5f
        );
    }

    // TORCH ILLUSION
    public static void fireTorchIllusion(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null ||
                torchTicks > 0)
            return;

        hiddenTorches.clear();

        BlockPos origin =
                client.player.getBlockPos();

        int radius = 20;

        for (int x = -radius;
             x <= radius;
             x++) {

            for (int y = -radius;
                 y <= radius;
                 y++) {

                for (int z = -radius;
                     z <= radius;
                     z++) {

                    if (x * x +
                            y * y +
                            z * z >
                            radius * radius)
                        continue;

                    BlockPos pos =
                            origin.add(
                                    x,
                                    y,
                                    z
                            );

                    BlockState state =
                            client.world
                                    .getBlockState(pos);

                    if (isTorch(
                            state.getBlock()
                    )) {

                        hiddenTorches.put(
                                pos,
                                state
                        );
                    }
                }
            }
        }

        if (hiddenTorches.isEmpty())
            return;

        for (BlockPos pos :
                hiddenTorches.keySet()) {

            client.world.setBlockState(
                    pos,
                    Blocks.AIR.getDefaultState(),
                    Block.NOTIFY_LISTENERS
            );
        }

        torchTicks =
                80 +
                        RANDOM.nextInt(180);

        client.player.playSound(
                ModSounds.STATIC,
                0.12f,
                0.6f
        );

        InsanityManager.add(
                3.0f +
                        RANDOM.nextFloat() * 4.0f
        );
    }

    private static boolean isTorch(
            Block block
    ) {

        return block == Blocks.TORCH ||
                block == Blocks.WALL_TORCH ||
                block == Blocks.SOUL_TORCH ||
                block == Blocks.SOUL_WALL_TORCH ||
                block == Blocks.LANTERN ||
                block == Blocks.SOUL_LANTERN;
    }

    private static void restoreTorches(
            MinecraftClient client
    ) {

        if (client.world == null ||
                hiddenTorches.isEmpty())
            return;

        for (Map.Entry<BlockPos, BlockState> entry :
                hiddenTorches.entrySet()) {

            if (client.world
                    .getBlockState(
                            entry.getKey()
                    )
                    .isAir()) {

                client.world.setBlockState(
                        entry.getKey(),
                        entry.getValue(),
                        Block.NOTIFY_LISTENERS
                );
            }
        }

        hiddenTorches.clear();
    }

    // DOPPELGANGER
    public static void fireDoppelganger(
            MinecraftClient client
    ) {

        if (WatcherEvent.isActive())
            return;

        if (client.player == null ||
                client.world == null ||
                doppelganger != null ||
                InsanityManager.getLevelNumber() < 3)
            return;

        double angle =
                Math.toRadians(
                        client.player.getYaw() +
                                180.0 +
                                (
                                        RANDOM.nextDouble()
                                                * 35.0 -
                                                17.5
                                )
                );

        double distance =
                7.0 +
                        RANDOM.nextDouble() * 11.0;

        double x =
                client.player.getX() +
                        Math.sin(angle) *
                                distance;

        double z =
                client.player.getZ() -
                        Math.cos(angle) *
                                distance;

        doppelganger =
                new ShadowEntity(
                        client.world,
                        getRandomPlayerProfile(client),
                        RANDOM.nextBoolean()
                );

        doppelganger.setId(
                -900000 -
                        RANDOM.nextInt(90000)
        );

        doppelganger.refreshPositionAndAngles(
                x,
                client.player.getY(),
                z,
                client.player.getYaw() +
                        180.0f,
                0.0f
        );

        doppelganger.setNoGravity(true);

        client.world.addEntity(
                doppelganger
        );

        doppelgangerTicks =
                220 +
                        RANDOM.nextInt(300);

        InsanityManager.add(
                6.0f +
                        RANDOM.nextFloat() * 5.0f
        );
    }

    private static void removeDoppelganger(
            MinecraftClient client
    ) {

        if (doppelganger != null) {

            doppelganger.remove(
                    net.minecraft.entity.Entity.RemovalReason
                            .DISCARDED
            );
        }

        doppelganger = null;
        doppelgangerTicks = 0;
    }

    // OVERLAY
    public static void renderOverlay(
            DrawContext context,
            RenderTickCounter tickCounter
    ) {
        if (WatcherEvent.isActive()) {
    WatcherEvent.renderOverlay(context);
}

        /*
         * Во время Watcher старые визуальные эффекты
         * HorrorManager не рисуются.
         *
         * Сам визуальный эффект Watcher добавим
         * отдельно в WatcherEvent.
         */
        if (WatcherEvent.isActive())
            return;

        if (glitchTicks <= 0 &&
                vhsTicks <= 0 &&
                eyesTicks <= 0 &&
                peripheralTicks <= 0 &&
                distortionTicks <= 0 &&
                tabIllusionTicks <= 0 &&
                !dontMoveActive &&
                !FindUsEvent.isActive() &&
                !SignIllusionEvent.isActive())
            return;

        int width =
                context.getScaledWindowWidth();

        int height =
                context.getScaledWindowHeight();

        int level =
                InsanityManager.getLevelNumber();

        if (vhsTicks > 0)
            renderVhs(
                    context,
                    width,
                    height,
                    level
            );

        if (glitchTicks > 0)
            renderGlitch(
                    context,
                    width,
                    height,
                    level
            );

        if (distortionTicks > 0)
            renderWorldDistortion(
                    context,
                    width,
                    height
            );

        if (eyesTicks > 0)
            renderEyes(
                    context,
                    width,
                    height
            );

        if (peripheralTicks > 0)
            renderPeripheral(
                    context,
                    width,
                    height
            );

        if (tabIllusionTicks > 0 &&
                tabIllusion != null)
            renderTabIllusion(
                    context,
                    width,
                    height
            );

        if (dontMoveActive)
            renderDontMove(
                    context,
                    width,
                    height
            );

        // FIND US OVERLAY
        if (FindUsEvent.isActive())
            FindUsEvent.renderOverlay(
                    context,
                    tickCounter
            );

        // SIGN ILLUSION OVERLAY
        if (SignIllusionEvent.isActive())
            SignIllusionEvent.renderOverlay(
                    context,
                    tickCounter
            );
    }

    // DON'T MOVE RENDER
    private static void renderDontMove(
            DrawContext context,
            int width,
            int height
    ) {

        int alpha =
                35 +
                        RANDOM.nextInt(35);

        context.fill(
                0,
                0,
                width,
                height,
                (alpha << 24) |
                        0x000000
        );

        MinecraftClient client =
                MinecraftClient.getInstance();

        var textRenderer =
                client.textRenderer;

        Text text =
                Text.literal("DON'T MOVE")
                        .formatted(
                                Formatting.DARK_RED,
                                Formatting.BOLD
                        );

        int textWidth =
                textRenderer.getWidth(text);

        int x =
                (width - textWidth) / 2;

        int y =
                height / 2 - 12;

        int color =
                RANDOM.nextInt(100) < 12
                        ? 0xFFFFFFFF
                        : 0xFFAA0000;

        context.drawText(
                textRenderer,
                text,
                x,
                y,
                color,
                true
        );
    }

    // WORLD DISTORTION
    private static void renderWorldDistortion(
            DrawContext context,
            int width,
            int height
    ) {

        int alpha =
                12 +
                        RANDOM.nextInt(32);

        context.fill(
                0,
                0,
                width,
                height,
                (alpha << 24) |
                        0x220000
        );

        int strips =
                3 +
                        RANDOM.nextInt(7);

        for (int i = 0;
             i < strips;
             i++) {

            int y =
                    RANDOM.nextInt(
                            Math.max(
                                    1,
                                    height
                            )
                    );

            int h =
                    1 +
                            RANDOM.nextInt(5);

            int c =
                    RANDOM.nextBoolean()
                            ? 0x330000
                            : 0x101010;

            context.fill(
                    0,
                    y,
                    width,
                    Math.min(
                            height,
                            y + h
                    ),
                    (
                            18 +
                                    RANDOM.nextInt(45)
                                    << 24) |
                            c
            );
        }
    }

    // EYES
    private static void renderEyes(
            DrawContext context,
            int width,
            int height
    ) {

        int side =
                RANDOM.nextInt(4);

        int x =
                side == 0
                        ? 12 +
                                RANDOM.nextInt(
                                        Math.max(
                                                1,
                                                width / 5
                                        )
                                )
                        : side == 1
                        ? width -
                                35 -
                                RANDOM.nextInt(
                                        Math.max(
                                                1,
                                                width / 5
                                        )
                                )
                        : RANDOM.nextInt(
                                Math.max(
                                        1,
                                        width
                                )
                        );

        int y =
                side < 2
                        ? 45 +
                                RANDOM.nextInt(
                                        Math.max(
                                                1,
                                                height - 90
                                        )
                                )
                        : RANDOM.nextInt(
                                Math.max(
                                        1,
                                        height
                                )
                        );

        int gap =
                7 +
                        RANDOM.nextInt(8);

        int size =
                2 +
                        RANDOM.nextInt(3);

        int color =
                RANDOM.nextInt(100) < 75
                        ? 0xAA0000
                        : 0x080808;

        context.fill(
                x,
                y,
                x + size,
                y + size,
                0xD0000000 |
                        color
        );

        context.fill(
                x + gap,
                y,
                x + gap + size,
                y + size,
                0xD0000000 |
                        color
        );

        if (RANDOM.nextInt(100) < 30) {

            context.fill(
                    x + 1,
                    y + size + 2,
                    x + gap + size - 1,
                    y + size + 3,
                    0x88000000
            );
        }
    }

    // PERIPHERAL
    private static void renderPeripheral(
            DrawContext context,
            int width,
            int height
    ) {

        int side =
                RANDOM.nextBoolean()
                        ? 0
                        : 1;

        int x =
                side == 0
                        ? 0
                        : width -
                                8 -
                                RANDOM.nextInt(24);

        int y =
                20 +
                        RANDOM.nextInt(
                                Math.max(
                                        1,
                                        height - 40
                                )
                        );

        int w =
                3 +
                        RANDOM.nextInt(12);

        int h =
                20 +
                        RANDOM.nextInt(70);

        context.fill(
                x,
                y,
                Math.min(
                        width,
                        x + w
                ),
                Math.min(
                        height,
                        y + h
                ),
                0x7A000000
        );

        if (RANDOM.nextBoolean()) {

            context.fill(
                    Math.min(
                            width - 1,
                            x + w
                    ),
                    y + 6,
                    Math.min(
                            width,
                            x + w + 2
                    ),
                    Math.min(
                            height,
                            y + h - 4
                    ),
                    0xAA550000
            );
        }
    }

    // TAB ILLUSION RENDER
    private static void renderTabIllusion(
            DrawContext context,
            int width,
            int height
    ) {

        int x =
                8 +
                        RANDOM.nextInt(
                                Math.max(
                                        1,
                                        width / 4
                                )
                        );

        int y =
                height -
                        44 -
                        RANDOM.nextInt(50);

        context.fill(
                x - 4,
                y - 3,
                Math.min(
                        width - 4,
                        x + 260
                ),
                y + 18,
                0x66000000
        );

        context.drawText(
                MinecraftClient.getInstance()
                        .textRenderer,
                Text.literal(tabIllusion)
                        .formatted(
                                Formatting.DARK_RED
                        ),
                x,
                y,
                0xFFFFFFFF,
                true
        );
    }

    // GLITCH
    private static void renderGlitch(
            DrawContext context,
            int width,
            int height,
            int level
    ) {

        context.fill(
                0,
                0,
                width,
                height,
                0x12000000
        );

        int bars =
                8 +
                        RANDOM.nextInt(
                                18 +
                                        level * 7
                        );

        for (int i = 0;
             i < bars;
             i++) {

            int y =
                    RANDOM.nextInt(
                            Math.max(
                                    1,
                                    height
                            )
                    );

            int h =
                    1 +
                            RANDOM.nextInt(
                                    4 +
                                            Math.min(
                                                    5,
                                                    level
                                            )
                            );

            int alpha =
                    20 +
                            RANDOM.nextInt(110);

            int color =
                    switch (glitchStyle % 6) {
                        case 0 -> 0xFFFFFF;
                        case 1 -> 0xAA0000;
                        case 2 -> 0xFF2020;
                        case 3 -> 0x111111;
                        case 4 -> 0x770000;
                        default -> 0xCCCCCC;
                    };

            context.fill(
                    0,
                    y,
                    width,
                    Math.min(
                            height,
                            y + h
                    ),
                    (alpha << 24) |
                            color
            );
        }

        int blocks =
                18 +
                        RANDOM.nextInt(
                                42 +
                                        level * 20
                        );

        for (int i = 0;
             i < blocks;
             i++) {

            int x =
                    RANDOM.nextInt(
                            Math.max(
                                    1,
                                    width
                            )
                    );

            int y =
                    RANDOM.nextInt(
                            Math.max(
                                    1,
                                    height
                            )
                    );

            int w =
                    1 +
                            RANDOM.nextInt(
                                    20 +
                                            level * 12
                            );

            int h =
                    1 +
                            RANDOM.nextInt(7);

            int alpha =
                    12 +
                            RANDOM.nextInt(105);

            int color =
                    RANDOM.nextInt(100) < 72
                            ? 0x550000
                            : 0x111111;

            context.fill(
                    x,
                    y,
                    Math.min(
                            width,
                            x + w
                    ),
                    Math.min(
                            height,
                            y + h
                    ),
                    (alpha << 24) |
                            color
            );
        }

        if (glitchStyle == 2 ||
                glitchStyle == 4) {

            context.fill(
                    RANDOM.nextInt(
                            Math.max(
                                    1,
                                    width / 2
                            )
                    ),
                    0,
                    width,
                    height,
                    0x08000000
            );
        }
    }

    // VHS
    private static void renderVhs(
            DrawContext context,
            int width,
            int height,
            int level
    ) {

        context.fill(
                0,
                0,
                width,
                height,
                0x12000000
        );

        for (int y = 0;
             y < height;
             y += 3) {

            context.fill(
                    0,
                    y,
                    width,
                    Math.min(
                            height,
                            y + 1
                    ),
                    0x18000000
            );
        }

        int tears =
                3 +
                        RANDOM.nextInt(
                                4 +
                                        level
                        );

        for (int i = 0;
             i < tears;
             i++) {

            int y =
                    RANDOM.nextInt(
                            Math.max(
                                    1,
                                    height
                            )
                    );

            int h =
                    1 +
                            RANDOM.nextInt(9);

            int alpha =
                    18 +
                            RANDOM.nextInt(50);

            int color =
                    RANDOM.nextInt(100) < 70
                            ? 0x770000
                            : 0x111111;

            context.fill(
                    0,
                    y,
                    width,
                    Math.min(
                            height,
                            y + h
                    ),
                    (alpha << 24) |
                            color
            );
        }

        if (RANDOM.nextInt(100) < 35) {

            int offset =
                    2 +
                            RANDOM.nextInt(5);

            context.fill(
                    offset,
                    0,
                    Math.min(
                            width,
                            offset + 2
                    ),
                    height,
                    0x12000055
            );

            context.fill(
                    Math.max(
                            0,
                            width - offset - 2
                    ),
                    0,
                    Math.max(
                            0,
                            width - offset
                    ),
                    height,
                    0x12005500
            );
        }
    }

    // STATE HELPERS
    public static boolean isRunActive() {
        return runActive;
    }

    public static boolean isDontMoveActive() {
        return dontMoveActive;
    }
}
