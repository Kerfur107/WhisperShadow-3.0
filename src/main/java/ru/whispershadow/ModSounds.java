package ru.whispershadow;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class ModSounds {
    public static final Identifier WHISPER_ID =
            Identifier.of(WhisperShadowClient.MOD_ID, "whisper");

    public static final Identifier STATIC_ID =
            Identifier.of(WhisperShadowClient.MOD_ID, "static");

    public static final Identifier GLITCH_ID =
            Identifier.of(WhisperShadowClient.MOD_ID, "glitch");

    public static final Identifier VHS_ID =
            Identifier.of(WhisperShadowClient.MOD_ID, "vhs");

    public static final Identifier FOOTSTEPS_ID =
            Identifier.of(WhisperShadowClient.MOD_ID, "footsteps");

    public static final Identifier CIRCUIT_CHASE_ID =
            Identifier.of(WhisperShadowClient.MOD_ID, "circuit_chase");


    public static final SoundEvent WHISPER =
            SoundEvent.of(WHISPER_ID);

    public static final SoundEvent STATIC =
            SoundEvent.of(STATIC_ID);

    public static final SoundEvent GLITCH =
            SoundEvent.of(GLITCH_ID);

    public static final SoundEvent VHS =
            SoundEvent.of(VHS_ID);

    public static final SoundEvent FOOTSTEPS =
            SoundEvent.of(FOOTSTEPS_ID);

    public static final SoundEvent CIRCUIT_CHASE =
            SoundEvent.of(CIRCUIT_CHASE_ID);


    private ModSounds() {}

    public static void init() {
        Registry.register(
                Registries.SOUND_EVENT,
                WHISPER_ID,
                WHISPER
        );

        Registry.register(
                Registries.SOUND_EVENT,
                STATIC_ID,
                STATIC
        );

        Registry.register(
                Registries.SOUND_EVENT,
                GLITCH_ID,
                GLITCH
        );

        Registry.register(
                Registries.SOUND_EVENT,
                VHS_ID,
                VHS
        );

        Registry.register(
                Registries.SOUND_EVENT,
                FOOTSTEPS_ID,
                FOOTSTEPS
        );

        Registry.register(
                Registries.SOUND_EVENT,
                CIRCUIT_CHASE_ID,
                CIRCUIT_CHASE
        );
    }
}
