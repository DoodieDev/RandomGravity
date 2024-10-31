package org.doodieman.randomgravity.events;

import net.minecraft.server.network.ServerPlayerEntity;
import org.doodieman.randomgravity.Game;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;

public class PlayerEvents {

	public static void registerEvents(Game game) {

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
		});

	}

}
