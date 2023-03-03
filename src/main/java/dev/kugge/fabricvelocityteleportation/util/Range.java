package dev.kugge.fabricvelocityteleportation.util;

import net.minecraft.util.math.BlockPos;

public record Range(BlockPos x, BlockPos y) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range range)) return false;
        return (x.equals(range.x) && y.equals(range.y)) || (x.equals(range.y) && y.equals(range.x));
    }

    private boolean isInsideX(BlockPos pos) {
        return (this.y.getX() <= pos.getX() && pos.getX() <= this.x.getX())
            || (this.x.getX() <= pos.getX() && pos.getX() <= this.y.getX());
    }

    private boolean isInsideY(BlockPos pos) {
        return (this.y.getY() <= pos.getY() && pos.getY() <= this.x.getY())
            || (this.x.getY() <= pos.getY() && pos.getY() <= this.y.getY());
    }

    private boolean isInsideZ(BlockPos pos) {
        return (this.y.getZ() <= pos.getZ() && pos.getZ() <= this.x.getZ())
            || (this.x.getZ() <= pos.getZ() && pos.getZ() <= this.y.getZ());
    }

    public boolean isInsideRange(BlockPos pos) {
        return isInsideX(pos) && isInsideY(pos) && isInsideZ(pos);
    }
}
