package sample;

import game.Game;
import game.Mouse;
import game.Timer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import logger.Logger;
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

		// Turn on debug mode
		LOGGER.setDebugModeTo(false);

		primaryStage.setTitle("Simple 2D game :)");
		Group root = new Group();
		Canvas canvas = new Canvas(1280, 720);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Register mouse position
		canvas.setOnMouseMoved(event -> {
			Mouse.setX(event.getX());
			Mouse.setY(event.getY());
		});

		canvas.setOnMouseClicked(event -> {
			Mouse.setLastClick(event.getX(), event.getY());
		});

		// Initialize shapes to draw
		Game.initialize(canvas);

		final Shado.Text FPS_TEXT = new Shado.Text("Loading...", 10, 30);
		final Shado.Image reload = new Shado.Image("DataFiles/Images/reload.png", 10, 10, 100, 100);
		reload.onClick(e -> Game.initialize(canvas));

		// clear the canvas and Draw shapes
		new AnimationTimer() {
			public void handle(long now) {
				gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

				// background image clears canvas
				Game.render(canvas);

				// Calculate and display FPS
				long oldFrameTime = frameTimes[frameTimeIndex];
				frameTimes[frameTimeIndex] = now;
				frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;

				long elapsedNanos = now - oldFrameTime;
				long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
				Timer.setFramerate(1_000_000_000.0 / elapsedNanosPerFrame);

				if (Timer.framerate() >= 1) {
					Timer.deltaTime = elapsedNanos / 100000000D;
					FPS_TEXT.setText(String.format("%.3f FPS", Timer.framerate()));

					// Increment the time elapsed since program start
					Timer.addTime(Timer.deltaTime);
				}

				// Draw FPS text
				FPS_TEXT.draw(canvas);

				// Draw reload button
				reload.draw(canvas);

			}
		}.start();


		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		Game.handleEvents(scene);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
