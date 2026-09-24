package ru.whispershadow;

import net.minecraft.entity.MovementType;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.util.math.Vec3d;

/**
 * A purely client-side copy of a player using the player's own GameProfile/skin.
 */
public final class ShadowEntity extends OtherClientPlayerEntity {

    private final boolean delayedMotion;
    private Vec3d lastPlayerPos;

    public ShadowEntity(
            ClientWorld world,
            GameProfile profile,
            boolean delayedMotion
    ) {
        super(world, profile);

        this.delayedMotion = delayedMotion;
        this.noClip = true;
        this.setNoGravity(true);
        this.lastPlayerPos = getEntityPos();
    }

    public void horrorTick(ClientPlayerEntity player) {
        Vec3d target = player.getEyePos();

        if (delayedMotion) {

            // Doppelganger copies the player's movement
            // with a small delay.
            Vec3d currentPlayer =
                    player.getEntityPos();

            Vec3d old =
                    lastPlayerPos;

            lastPlayerPos =
                    currentPlayer;

            double dx =
                    old.x - getX();

            double dz =
                    old.z - getZ();

            if (Math.abs(dx) + Math.abs(dz) > 0.02) {

                setPos(
                        getX() + dx * 0.12,
                        getY() + (old.y - getY()) * 0.08,
                        getZ() + dz * 0.12
                );
            }
        }

        lookAt(
                EntityAnchorArgumentType.EntityAnchor.EYES,
                target
        );
    }

    /**
     * Dedicated movement for the RUN event.
     *
     * Unlike horrorTick(), this actually moves the Shadow
     * toward the player.
     */
    public void chasePlayer(ClientPlayerEntity player) {

    Vec3d target =
            player.getEntityPos();

    double dx =
            target.x - getX();

    double dz =
            target.z - getZ();

    double distance =
            Math.sqrt(dx * dx + dz * dz);

    if (distance > 0.01) {

        double speed = 0.105;

        setVelocity(
                (dx / distance) * speed,
                (target.y - getY()) * 0.08,
                (dz / distance) * speed
        );

        move(
                MovementType.SELF,
                getVelocity()
        );

        setVelocity(0.0, 0.0, 0.0);
    }

    lookAt(
            EntityAnchorArgumentType.EntityAnchor.EYES,
            player.getEyePos()
    );
}
