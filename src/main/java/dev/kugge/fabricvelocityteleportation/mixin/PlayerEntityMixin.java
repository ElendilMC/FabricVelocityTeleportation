package dev.kugge.fabricvelocityteleportation.mixin;

import dev.kugge.fabricvelocityteleportation.FabricVelocityTeleportation;
import dev.kugge.fabricvelocityteleportation.message.Messaging;
import dev.kugge.fabricvelocityteleportation.util.Destination;
import dev.kugge.fabricvelocityteleportation.util.Range;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    public boolean isStuckInPortal = true;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
	private void tickMovement(CallbackInfo info) {
        BlockPos pos = this.getBlockPos();
        boolean stillStuck = false;
        // Check if inside portal
        for (Range range: FabricVelocityTeleportation.database.data.keySet()) {
            if (range.isInsideRange(pos)) {
                if (this.isStuckInPortal) {
                    stillStuck = true;
                    continue;
                }
                Destination destination = FabricVelocityTeleportation.database.data.get(range);
                Messaging.requestWarp(this.uuidString, destination.serverName, destination.destinationName);
            }
        }
        if (this.isStuckInPortal && !stillStuck) this.isStuckInPortal = false;
	}
}