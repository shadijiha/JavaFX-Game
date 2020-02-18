/**
 * Shado Rectangle
 */

package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import shadoMath.Vector;
import shadoMath.Vertex;

public final class Shado {

	public static abstract class Shape implements Cloneable {
		protected Vertex position;
		protected Dimension dimensions;
		protected Color fill;
		protected Color stroke;
		protected int lineWidth;
		protected Vector velocity;

		protected Shape() {
			position = new Vertex();
			dimensions = new Dimension();
			velocity = new Vector();
			lineWidth = 1;
		}

		/**
		 * Draws the calling rectangle to a JAVAFX Canvas
		 *
		 * @param g The GraphicsContext of the canvas
		 */
		public abstract void draw(GraphicsContext g);

		/**
		 * @return Returns the area of the calling Shape
		 */
		public abstract float area();

		public void move() {
			position.x += velocity.x;
			position.y += velocity.y;
		}

		// Setters

		/**
		 * Changes the position of the shape
		 *
		 * @param pos The new position
		 * @return Returns the calling object
		 */
		public Shape setPosition(final Vertex pos) {
			this.position = new Vertex(pos);
			return this;
		}

		/**
		 * Changes the dimensions of the shape
		 *
		 * @param d The new dimensions
		 * @return Returns the calling object
		 */
		public Shape setDimensions(final Dimension d) {
			this.dimensions = new Dimension(d);
			return this;
		}

		/**
		 * Changes the Fill color of the calling object
		 *
		 * @param c The new Color
		 * @return Returns the calling object
		 */
		public Shape setFill(final Color c) {
			fill = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity());
			return this;
		}

