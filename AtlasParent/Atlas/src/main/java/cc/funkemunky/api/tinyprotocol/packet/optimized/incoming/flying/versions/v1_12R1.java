package cc.funkemunky.api.tinyprotocol.packet.optimized.incoming.flying.versions;

import cc.funkemunky.api.tinyprotocol.packet.optimized.incoming.flying.AtlasPacketPlayInFlying;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;

@Deprecated
public class v1_12R1 extends AtlasPacketPlayInFlying {

    public v1_12R1(Object packet) {
        super(packet);
    }

    @Override
    public double getX() {
        return getFlying().a(0D);
    }

    @Override
    public double getY() {
        return getFlying().b(0D);
    }

    @Override
    public double getZ() {
        return getFlying().c(0D);
    }

    @Override
    public float getYaw() {
        return getFlying().a(0f);
    }

    @Override
    public float getPitch() {
        return getFlying().b(0f);
    }

    @Override
    public boolean isOnGround() {
        return getFlying().a();
    }

    @Override
    public boolean isPos() {
        return getFlying() instanceof PacketPlayInFlying.PacketPlayInPositionLook
                || getFlying() instanceof PacketPlayInFlying.PacketPlayInPosition;
    }

    @Override
    public boolean isLook() {
        return getFlying() instanceof PacketPlayInFlying.PacketPlayInLook
                || getFlying() instanceof PacketPlayInFlying.PacketPlayInPositionLook;
    }

    private PacketPlayInFlying getFlying() {
        return (PacketPlayInFlying) packet;
    }
}
