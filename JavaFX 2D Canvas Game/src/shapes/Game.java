/**
 * All initializations happen here
 */

package shapes;

import game.Platform;
import game.Player;
import game.Time;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {

	private static final int GROUND_LEVEL = 400;
	public static Player player = new Player(50, 0);

	// This list holds environment stuff (sun, sky, etc)
	private static List<Shado.Shape> environment = new ArrayList<>();

	// This list holds all monsters created
	private static List<Player> allMonsters = new ArrayList<>();

	public static void initialize(Canvas c) {

		// Draw ground
		var ground = new Platform(-10000, GROUND_LEVEL, (float) c.getWidth() + 20000, (float) c.getHeight() - GROUND_LEVEL);
		ground.setFill(Color.GREEN);
		ground.setStroke(Color.GREEN);
		ground.flagAsImmobile();

		// Draw sky
		var sky = new Shado.Rectangle(-1000, 0, (float) c.getWidth() * 2, GROUND_LEVEL);
		sky.setFill(Color.LIGHTBLUE);
		sky.setStroke(Color.LIGHTBLUE);

		var sun = new Shado.Circle(50, 50, 25);
		sun.setFill(Color.YELLOW);
		sun.setStroke(Color.ORANGE);

		environment.add(sky);
		environment.add(sun);

		// Add monsters
		for (int i = 0; i < 1; i++) {
			var monster = new Player(100 * i + 400, 1);
			monster.getShape().setFill(Color.PURPLE);
			allMonsters.add(monster);
		}

		// Add platforms
		for (int i = 0; i < 1; i++) {
			var platform = new Platform(150 * i + 200, 50 * i + 300, 100, 25);
			platform.setFill(Color.LIGHTGRAY);
		}
	}

	public static void render(GraphicsContext g) {
		// Render sky
		Game.environment.parallelStream()
				.forEachOrdered(e -> e.draw(g));

		// Render all platforms
		Platform.allPlatforms.parallelStream()
				.forEachOrdered(e -> e.draw(g));

		// Draw all monsters
		Game.allMonsters.parallelStream()
				.forEachOrdered(e -> {
					e.draw(g);
					e.update();
				});

		// Draw and update player
		player.draw(g);
		player.update();

	}

	public static void moveWorld(double amount) {
		// Move environment
		environment.parallelStream()
				.forEachOrdered(e -> {
					e.getPosition().x = e.getPosition().x + amount;
				});

		allMonsters.parallelStream()
				.forEachOrdered(e -> {
					e.move(amount * Time.deltaTime / 20, 0);
				});

		Platform.allPlatforms.parallelStream()
				.forEachOrdered(e -> {
					if (!e.isImmobile()) {
						e.move(amount * Time.deltaTime / 20, 0);
					}
				});
	}

	public static void handleEvents(Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case UP:
						player.jump();
						break;
					case DOWN:
						break;
					case LEFT:
						moveWorld(player.getMS());
						break;
					case RIGHT:
						moveWorld(-1 * player.getMS());
						break;
				}
			}
		});
	}
}
