/**
 *
 */

package game;

import dataGetters.ReadSONFile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Main;
import shadoMath.Vector;
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
	private double lifeSteal;

	private int range;
	private double jumpCost;
	private int ms;

	private double gravity = 0.1;
	private double lift = -5.0;
	protected Vector bulletVelocity;

	// Read properties from a file
	private ReadSONFile props;

	public Player(int x, int y, String propsFileName) {
		super("player");
		position.x = x;
		position.y = y;
		dimensions.width = 75;
		dimensions.height = 150;
		shape.setPosition(position);
		shape.setDimensions(dimensions);
		shape.setFill(Color.PINK);

		props = new ReadSONFile(propsFileName, Main.LOGGER);
		initializeProps();

		allPlayers.add(this);
	}

	public Player(String propsFileName) {
		this(0, 0, propsFileName);
	}

	/**
	 * Gets the stats data of the object from the specified file
	 */
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

		bulletVelocity = new Vector(Double.parseDouble(props.get("bulletVelocityX", true)),
				Double.parseDouble(props.get("bulletVelocityY", true)));

		lifeSteal = Double.parseDouble(props.get("lifeSteal", true));
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
		var bullet = new Bullet(this);
		bullet.setTexture("DataFiles/Images/weapon.png");

		bullets.add(bullet);
	}

	/**
	 * Damages the calling object based on the source AD and calling object armor
	 * @param source The damage source
	 * @return Returns the amount substracted from the calling object health
	 */
	public double damage(Player source) {
		double reduction = armor / (100 + armor);
		double finalDamage = source.ad * (1 - reduction);

		// See if the damage crits
		if (Math.random() <= source.critChance) {
			finalDamage *= 2;

			// Draw the damage dealt
			new Info(Integer.toString((int) finalDamage),
					this.position,
					Vector.fromAngle(Math.random() * Math.PI * 2),
					Color.ORANGE,
					"DataFiles/Images/Critical_strike_chance.png");

			// Play crit sound
			new Audio("DataFiles/sounds/crit sound.mp3").setVolume(0.1).play();
		}

		hp -= finalDamage;

		// Apply life steal to the source
		source.heal(finalDamage * source.lifeSteal);


		return finalDamage;
	}

	/**
	 * Heals (Increment the hp) the calling object by a specific amount
	 * @param amount The amount to heal
	 * @return Returns the amount healed
	 */
	public double heal(double amount) {
		this.hp += amount;
		return amount;
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

		// Display Armor stat
		Shado.Image armor_icon = new Shado.Image("DataFiles/Images/armor.png", stat_start_pos.x, stat_start_pos.y + ad_icon.getDimensions().height * 3, 20, 20);
		Shado.Text armor_text = new Shado.Text(Integer.toString((int) this.armor), armor_icon.getPosition().x + armor_icon.getDimensions().width + 5, armor_icon.getCenter().y + 5);
		armor_icon.draw(g);
		armor_text.draw(g);

		// Display magic resist stat
		Shado.Image mr_icon = new Shado.Image("DataFiles/Images/mr.png", ap_icon.getPosition().x, armor_icon.getPosition().y, 20, 20);
		Shado.Text mr_text = new Shado.Text(Integer.toString((int) this.mr), mr_icon.getPosition().x + mr_icon.getDimensions().width + 5, mr_icon.getCenter().y + 5);
		mr_icon.draw(g);
		mr_text.draw(g);

		// Display attack speed stat
		Shado.Image as_icon = new Shado.Image("DataFiles/Images/attack_speed.png", armor_icon.getPosition().x, armor_icon.getPosition().y + armor_icon.getDimensions().height * 3, 20, 20);
		Shado.Text as_text = new Shado.Text(Double.toString(this.attackSpeed), as_icon.getPosition().x + as_icon.getDimensions().width + 5, as_icon.getCenter().y + 5);
		as_icon.draw(g);
		as_text.draw(g);

		// Display life steal stat
		Shado.Image lifesteal_icon = new Shado.Image("DataFiles/Images/lifeSteal.png", mr_icon.getPosition().x, mr_icon.getPosition().y + mr_icon.getDimensions().height * 3, 20, 20);
		Shado.Text lifesteal_text = new Shado.Text(Integer.toString((int) (this.lifeSteal * 100)) + "%",
				lifesteal_icon.getPosition().x + lifesteal_icon.getDimensions().width + 5, lifesteal_icon.getCenter().y + 5);
		lifesteal_icon.draw(g);
		lifesteal_text.draw(g);

		// Display range stat
		Shado.Image range_icon = new Shado.Image("DataFiles/Images/range.png", as_icon.getPosition().x, as_icon.getPosition().y + as_icon.getDimensions().height * 3, 20, 20);
		Shado.Text range_text = new Shado.Text(Integer.toString(this.range), range_icon.getPosition().x + range_icon.getDimensions().width + 5, range_icon.getCenter().y + 5);
		range_icon.draw(g);
		range_text.draw(g);

		// Display Crit chance stat
		Shado.Image crit_icon = new Shado.Image("DataFiles/Images/crit.png", lifesteal_icon.getPosition().x, lifesteal_icon.getPosition().y + lifesteal_icon.getDimensions().height * 3, 20, 20);
		Shado.Text crit_text = new Shado.Text(Integer.toString((int) (this.critChance * 100)) + "%", crit_icon.getPosition().x + crit_icon.getDimensions().width + 5, crit_icon.getCenter().y + 5);
		crit_icon.draw(g);
		crit_text.draw(g);


	}

	public void drawRange(GraphicsContext g) {

		g.setGlobalAlpha(0.4);

		new Shado.Circle(position.x - range + dimensions.width / 2, position.y - range + dimensions.height / 2, range).setFill(Color.PURPLE).draw(g);

		g.setGlobalAlpha(1);

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
