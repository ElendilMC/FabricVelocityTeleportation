package dev.kugge.fabricvelocityteleportation.message;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.Unpooled;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class Messaging {
    public static void requestWarp(String uuid, String serverName, String dest) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PortalEnter"); // subchannel
        out.writeUTF(serverName);
        out.writeUTF(dest);
        out.writeUTF(uuid); // UUID
        PacketByteBuf pbb = new PacketByteBuf(Unpooled.copiedBuffer(out.toByteArray()));
        ClientPlayNetworking.send(new Identifier("velocityteleport:warp"), pbb);
    }
}
