package dev.kugge.fabricvelocityteleportation.mixin;

import com.mojang.authlib.GameProfile;
import dev.kugge.fabricvelocityteleportation.FabricVelocityTeleportation;
import dev.kugge.fabricvelocityteleportation.message.Messaging;
import dev.kugge.fabricvelocityteleportation.util.Destination;
import dev.kugge.fabricvelocityteleportation.util.Range;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public boolean isInPortal = true;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo ci) {
        BlockPos pos = this.getBlockPos();
        boolean stuck = false;
        // Check if inside portal
        for (Range range: FabricVelocityTeleportation.database.data.keySet()) {
            if (range.isInsideRange(pos)) {
                if (this.isInPortal) {
                    stuck = true;
                    continue;
                }
                this.isInPortal = true;
                stuck = true;
                Destination destination = FabricVelocityTeleportation.database.data.get(range);
                Messaging.requestWarp((ServerPlayerEntity) (Object) this, destination.serverName, destination.destinationName);
            }
        }
        if (this.isInPortal && !stuck) this.isInPortal = false;
	}
}