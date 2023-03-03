package dev.kugge.fabricvelocityteleportation.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public class Messaging {

    private MinecraftServer server;

    public Messaging() {
    }

    private void requestWarp() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        ClientPlayNetworking.send(new Identifier("advancedportals:warp"), packetBuf);
    }
}