		/**
		 * Changes the Stroke color of the calling object
		 *
		 * @param c The new stroke color
		 * @return Returns the calling object
		 */
		public Shape setStroke(final Color c) {
			fill = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity());
			return this;
		}

		/**
		 * Changes the line width of the calling shape
		 *
		 * @param lineWidth The new line width
		 * @return Returns the calling object
		 */
		public Shape setLineWidth(int lineWidth) {
			this.lineWidth = lineWidth;
			return this;
		}

		public Shape noFill() {
			setFill(Color.rgb(0, 0, 0, 0));
			return this;
		}

		public Shape noStroke() {
			setStroke(Color.rgb(0, 0, 0, 0));
			return this;
		}

		public Shape setVelocity(Vector vel) {
			this.velocity.x = vel.x;
			this.velocity.y = vel.y;
			this.velocity.z = vel.z;
			return this;
		}

		// Overridden java.lang.Object functions
		public boolean equals(Object o) {
			if (o == null || o.getClass() != getClass()) {
				return false;
			} else {
				Shape s = (Shape) o;
				return dimensions.equals(s.dimensions) && position.equals(s.position);
			}
		}

		public String toString() {
			return String.format("%s (x: %f, y: %f, w: %f, h: %f)", getClass(), position.x, position.y, dimensions.width, dimensions.height);
		}

		public Shape clone() {
			try {
				Shape clone = (Shape) super.clone();
				clone.position = position.clone();
				clone.dimensions = dimensions.clone();
				clone.fill = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), fill.getOpacity());
				clone.stroke = new Color(stroke.getRed(), stroke.getGreen(), stroke.getBlue(), stroke.getOpacity());
				clone.lineWidth = lineWidth;
				clone.velocity = velocity.clone();
				return clone;
			} catch (Exception e) {
				return null;
			}
		}

		// Getters

		/**
		 * @return Returns the position
		 */
		public Vertex getPosition() {
			return new Vertex(position);
		}

		/**
		 * @return Returns the dimension of the shape
		 */
		public Dimension getDimensions() {
			return new Dimension(dimensions);
		}
	}

	public static class Rectangle extends Shape {

		/**
		 * Initializes a Rectangle
		 *
		 * @param x The X position of the rectangle
		 * @param y The Y position of the rectangle
		 * @param w The width of the rectangle
		 * @param h The height of the rectangle
		 */
		public Rectangle(float x, float y, float w, float h) {
			super();
			position.x = x;
			position.y = y;
			dimensions.width = w;
			dimensions.height = h;
			fill = Color.rgb(255, 255, 255);
			stroke = Color.rgb(0, 0, 0);
		}

		public Rectangle(Vertex pos, Dimension d) {
			this((float) pos.x, (float) pos.y, d.width, d.height);
		}

		/**
		 * Initializes a rectangle to (x: 0, y: 0, w: 100, h: 100)
		 */
		public Rectangle() {
			this(0, 0, 100, 100);
		}

		public Rectangle(final Rectangle other) {
			this(other.position, other.dimensions);
		}

		/**
		 * Draws the calling rectangle to a JAVAFX Canvas
		 *
		 * @param g The GraphicsContext of the canvas
		 */
		@Override
		public void draw(GraphicsContext g) {
			g.setFill(fill);
			g.setStroke(stroke);
			g.setLineWidth(lineWidth);
			g.fillRect(position.x, position.y, dimensions.width, dimensions.height);
			g.strokeRect(position.x, position.y, dimensions.width, dimensions.height);
		}

		// Collisions
		public boolean Collides(Shado.Rectangle other) {
			return false;
		}

		/**
		 * @return Returns the area of the rectangle
		 */
		@Override
		public float area() {
			return dimensions.width * dimensions.height;
		}
	}

	public static class Circle extends Shape {

		public Circle(float x, float y, float r) {
			position.x = x;
			position.y = y;
			dimensions.width = r;
			dimensions.height = r;
		}

		public Circle(Vertex pos, Dimension d) {
			this((float) pos.x, (float) pos.y, d.width);
		}

		public Circle() {
			this(0, 0, 0);
		}

		public Circle(final Circle other) {
			this(other.position, other.dimensions);
		}

		@Override
		public void draw(GraphicsContext g) {
			g.setFill(fill);
			g.setStroke(stroke);
			g.setLineWidth(lineWidth);
			g.fillOval(position.x, position.y, dimensions.width, dimensions.width);
			g.strokeOval(position.x, position.y, dimensions.width, dimensions.width);
		}

		@Override
		public float area() {
			return (float) (Math.PI * dimensions.width * dimensions.width);
		}

		public float getRadius() {
			return dimensions.width;
		}

		public Circle setRadius(float radius) {
			dimensions.width = radius;
			dimensions.height = radius;
			return this;
		}
	}

	public static class Line extends Shape {

		/**
		 * Initializes a Rectangle
		 *
		 * @param fromX The X position of the rectangle
		 * @param fromY The Y position of the rectangle
		 * @param toX   The width of the rectangle
		 * @param toY   The height of the rectangle
		 */
		public Line(float fromX, float fromY, float toX, float toY) {
			super();
			position.x = fromX;
			position.y = fromY;
			dimensions.width = toX;
			dimensions.height = toY;
			stroke = Color.rgb(0, 0, 0);
		}

		public Line(Vertex pos, Dimension d) {
			this((float) pos.x, (float) pos.y, d.width, d.height);
		}

		/**
		 * Initializes a rectangle to (x: 0, y: 0, w: 100, h: 100)
		 */
		public Line() {
			this(0, 0, 100, 100);
		}

		public Line(final Line other) {
			this(other.position, other.dimensions);
		}

		/**
		 * Draws the calling rectangle to a JAVAFX Canvas
		 *
		 * @param g The GraphicsContext of the canvas
		 */
		@Override
		public void draw(GraphicsContext g) {
			g.setFill(fill);
			g.setStroke(stroke);
			g.setLineWidth(lineWidth);
			g.strokeLine(position.x, position.y, dimensions.width, dimensions.height);
		}

		/**
		 * @return Returns the area of the rectangle
		 */
		@Override
		public float area() {
			return 0.0f;
		}
	}

	public static class Text extends Shape {

		private Font font;
		private String text;

		public Text(String content, float x, float y, Font font, Color fillColor, Color strokeColor) {
			super();
			this.text = content;
			position.x = x;
			position.y = y;
			this.font = font;
			fill = fillColor;
			stroke = strokeColor;
		}

		public Text(String content, float x, float y) {
			this(content, x, y, new Font("Arial", 16), Color.rgb(0, 0, 0), Color.rgb(0, 0, 0, 0));
		}

		public Text() {
			this("Sample text", 0.0f, 0.0f);
		}

		public Text(final Text other) {
			this(other.text, (float) other.position.x, (float) other.position.y, other.font, other.fill, other.stroke);
		}

		// Setters

		/**
		 * Changes the font of the calling Text element
		 *
		 * @param newFont The new desired font
		 * @return Returns the calling object
		 */
		public Text setFont(Font newFont) {
			this.font = newFont;
			return this;
		}

		/**
		 * Changes the family of the calling Text element font's
		 *
		 * @param family The new font family
		 * @return Returns the calling object
		 */
		public Text setFontFamily(String family) {
			this.font = new Font(family, this.font.getSize());
			return this;
		}

		/**
		 * Changes the size of the calling Text element font's
		 *
		 * @param size The new font size
		 * @return Returns the calling object
		 */
		public Text setFontSize(int size) {
			this.font = new Font(this.font.getFamily(), size);
			return this;
		}

		/**
		 * Changes the Text content of the calling Text element
		 *
		 * @param text The new desired Text
		 * @return Returns the calling object
		 */
		public Text setText(String text) {
			this.text = text;
			return this;
		}

		@Override
		public void draw(GraphicsContext g) {
			g.setFont(font);
			g.setFill(fill);
			g.setStroke(stroke);
			g.fillText(text, position.x, position.y);
			g.strokeText(text, position.x, position.y);
		}

		@Override
		public float area() {
			return 0.0f;
		}
	}
}




