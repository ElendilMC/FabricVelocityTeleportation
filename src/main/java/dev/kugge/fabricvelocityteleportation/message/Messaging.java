package dev.kugge.fabricvelocityteleportation.message;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.Unpooled;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class Messaging {
    public static void requestWarp(ServerPlayerEntity player, String serverName, String dest) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PortalEnter"); // subchannel
        out.writeUTF(serverName);
        out.writeUTF(dest);
        out.writeUTF(player.getUuidAsString()); // UUID
        PacketByteBuf pbb = new PacketByteBuf(Unpooled.copiedBuffer(out.toByteArray()));
        new Thread(() -> ServerPlayNetworking.send(player, new Identifier("advancedportals:warp"), pbb)).start();
    }
}
