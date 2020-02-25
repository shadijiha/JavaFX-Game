/**
 *
 */

package game;

import dataGetters.ReadShadoObjectNotationFile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Main;
import shadoMath.Vertex;
import shapes.Dimension;
import shapes.Shado;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

	public static List<Player> allPlayers = new ArrayList<Player>();

	private List<Bullet> bullets = new ArrayList<>();

	private double maxHp;
	private double hp;
	private double hpRegen;

	private double maxEnergy;
	private double energy;
	private double energyRegen;

	private double ap;
	private double mr;

	private double attackSpeed;
	private double critChance;

	private double ad;
	private double armor;

	private int range;
	private double jumpCost;
	private int ms;

	private double gravity = 0.1;
	private double lift = -5.0;

	// Read properties from a file
	private ReadShadoObjectNotationFile props;

	public Player(int x, int y, String propsFileName) {
		super("player");
		position.x = x;
		position.y = y;
		dimensions.width = 75;
		dimensions.height = 150;
		shape.setPosition(position);
		shape.setDimensions(dimensions);
		shape.setFill(Color.PINK);

		props = new ReadShadoObjectNotationFile(propsFileName, Main.LOGGER);
		initializeProps();

		allPlayers.add(this);
	}

	public Player(String propsFileName) {
		this(0, 0, propsFileName);
	}

	private void initializeProps() {
		maxHp = Double.parseDouble(props.get("maxHp", true));
		hp = Double.parseDouble(props.get("hp", true));
		hpRegen = Double.parseDouble(props.get("hpRegen", true));

		maxEnergy = Double.parseDouble(props.get("maxEnergy", true));
		energy = Double.parseDouble(props.get("energy", true));
		energyRegen = Double.parseDouble(props.get("energyRegen", true));

		ap = Double.parseDouble(props.get("ap", true));
		mr = Double.parseDouble(props.get("magicResist", true));

		attackSpeed = Double.parseDouble(props.get("attackSpeed", true));
		critChance = Double.parseDouble(props.get("critChance", true));

		ad = Double.parseDouble(props.get("ad", true));
		armor = Double.parseDouble(props.get("armor", true));

		range = Integer.parseInt(props.get("range", true));
		ms = Integer.parseInt(props.get("ms", true));

		jumpCost = Double.parseDouble(props.get("jumpCost", true));
	}

	/**
	 * Applies gravity on the calling Player
	 */
	public void update() {

		// Apply gravity
		this.force();

		// Update texture position
		if (texture != null)
			texture.setPosition(position);

		// Detect collisions with platform
		Platform.allPlatforms.parallelStream()
				.forEachOrdered(e -> {
					if (e.collides(this)) {
						// Put the object on the platform
						if (this.position.y < e.position.y) {
							this.position.y = e.position.y - this.dimensions.height;
							this.velocity.y = 0; // Setting velocity to 0 to prevent gravity accumulation
						} else {
							this.position.y = e.position.y + e.dimensions.height;
							this.force();
						}
						this.velocity.y = 0;
					}
				});

		// Regen energy
		energy += energyRegen * Timer.deltaTime;
		hp += hpRegen * Timer.deltaTime;

		// Keep every stat normal
		energy = Math.min(energy, maxEnergy);
		energy = energy < 0 ? 0 : energy;
		hp = Math.min(hp, maxHp);
		hp = hp < 0 ? 0 : hp;
	}

	/**
	 * Applies gravity the object
	 */
	private void force() {
		this.velocity.y += this.gravity * Timer.deltaTime / 10;
		this.position.y += this.velocity.y * Timer.deltaTime / 10;

		if (this.position.y < 0) {
			this.position.y = 0;
			this.velocity.y = 0;
		}
		this.shape.setPosition(position);
	}

	/**
	 * Adds velocity the player for it to jump
	 */
	public void jump() {
		if (
				this.energy > this.jumpCost &&
						this.hp > 0 //&&
			//!this.stunned &&
			//!this.rooted
		) {
			this.velocity.y += this.lift * Timer.deltaTime / 10;
			this.energy -= this.jumpCost;
		}
	}

	/**
	 * Shoots a bullet
	 */
	public void shoot() {
		bullets.add(new Bullet(this));
	}

	/**
	 * Damages the calling object based on the source AD and calling object armor
	 * @param source The damage source
	 * @return Returns the amount substracted from the calling object health
	 */
	public double damage(Player source) {
		double reduction = armor / (100 + armor);
		double temp = source.ad * (1 - reduction);
		hp -= temp;
		return temp;
	}

	/**
	 * Draw the health bar of the calling object
	 * @param g The GraphicsContext
	 * @param p Position of the health bar
	 * @param d dimension of the health bar
	 */
	private void drawHealthBar(GraphicsContext g, Vertex p, Dimension d) {
		double percentage = hp / maxHp;

		var main_bar = new Shado.Rectangle((float) p.x, (float) p.y, d.width, d.height);
		var sec_bar = new Shado.Rectangle((float) main_bar.getPosition().x, (float) main_bar.getPosition().y, (float) (main_bar.getDimensions().width * percentage), (float) main_bar.getDimensions().height);

		main_bar.setFill(Color.WHITE).draw(g);
		sec_bar.setFill(Color.GREEN).draw(g);
	}

	/**
	 * Draw the health bar of the calling object
	 * @param g The GraphicsContext
	 */
	public void drawHealthBar(GraphicsContext g) {
		drawHealthBar(g, new Vertex((float) position.x, (float) position.y - 30), new Dimension(dimensions.width, 15));
	}

	/**
	 * Draw the health bar of the calling object
	 * @param g The GraphicsContext
	 * @param p Position of the health bar
	 * @param d dimension of the health bar
	 */
	private void drawEnergyBar(GraphicsContext g, Vertex p, Dimension d) {
		double percentage = energy / maxEnergy;

		var main_bar = new Shado.Rectangle((float) p.x, (float) p.y, d.width, d.height);
		var sec_bar = new Shado.Rectangle(
				(float) main_bar.getPosition().x,
				(float) main_bar.getPosition().y,
				(float) (main_bar.getDimensions().width * percentage),
				(float) main_bar.getDimensions().height);

		main_bar.setFill(Color.WHITE).draw(g);
		sec_bar.setFill(Color.rgb(0, 128, 255)).draw(g);
	}

	/**
	 * Draw all the infomation of the current object to the screen
	 * @param g The Graphic context
	 * @param c The canvas on the graphic context
	 */
	public void drawHUD(GraphicsContext g, Canvas c) {

		Dimension HUD_dimensions = new Dimension((float) (c.getWidth() * 0.45), (float) (c.getHeight() * 0.25));
		Vertex center = new Vertex(c.getWidth() / 2 - HUD_dimensions.width / 2, c.getHeight() - HUD_dimensions.height);

		final var main_HUD = new Shado.Rectangle((float) center.x, (float) center.y, HUD_dimensions.width, HUD_dimensions.height);
		main_HUD.setFill(Color.rgb(150, 150, 150, 0.8));
		main_HUD.draw(g);

		// Draw big health bar and text
		float bar_height = (float) (main_HUD.getDimensions().height * 0.10);
		Vertex health_bar_pos = new Vertex(main_HUD.getPosition().x + main_HUD.getDimensions().width * 0.10,
				main_HUD.getPosition().y + main_HUD.getDimensions().height * 0.75 - bar_height);

		Dimension health_bar_dim = new Dimension((float) (main_HUD.getDimensions().width * 0.80), bar_height);

		drawHealthBar(g,
				health_bar_pos,
				health_bar_dim);

		var hp_bar_text = new Shado.Text(String.format("%.0f/%.0f + %.2f", hp, maxHp, hpRegen * Timer.deltaTime * Timer.framerate()),
				(health_bar_pos.x + (health_bar_dim.width) / 2 - 20),
				(health_bar_pos.y + 2 * bar_height / 3));
		hp_bar_text.draw(g);

		// Draw big energy bar
		drawEnergyBar(g,
				new Vertex(health_bar_pos.x, health_bar_pos.y + bar_height * 1.25),
				health_bar_dim);
		var energy_bar_text = new Shado.Text(String.format("%.0f/%.0f + %.2f", energy, maxEnergy, energyRegen * Timer.deltaTime * Timer.framerate()),
				(health_bar_pos.x + health_bar_dim.width / 2 - 20),
				(health_bar_pos.y + bar_height * 2));
		energy_bar_text.draw(g);

		//========= Draw stats and portrait ==================
		Dimension stat_box_dim = new Dimension(250, HUD_dimensions.height);
		var stats_box = new Shado.Rectangle(center.x - stat_box_dim.width, center.y, stat_box_dim.width, stat_box_dim.height);
		stats_box.setFill(Color.rgb(100, 100, 100, 0.5));
		stats_box.draw(g);

		// Display portrait
		Shado.Image portait = new Shado.Image(texture_path, stats_box.getPosition().x * 1.05, stats_box.getPosition().y + dimensions.height / 2, dimensions.width, dimensions.height);
		portait.draw(g);

		// Display AD stat
		Vertex stat_start_pos = new Vertex(portait.getPosition().x + portait.getDimensions().width + 20, portait.getPosition().y - 30);

		Shado.Image ad_icon = new Shado.Image("DataFiles/Images/ad.png", stat_start_pos.x, stat_start_pos.y, 20, 20);
		Shado.Text ad_text = new Shado.Text(Integer.toString((int) this.ad), ad_icon.getPosition().x + ad_icon.getDimensions().width + 5, ad_icon.getCenter().y + 5);
		ad_icon.draw(g);
		ad_text.draw(g);

		// Display AP stat
		Shado.Image ap_icon = new Shado.Image("DataFiles/Images/ability_power.png", ad_text.getPosition().x + 50, ad_icon.getPosition().y, 20, 20);
		Shado.Text ap_text = new Shado.Text(Integer.toString((int) this.ap), ap_icon.getPosition().x + ap_icon.getDimensions().width + 5, ad_text.getPosition().y);
		ap_icon.draw(g);
		ap_text.draw(g);
	}

	// Getters

	/**
	 * @return Returns the range of the object
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @return Returns the mouvement speed of the player object
	 */
	public int getMS() {
		return ms;
	}

	/**
	 * @return Returns if the object has <= 0 hp
	 */
	public boolean isDead() {
		return hp <= 0;
	}

	/**
	 * @return Returns the list of bullets shot by the object
	 */
	public List<Bullet> getAllBullets() {
		return bullets;
	}
}
