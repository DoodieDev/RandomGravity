package org.doodieman.randomgravity.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.doodieman.randomgravity.Game;
import org.doodieman.randomgravity.Main;

public class GameCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
			LiteralArgumentBuilder.<ServerCommandSource>literal("game")

				.then(CommandManager.literal("start")
					.executes(context -> {
						Main.getInstance().getGame().setTimerActive(true);
						return 1;
					})
				)

				.then(CommandManager.literal("stop")
					.executes(context -> {
						Main.getInstance().getGame().setTimerActive(false);
						return 1;
					})
				)

				.then(CommandManager.literal("status")
					.executes(context -> {
						Game game = Main.getInstance().getGame();
						boolean timerActive = game.isTimerActive();
						int secondsLeft = game.getSecondsLeft();

						context.getSource().sendFeedback(() -> Text.literal("timerActive: "+timerActive+", secondsLeft: "+secondsLeft), false);
						return 1;
					})
				)

		);

	}

}
