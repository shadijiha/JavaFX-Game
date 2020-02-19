package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import logger.Logger;
import shapes.Game;

public class Main extends Application {

	public static final Logger LOGGER = new Logger(false);

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
		Canvas canvas = new Canvas(1000, 562.5);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Initialize shapes to draw
		Game.initialize();

		// clear the canvas and Draw shapes
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

				// background image clears canvas
				Game.render(gc);
			}
		}.start();


		root.getChildren().add(canvas);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
