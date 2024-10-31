package org.doodieman.randomgravity;

import lombok.Getter;
import net.minecraft.server.MinecraftServer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("RandomGravity");

	@Getter
	private static Main instance;
	@Getter
	private ModContainer mod;
	@Getter
	private MinecraftServer serverInstance;

	@Getter
	private Game game;

	@Override
	public void onInitialize(ModContainer mod) {
		instance = this;
		this.mod = mod;

		ServerLifecycleEvents.STARTING.register(server -> {
			this.serverInstance = server;
			this.game = new Game(server);
		});
	}


}
