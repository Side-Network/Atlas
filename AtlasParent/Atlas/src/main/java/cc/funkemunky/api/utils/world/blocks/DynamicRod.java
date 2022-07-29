package cc.funkemunky.api.utils.world.blocks;

import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.utils.world.CollisionBox;
import cc.funkemunky.api.utils.world.types.CollisionFactory;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;

@SuppressWarnings("Duplicates")
public class DynamicRod implements CollisionFactory {

    public static final CollisionBox UD = new SimpleCollisionBox(0.4375,0, 0.4375, 0.5625, 1, 0.625);
    public static final CollisionBox EW = new SimpleCollisionBox(0,0.4375, 0.4375, 1, 0.5625, 0.625);
    public static final CollisionBox NS = new SimpleCollisionBox(0.4375, 0.4375, 0, 0.5625, 0.625, 1);

    @Override
    public CollisionBox fetch(ProtocolVersion version, Block b) {
        if (b.getBlockData() instanceof Directional)
            switch (((Directional) b.getBlockData()).getFacing()) {
                case UP:
                case DOWN:
                default:
                    return UD.copy();
                case NORTH:
                case SOUTH:
                    return NS.copy();
                case EAST:
                case WEST:
                    return EW.copy();
            }
        else
            switch (((Orientable) b.getBlockData()).getAxis()) {
                case X:
                    return EW.copy();
                case Y:
                    return UD.copy();
                case Z:
                    return NS.copy();
            }

        return null;
    }

}
