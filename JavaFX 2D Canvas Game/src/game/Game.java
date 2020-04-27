/**
 * All initializations happen here
 */

package game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import shadoMath.Util;
import shadoMath.Vertex;
import shapes.Shado;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {

	// What resolution the game was made in (to calculate scaling in Shado.Shape)
	public static final Shado.Rectangle DESIGN_RESOLUTION = new Shado.Rectangle(0, 0, 1920.0, 1080.0);

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

		GROUND_LEVEL = (int) ((c.getHeight() * 0.70) * scaleRatio(c));
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
			monster.onMouseOver(e -> ((Player) e).shoot());
			allMonsters.add(monster);
		}

		// Add platforms
		for (int i = 0; i < 1; i++) {
			var platform = new Platform(150 * i + 400, 50 * i + 300, 100, 25);
			platform.setFill(Color.LIGHTGRAY);
			platform.setTexture("DataFiles/Images/platforme.png");
		}
	}

	/**
	 * Renders all game components to the screen
	 *
	 * @param g The canvas on which to render
	 */
	public static void render(Canvas g) {

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
				.forEachOrdered(monster -> {

					// Update monster and draw it if they are not hidden
					if (!monster.isHidden()) {
						monster.draw(g);
						monster.update();
						monster.drawHealthBar(g);    // Draw all Health bars of monsters
					}

					// If monster has less or equal to 0 Hp, Hide it
					if (monster.isDead())
						monster.hide();

					// Draw all bullets of monsters
					monster.getAllBullets().parallelStream()
							.forEachOrdered(b -> {
								b.update();
								b.draw(g);

								// Detect if bullet has exceeded monster's max range
								if (b.getPosition().getDistance(b.getInitialPosition()) > monster.getRange())
									b.delete();

								// Detect collision with the player and apply damage to the player
								if (b.getShape().collides(player.getShape())) {
									player.damage(monster);
									b.delete();
								}
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
					if (b.getPosition().getDistance(b.getInitialPosition()) > player.getRange())
						b.delete();

					// Detect collision with monsters
					allMonsters.parallelStream()
							.forEachOrdered(monster -> {

								// If a bullet shot by player collides a monster:
								// Applies only if bullet is active and monster is not hidden
								// Damage the monster and set the bullet to be inactive (to delete it later)
								if (!b.isDeleted() && !monster.isHidden() && monster.getShape().collides(b.getShape())) {
									monster.damage(player);
									b.delete();
								}
							});

				});

		// Draw all infos in the game
		Info.allInfos.parallelStream()
				.filter(info -> !info.isHidden())
				.forEachOrdered(info -> info.draw(g));

		// Draw Player HUD
		selectedHUD.drawHUD(g);
		if (show_selected_HUD_range)
			selectedHUD.drawRange(g);

		// Show description box
		//DESCRIPTION_BOX.draw(g);
		//DESCRIPTION_TEXT.draw(g);
	}

	/**
	 * Goes over player's and all monster's bullets and deletes all inactive onces
	 * Goes over all infos and deletes all once marked as deleted
	 */
	public static void deleteElements() {

		// Go over all the bullets of the player and delete all inactive once
		Util.delete_if(player.getAllBullets(), GameObject::isDeleted);

		// Go over all the bullets of all Monsters and delete all inactive once
		allMonsters.parallelStream()
				.forEachOrdered(monster -> {
					Util.delete_if(monster.getAllBullets(), GameObject::isDeleted);
				});

		// Go over all deleted info and delete them
		Util.delete_if(Info.allInfos, GameObject::isDeleted);

	}

	public static void showDescription(Shado.Shape element, String text) throws Exception {
		// TODO: implement code
		throw new Exception("Code hasn't been implemented yet!");
	}

	/**
	 * Moves all elements except Player by a certain amount
	 *
	 * @param amount The amount to move all elements
	 */
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

	/**
	 * Handles the KeyPress and KeyRelease Events
	 *
	 * @param scene The scene on which the events must be handled
	 */
	public static void handleEvents(Scene scene) {
		scene.setOnKeyPressed(event -> {
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
		});

		scene.setOnKeyReleased(event -> {
			switch (event.getCode()) {
				case C:
					show_selected_HUD_range = false;
					break;
			}
		});
	}

	/**
	 * This function returns the ratio of Original design on the current canvas size
	 *
	 * @param c The current canvas
	 * @return Returns how much the current size differs from the original Design resolution
	 */
	public static double scaleRatio(Canvas c) {
		return ((c.getWidth() * c.getHeight()) / DESIGN_RESOLUTION.area());
	}

	public static void scaleShape(Shado.Shape image, Canvas canvas) {

		double imgWidth = image.getDimensions().width;
		double imgHeight = image.getDimensions().height;

		double imgAspect = (double) imgHeight / imgWidth;

		double canvasWidth = canvas.getWidth();
		double canvasHeight = canvas.getHeight();

		double canvasAspect = (double) canvasHeight / canvasWidth;

		double x1 = 0; // top left X position
		double y1 = 0; // top left Y position
		double x2 = 0; // bottom right X position
		double y2 = 0; // bottom right Y position

		if (imgWidth < canvasWidth && imgHeight < canvasHeight) {
			// the image is smaller than the canvas
			x1 = (canvasWidth - imgWidth) / 2;
			y1 = (canvasHeight - imgHeight) / 2;
			x2 = imgWidth + x1;
			y2 = imgHeight + y1;

		} else {
			if (canvasAspect > imgAspect) {
				y1 = canvasHeight;
				// keep image aspect ratio
				canvasHeight = (int) (canvasWidth * imgAspect);
				y1 = (y1 - canvasHeight) / 2;
			} else {
				x1 = canvasWidth;
				// keep image aspect ratio
				canvasWidth = (int) (canvasHeight / imgAspect);
				x1 = (x1 - canvasWidth) / 2;
			}
			x2 = canvasWidth + x1;
			y2 = canvasHeight + y1;
		}

		image.getDimensions().width = x2 - x1;
		image.getDimensions().height = y2 - y1;

		//g.drawImage(image, x1, y1, x2, y2, 0, 0, imgWidth, imgHeight, null);
	}
}
