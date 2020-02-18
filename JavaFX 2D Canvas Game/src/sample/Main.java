package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import logger.Logger;
import shadoMath.Vector;
import shapes.Shado;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

	public static final Logger LOGGER = new Logger(false);
	private static List<Shado.Shape> shapesToDraw = new ArrayList<Shado.Shape>();

	public static void main(String[] args) {
		launch(args);

		try {
			LOGGER.close();
		} catch (Exception e) {
			System.out.print(e.getMessage() + " ");
			e.printStackTrace();
		}
	}


	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Drawing Operations Test");
		Group root = new Group();
		Canvas canvas = new Canvas(1920, 1080);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Initialize shapes to draw
		initialize();

		// clear the canvas and Draw shapes
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

				// background image clears canvas
				drawShapes(gc);
			}
		}.start();


		root.getChildren().add(canvas);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	private void initialize() {
		// Init shapes
		Shado.Rectangle rect = new Shado.Rectangle(300, 300, 100, 100);
		rect.setVelocity(new Vector(1, 1));

		Shado.Text txt = new Shado.Text("Hello :D", 500, 500).setFontSize(32);

		shapesToDraw.add(rect);
		shapesToDraw.add(txt);

	}

	private void drawShapes(GraphicsContext gc) {

		// Draw shapes
		shapesToDraw.parallelStream()
				.forEachOrdered(e -> {
					e.draw(gc);
					e.move();
				});
	}
}
