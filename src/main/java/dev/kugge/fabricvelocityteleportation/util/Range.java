package dev.kugge.fabricvelocityteleportation.util;

import net.minecraft.util.math.BlockPos;
import java.util.Objects;

public class Range {
    private final BlockPos x;
    private final BlockPos y;

    public Range(BlockPos x, BlockPos y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range range)) return false;
        return (x == range.x && y == range.y) || (x == range.y && y == range.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
