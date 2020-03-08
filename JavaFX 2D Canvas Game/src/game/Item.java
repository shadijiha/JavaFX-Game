/**
 *
 */

package game;

import java.util.function.Consumer;

public class Item extends GameObject {

	private Consumer<Player> onBuy;
	private Consumer<Player> onSell;
	private Consumer<Player> passive;
	private Consumer<Player> active;

	private String icon;

	private Item(String icon, Consumer<Player> onBuy, Consumer<Player> onSell, Consumer<Player> passive, Consumer<Player> active) {
		super("item");
		this.onSell = onBuy;
		this.onBuy = onBuy;
		this.passive = passive;
		this.active = active;
		this.icon = icon;
	}

	public static class ItemBuilder {

		private Consumer<Player> builder_onBuy;
		private Consumer<Player> builder_onSell;
		private Consumer<Player> builder_passive;
		private Consumer<Player> builder_active;
		private String builder_icon;

		public ItemBuilder() {
		}

		public ItemBuilder onBuy(Consumer<Player> func) {
			builder_onBuy = func;
			return this;
		}

		public ItemBuilder onSell(Consumer<Player> func) {
			builder_onSell = func;
			return this;
		}

		public ItemBuilder setPassive(Consumer<Player> func) {
			builder_passive = func;
			return this;
		}

		public ItemBuilder setActive(Consumer<Player> func) {
			builder_active = func;
			return this;
		}

		public ItemBuilder setIcon(String icon) {
			builder_icon = icon;
			return this;
		}

		public Item build() {
			return new Item(builder_icon, builder_onBuy, builder_onSell, builder_passive, builder_active);
		}
	}
}
