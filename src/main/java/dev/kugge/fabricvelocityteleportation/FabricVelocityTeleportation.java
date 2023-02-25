package dev.kugge.fabricvelocityteleportation;

import dev.kugge.fabricvelocityteleportation.command.PortalCommand;
import dev.kugge.fabricvelocityteleportation.util.Database;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class FabricVelocityTeleportation implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("fabricvelocityteleportation");
    public static Database database;

	@Override
	public void onInitialize() {
        try {
            database = new Database("config/fabric-velocity-teleport.json");
            database.load();
        } catch (IOException e) {
            LOGGER.error("Cannot load FabricVelocityTeleportation database!");
            throw new RuntimeException(e);
        }

		PortalCommand.register();

		LOGGER.info("Sucessfully launched FabricVelocityTeleportation");
	}
}
