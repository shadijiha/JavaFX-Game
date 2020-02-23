package sample;

import game.Time;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import logger.Logger;
import shapes.Game;
import shapes.Shado;

public class Main extends Application {

	public static final Logger LOGGER = new Logger(false);
	private final long[] frameTimes = new long[100];
	private int frameTimeIndex = 0;

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
		Game.initialize(canvas);

		Shado.Text FPS_TEXT = new Shado.Text("Loading...", 10, 30);

		// clear the canvas and Draw shapes
		new AnimationTimer() {
			public void handle(long now) {
				gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

				// background image clears canvas
				Game.render(gc, canvas);

				// Calculate and display FPS
				long oldFrameTime = frameTimes[frameTimeIndex];
				frameTimes[frameTimeIndex] = now;
				frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;

				long elapsedNanos = now - oldFrameTime;
				long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
				Time.setFramerate(1_000_000_000.0 / elapsedNanosPerFrame);

				if (Time.framerate() >= 1) {
					Time.deltaTime = elapsedNanos / 100000000D;
					FPS_TEXT.setText(String.format("%.3f FPS", Time.framerate()));

					// Increment the time elapsed since program start
					Time.addTime(Time.deltaTime);
				}
				FPS_TEXT.draw(gc);
			}
		}.start();


		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		Game.handleEvents(scene);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
