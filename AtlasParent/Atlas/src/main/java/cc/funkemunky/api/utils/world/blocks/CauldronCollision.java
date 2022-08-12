package cc.funkemunky.api.utils.world.blocks;

import cc.funkemunky.api.utils.world.types.ComplexCollisionBox;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;

public class CauldronCollision extends ComplexCollisionBox {

    public CauldronCollision() {
        this.add(new SimpleCollisionBox(0,0,0,1, 0.125,1));
        double thickness = 0.125;
        this.add(new SimpleCollisionBox(0, 0.125, 0, thickness, 1, 1));
        this.add(new SimpleCollisionBox(1-thickness, 0.125, 0, 1, 1, 1));
        this.add(new SimpleCollisionBox(0, 0.125, 0, 1, 1, thickness));
        this.add(new SimpleCollisionBox(0, 0.125, 1-thickness, 1, 1, 1));
    }
}
