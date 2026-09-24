package ru.whispershadow;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

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

        this.lastPlayerPos =
                getEntityPos();
    }

    public void horrorTick(
            ClientPlayerEntity player
    ) {

        Vec3d target =
                player.getEyePos();

        if (delayedMotion) {

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

            if (Math.abs(dx) +
                    Math.abs(dz) > 0.02) {

                setPos(
                        getX() + dx * 0.12,
                        getY() +
                                (old.y - getY()) * 0.08,
                        getZ() + dz * 0.12
                );
            }
        }

        lookAt(
                EntityAnchorArgumentType.EntityAnchor.EYES,
                target
        );
    }

    public void chasePlayer(
            ClientPlayerEntity player
    ) {

        Vec3d target =
                player.getEntityPos();

        double dx =
                target.x - getX();

        double dz =
                target.z - getZ();

        double distance =
                Math.sqrt(
                        dx * dx +
                                dz * dz
                );

        if (distance > 0.01) {

            double speed = 0.105;

            double velocityX =
                    (dx / distance) * speed;

            double velocityY =
                    (target.y - getY()) * 0.08;

            double velocityZ =
                    (dz / distance) * speed;

            setVelocity(
                    velocityX,
                    velocityY,
                    velocityZ
            );

            move(
                    MovementType.SELF,
                    getVelocity()
            );

            setVelocity(
                    Vec3d.ZERO
            );
        }

        lookAt(
                EntityAnchorArgumentType.EntityAnchor.EYES,
                player.getEyePos()
        );
    }
}
