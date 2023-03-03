package dev.kugge.fabricvelocityteleportation.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.kugge.fabricvelocityteleportation.FabricVelocityTeleportation;
import dev.kugge.fabricvelocityteleportation.util.Destination;import dev.kugge.fabricvelocityteleportation.util.Range;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import java.io.IOException;

public class PortalCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("portaladd")
                    .then(CommandManager.argument("portal corner 1", BlockPosArgumentType.blockPos())
                    .then(CommandManager.argument("portal corner 2", BlockPosArgumentType.blockPos())
                    .then(CommandManager.argument("server name", StringArgumentType.word())
                    .then(CommandManager.argument("destination", StringArgumentType.word())
                    .executes(PortalCommand::portalAdd))))));
            dispatcher.register(CommandManager.literal("portaldel")
                    .then(CommandManager.argument("portal corner 1", BlockPosArgumentType.blockPos())
                    .then(CommandManager.argument("portal corner 2", BlockPosArgumentType.blockPos())
                    .executes(PortalCommand::portalDel))));
            dispatcher.register(CommandManager.literal("portallist").executes(PortalCommand::portalList));
            });
    }

	public static int portalAdd(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		BlockPos pos1 = BlockPosArgumentType.getBlockPos(context, "portal corner 1");
        BlockPos pos2 = BlockPosArgumentType.getBlockPos(context, "portal corner 2");
        String name = StringArgumentType.getString(context, "server name");
        String dest = StringArgumentType.getString(context, "destination");
        try {
            FabricVelocityTeleportation.database.add(pos1, pos2, name, dest);
        } catch (IllegalArgumentException e) {
            context.getSource().sendMessage(Text.of("Portal already exists!"));
            return 1;
        }
        try {
            FabricVelocityTeleportation.database.save();
        } catch (IOException e) {
            FabricVelocityTeleportation.LOGGER.error("ERROR CANNOT SAVE DATABASE!");
        }
		context.getSource().sendMessage(Text.of("Successfully set portal to " + name));
		return 1;
	}

	public static int portalDel(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		BlockPos pos1 = BlockPosArgumentType.getBlockPos(context, "portal corner 1");
        BlockPos pos2 = BlockPosArgumentType.getBlockPos(context, "portal corner 2");
        try {
            FabricVelocityTeleportation.database.del(pos1, pos2);
        } catch (IllegalArgumentException e) {
            context.getSource().sendMessage(Text.of("Cannot delete portal ! Portal does not exists. Use /portallist"));
        }
        try {
            FabricVelocityTeleportation.database.save();
        } catch (IOException e) {
            FabricVelocityTeleportation.LOGGER.error("ERROR CANNOT SAVE DATABASE!");
        }
		context.getSource().sendMessage(Text.of("Successfully deleted portal"));
		return 1;
	}

    public static int portalList(CommandContext<ServerCommandSource> context) {
        int i = 0;
        for (Range range: FabricVelocityTeleportation.database.data.keySet()) {
            Destination dest = FabricVelocityTeleportation.database.data.get(range);
            context.getSource().sendMessage(Text.of("Portal " + i + " to server " + dest.serverName + " (" + dest.destinationName + ")"));
            context.getSource().sendMessage(Text.of("Pos1 (X: " + range.x.getX() + " Y: " + range.x.getY() + " Z: " + range.x.getZ()));
            context.getSource().sendMessage(Text.of("Pos1 (X: " + range.x.getX() + " Y: " + range.x.getY() + " Z: " + range.x.getZ()));
            context.getSource().sendMessage(Text.of("Pos2 (X: " + range.y.getX() + " Y: " + range.y.getY() + " Z: " + range.y.getZ()));
            context.getSource().sendMessage(Text.of("Pos2 (X: " + range.y.getX() + " Y: " + range.y.getY() + " Z: " + range.y.getZ()));
            i++;
        }
        return 1;
    }
}
