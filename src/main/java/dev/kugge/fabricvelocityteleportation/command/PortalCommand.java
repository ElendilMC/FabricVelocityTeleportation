package dev.kugge.fabricvelocityteleportation.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.kugge.fabricvelocityteleportation.FabricVelocityTeleportation;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;import java.io.IOException;import java.util.logging.Logger;

public class PortalCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("portaladd")
                    .then(CommandManager.argument("pos1", BlockPosArgumentType.blockPos())
                    .then(CommandManager.argument("pos2", BlockPosArgumentType.blockPos())
                    .then(CommandManager.argument("name", StringArgumentType.word())
                    .executes(PortalCommand::portalAdd)))));
            dispatcher.register(CommandManager.literal("portaldel")
                    .then(CommandManager.argument("pos1", BlockPosArgumentType.blockPos())
                    .then(CommandManager.argument("pos2", BlockPosArgumentType.blockPos())
                    .executes(PortalCommand::portalDel))));
            });
    }

	public static int portalAdd(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		BlockPos pos1 = BlockPosArgumentType.getBlockPos(context, "pos1");
        BlockPos pos2 = BlockPosArgumentType.getBlockPos(context, "pos2");
        String name = StringArgumentType.getString(context, "name");
        FabricVelocityTeleportation.database.add(pos1, pos2, name);
        try {
            FabricVelocityTeleportation.database.save();
        } catch (IOException e) {
            FabricVelocityTeleportation.LOGGER.error("ERROR CANNOT SAVE DATABASE!");
        }
		context.getSource().sendMessage(Text.of("Sucessfully set portal to " + name));
		return 1;
	}

	public static int portalDel(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		BlockPos pos1 = BlockPosArgumentType.getBlockPos(context, "pos1");
        BlockPos pos2 = BlockPosArgumentType.getBlockPos(context, "pos2");
        FabricVelocityTeleportation.database.del(pos1, pos2);
        try {
            FabricVelocityTeleportation.database.save();
        } catch (IOException e) {
            FabricVelocityTeleportation.LOGGER.error("ERROR CANNOT SAVE DATABASE!");
        }
		context.getSource().sendMessage(Text.of("Sucessfully deleted portal"));
		return 1;
	}
}
