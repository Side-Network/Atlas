package cc.funkemunky.api.utils.world.blocks;

import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.utils.world.CollisionBox;
import cc.funkemunky.api.utils.world.types.CollisionFactory;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;

public class TrapDoorHandler implements CollisionFactory {
    @Override
    public CollisionBox fetch(ProtocolVersion version, Block block) {
        double var2 = 0.1875;

        if (!((Openable) block.getBlockData()).isOpen()) {
            if (((Bisected) block.getBlockData()).getHalf() == Bisected.Half.TOP) {
                return new SimpleCollisionBox(0.0, 1.0 - var2, 0.0, 1.0, 1.0, 1.0);
            } else {
                return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, var2, 1.0);
            }
        }

        switch (((Directional) block.getBlockData()).getFacing()) {
            case NORTH:
                return new SimpleCollisionBox(0.0, 0.0, 1.0 - var2, 1.0, 1.0, 1.0);

            case SOUTH:
                return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, var2);

            case WEST:
                return new SimpleCollisionBox(1.0 - var2, 0.0, 0.0, 1.0, 1.0, 1.0);

            case EAST:
                return new SimpleCollisionBox(0.0, 0.0, 0.0, var2, 1.0, 1.0);
        }

        return null;
    }
}
