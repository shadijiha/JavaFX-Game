/**
 * All initializations happen here
 */

package shapes;

import game.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import shadoMath.Util;
import shadoMath.Vertex;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {

	// What resolution the game was made in (to calculate scaling in Shado.Shape)
	public static final int WIDTH_RESOLUTION_ON_DESIGN = 1920;
	public static final int HEIGHT_RESOLUTION_ON_DESIGN = 1080;

	// ==========================================
	private static int GROUND_LEVEL = 400;
	public static Player player = new Player(50, 0, "DataFiles/playerInfo.son");

	private static Player selectedHUD = player;
	private static boolean show_selected_HUD_range = false;

	// This list holds environment stuff (sun, sky, etc)
	private static List<Shado.Shape> environment;

	// This list holds all monsters created
	private static List<Player> allMonsters;

	// This Rectangle is the description box for whenever user hovers over something that needs explanation
	private static final Shado.Rectangle DESCRIPTION_BOX = new Shado.Rectangle();
	private static final Shado.Text DESCRIPTION_TEXT = new Shado.Text();

	public static void initialize(Canvas c) {

		environment = new ArrayList<>();
		allMonsters = new ArrayList<>();
		Platform.allPlatforms = new ArrayList<>();

		GROUND_LEVEL = (int) (c.getHeight() * 0.70);
		player.setPosition(new Vertex(c.getWidth() * 0.20, 0));
		player.setTexture("DataFiles/Images/player.png");
		player.onClick(e -> selectedHUD = (Player) e);

		// Draw ground
		var ground = new Platform(-5000, GROUND_LEVEL, (float) c.getWidth() + 10000, (float) c.getHeight() - GROUND_LEVEL);
		ground.setFill(Color.GREEN);
		ground.setStroke(Color.GREEN);
		ground.flagAsImmobile();
		ground.setTexture("DataFiles/Images/grass.png");

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
		for (int i = 0; i < 2; i++) {
			var monster = new Player(200 * i + 500, 1, "DataFiles/monster.son");
			monster.getShape().setFill(Color.PURPLE);
			monster.setTexture("DataFiles/Images/monster.png");
			monster.onClick(e -> selectedHUD = (Player) e);
			allMonsters.add(monster);
		}

		// Add platforms
		for (int i = 0; i < 1; i++) {
			var platform = new Platform(150 * i + 400, 50 * i + 300, 100, 25);
			platform.setFill(Color.LIGHTGRAY);
			platform.setTexture("DataFiles/Images/platforme.png");
		}
	}

	public static void render(GraphicsContext g, Canvas c) {

		// Delete all inwanted elements
		Game.deleteElements();

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

		// Draw all infos in the game
		Info.allInfos.parallelStream()
				.filter(info -> !info.isHidden())
				.forEachOrdered(info -> info.draw(g));

		// Draw Player HUD
		selectedHUD.drawHUD(g, c);
		if (show_selected_HUD_range)
			selectedHUD.drawRange(g);

		// Show description box
		//DESCRIPTION_BOX.draw(g);
		//DESCRIPTION_TEXT.draw(g);
	}

	public static void deleteElements() {

		// Go over all the bullets of the player and delete all inactive once
		Util.delete_if(player.getAllBullets(), bullet -> !bullet.isActive());

		// Go over all the bullets of all Monsters and delete all inactive once
		allMonsters.parallelStream()
				.forEachOrdered(monster -> {
					Util.delete_if(monster.getAllBullets(), bullet -> !bullet.isActive());
				});

		// Go over all deleted info and delete them
		Util.delete_if(Info.allInfos, GameObject::isDeleted);

	}

	public static void showDescription(Shado.Shape element, String text) throws CodeNotImplementedException {
		// TODO: implement code
		throw new CodeNotImplementedException("?XD");
	}

	public static void moveWorld(double amount) {
		final double final_amount = amount * Timer.deltaTime / 10;
		// Move environment
		environment.parallelStream()
				.forEachOrdered(e -> {
					e.move(final_amount * 0.15, 0);
				});

		// Move monsters and their bullets
		allMonsters.parallelStream()
				.forEachOrdered(e -> {
					e.move(final_amount, 0);

					e.getAllBullets().parallelStream()
							.forEachOrdered(bullet -> bullet.move(final_amount, 0));
				});

		// Move all platforms
		Platform.allPlatforms.parallelStream()
				.forEachOrdered(e -> {
					if (!e.isImmobile()) {
						e.move(final_amount, 0);
					}
				});

		// Move all bullets of player
		player.getAllBullets().parallelStream()
				.forEachOrdered(bullet -> {
					bullet.move(final_amount, 0);
				});
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
					case C:
						show_selected_HUD_range = true;
						break;
					case SPACE:
						player.shoot();
						break;
				}
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case C:
						show_selected_HUD_range = false;
						break;
				}
			}
		});
	}
}
