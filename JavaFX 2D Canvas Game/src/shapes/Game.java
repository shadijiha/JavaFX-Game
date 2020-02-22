/**
 * All initializations happen here
 */

package shapes;

import game.Bullet;
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

	private static int GROUND_LEVEL = 400;
	public static Player player = new Player(50, 0);

	// This list holds environment stuff (sun, sky, etc)
	private static List<Shado.Shape> environment = new ArrayList<>();

	// This list holds all monsters created
	private static List<Player> allMonsters = new ArrayList<>();

	public static void initialize(Canvas c) {

		GROUND_LEVEL = (int) (c.getHeight() * 0.70);

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

					// Update monster and draw it if they are not hidden
					if (!e.isHidden()) {
						e.draw(g);
						e.update();
						e.drawHealthBar(g);    // Draw all Health bars of monsters
					}

					// If monster has less or equal to 0 Hp, Hide it
					if (e.isDead())
						e.hide();

					// Draw all bullets of monsters
					e.getAllBullets().parallelStream()
							.forEachOrdered(b -> {
								b.update();
								b.draw(g);
							});
				});

		// Draw and update player
		player.draw(g);
		player.update();

		// Draw all player's bullets and detect collision with Monsters
		player.getAllBullets().parallelStream()
				.forEachOrdered(b -> {
					// Update and draw each bullet
					b.update();
					b.draw(g);

					// Detect if bullet has exceeded range of player
					if (b.getPosition().getDistance(player.getPosition()) > player.getRange())
						b.setActivityTo(false);

					// Detect collision with monsters
					allMonsters.parallelStream()
							.forEachOrdered(monster -> {

								// If a bullet shot by player collides a monster:
								// Applies only if bullet is active and monster is not hidden
								// Damage the monster and set the bullet to be inactive (to delete it later)
								if (b.isActive() && !monster.isHidden() && monster.getShape().collides(b.getShape())) {
									monster.damage(player);
									b.setActivityTo(false);
								}
							});

				});

		// Go over all the bullets of the player and delete all inactive once
		List<Bullet> player_bullets = player.getAllBullets();
		for (int i = player_bullets.size() - 1; i >= 0; i--) {
			if (!player_bullets.get(i).isActive()) {
				player_bullets.remove(i);
			}
		}

		// Go over all the bullets of all Monsters and delete all inactive once
		allMonsters.parallelStream()
				.forEachOrdered(monster -> {
					List<Bullet> monster_bullets = monster.getAllBullets();
					for (int i = monster_bullets.size() - 1; i >= 0; i--) {
						if (!monster_bullets.get(i).isActive()) {
							monster_bullets.remove(i);
						}
					}
				});

	}

	public static void moveWorld(double amount) {
		// Move environment
		environment.parallelStream()
				.forEachOrdered(e -> {
					e.getPosition().x = e.getPosition().x + amount;
				});

		// Move monsters and their bullets
		allMonsters.parallelStream()
				.forEachOrdered(e -> {
					e.move(amount * Time.deltaTime / 20, 0);

					e.getAllBullets().parallelStream()
							.forEachOrdered(bullet -> bullet.move(amount * Time.deltaTime / 20, 0));
				});

		// Move all platforms
		Platform.allPlatforms.parallelStream()
				.forEachOrdered(e -> {
					if (!e.isImmobile()) {
						e.move(amount * Time.deltaTime / 20, 0);
					}
				});

		// Move all bullets of player
		player.getAllBullets().parallelStream()
				.forEachOrdered(e -> e.move(amount * Time.deltaTime / 20, 0));
	}

	public static void handleEvents(Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case W:
						player.jump();
						break;
					case S:
						break;
					case A:
						moveWorld(player.getMS());
						break;
					case D:
						moveWorld(-1 * player.getMS());
						break;
					case SPACE:
						player.shoot();
						break;
				}
			}
		});
	}
}
