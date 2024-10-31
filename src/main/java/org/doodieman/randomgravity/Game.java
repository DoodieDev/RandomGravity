package org.doodieman.randomgravity;

import lombok.Setter;
import net.minecraft.server.MinecraftServer;
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents;

public class Game {

	long tickCounter = 0L;
	@Setter
	int secondsLeft = 0;

	final MinecraftServer server;

	public Game(MinecraftServer server) {
		this.server = server;
		ServerTickEvents.START.register(this::onTick);
	}

	public void onTimerFinish() {

	}

	public void onTick(MinecraftServer server) {
		tickCounter++;
		if (tickCounter >= 20L) {
			tickCounter = 0L;

			//Runs every second
			if (this.secondsLeft == 0) {
				this.onTimerFinish();
			}
			this.secondsLeft--;
		}
	}



}
