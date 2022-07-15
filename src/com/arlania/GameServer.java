package com.arlania;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.arlania.engine.task.impl.ServerTimeUpdateTask;
import com.arlania.util.MACBanL;
import com.arlania.util.ShutdownHook;
import com.arlania.world.World;
import com.arlania.world.content.droptable.DropTablePreloading;

/**
 * The starting point of Arlania.
 * 
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

    private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
    private static final Logger logger = Logger.getLogger("Pwnlite");
    private static boolean updating;

    public static void main(String[] params) {
                
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        try {
            
            logger.info("Initializing the loader...");
            loader.init();
            loader.finish();
            logger.info("The loader has finished loading utility tasks.");
            ServerTimeUpdateTask.start_configuration_process();
            MACBanL.init();
            DropTablePreloading.loadPossibleNPCs();
            if (GameSettings.CONFIGURATION_TIME == 5) {
                logger.info("Configurations loaded.");
            }
            logger.info("Pwnlite is now online on IP: port " + GameSettings.GAME_PORT + "!");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not start | Pwnlite Program terminated.", ex);
            System.exit(1);
        }
    }

	public static GameLoader getLoader() {
		return loader;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setUpdating(boolean updating) {
		GameServer.updating = updating;
	}

	public static boolean isUpdating() {
		return GameServer.updating;
	}
}
