package org.doodieman.randomgravity;

import com.fusionflux.gravity_api.api.GravityChangerAPI;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

	long tickCounter = 0L;
	@Getter @Setter
	int secondsLeft = 30;
	@Getter @Setter
	boolean timerActive = false;

	final MinecraftServer server;

	final List<Direction> directions = Arrays.asList(
		Direction.DOWN,
		Direction.UP,
		Direction.NORTH,
		Direction.SOUTH,
		Direction.EAST,
		Direction.WEST
	);

	final Map<UUID, Direction> currentDirections = new HashMap<>();
	final Map<UUID, Direction> nextDirections = new HashMap<>();

	public Game(MinecraftServer server) {
		this.server = server;
		ServerTickEvents.START.register(this::onTick);
	}

	//When the timer finishes. Do the random gravity.
	public void onTimerFinish() {

		for (ServerPlayerEntity player : this.getAllPlayers()) {
			Direction randomDirection = this.getNextDirection(player.getUuid());
			GravityChangerAPI.setDefaultGravityDirection(player, randomDirection);
			currentDirections.put(player.getUuid(), randomDirection);
			player.sendMessage(Text.literal("§6§lDIN TYNGDEKRAFT SKIFTEDE TIL §f§l"+randomDirection.asString().toUpperCase()+"§6§l!"), false);
			nextDirections.remove(player.getUuid());
		}
	}

	//Called every game tick
	public void onTick(MinecraftServer server) {
		if (!timerActive) return;
		tickCounter++;
		if (tickCounter >= 20L) {
			tickCounter = 0L;

			Main.LOGGER.info("Skifter om "+secondsLeft);

			for (ServerPlayerEntity player : this.getAllPlayers()) {
				if (player.getY() > 400) {
					player.kill();
				}
			}

			//Runs every second
			if (this.secondsLeft <= 10 && this.secondsLeft > 0) {
				this.broadcastMessage("§c§lTYNGDEKRAFTEN SKIFTER OM §f§l"+this.secondsLeft+" SEKUNDER§c§l!");

				//Show next direction in actionBar
				for (ServerPlayerEntity player : this.getAllPlayers()) {
					player.sendMessage(Text.literal("§fSkifter til: §6§l"+this.getNextDirection(player.getUuid()).asString().toUpperCase()), true);
				}
			}

			if (this.secondsLeft == 0) {
				this.onTimerFinish();
				this.secondsLeft = this.generateRandomTime();
			}

			this.secondsLeft--;
		}
	}


	public Direction getNextDirection(UUID uuid) {
		if (!this.nextDirections.containsKey(uuid)) {
			this.nextDirections.put(uuid, this.getRandomDirection());
		}
		return this.nextDirections.get(uuid);
	}
	public Direction getCurrentDirection(UUID uuid) {
		return this.currentDirections.getOrDefault(uuid, Direction.DOWN);
	}

	//Gets a random gravity direction
	public Direction getRandomDirection() {
		int random = ThreadLocalRandom.current().nextInt(this.directions.size());
		return this.directions.get(random);
	}
	//Broadcast message to all players
	public void broadcastMessage(String message) {
		this.server.getPlayerManager().broadcastSystemMessage(Text.literal(message), false);
	}
	//Generates random time
	public int generateRandomTime() {
		return ThreadLocalRandom.current().nextInt(30, 60);
	}
	//Get all online players
	public List<ServerPlayerEntity> getAllPlayers() {
		return this.server.getPlayerManager().getPlayerList();
	}


}
