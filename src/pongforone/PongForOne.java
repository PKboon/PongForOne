package pongforone;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Pikulkaew Boonpeng 
 * Last Modified 4/12/2019 10.31PM
 */
public class PongForOne extends Application {

    // game pane
    Pane game = new Pane();

    // For pane size
    private final double WINDOW_W = 800;
    private final double WINDOW_H = 500;

    // For scoring
    private int score = 20; // begin
    StackPane scoreLine = new StackPane();
    //Label scoreLabel = new Label("SCORE: ");
    Label scoreValue = new Label(Integer.toString(score));

    // For ball
    public double radius = 20;
    private Timeline animation;
    private double x = radius; // x position of the ball, center of the ball
    private double y = radius; // y position of the ball, center of the ball
    private double dx = 1; // delta x
    private double dy = 1; // delta y
    private final Circle ball = new Circle(x, y, radius);

    // For paddle
    private final double PADDLE_H = 20;
    public double paddle_w = 80;
    private final Rectangle paddle = new Rectangle(paddle_w, PADDLE_H);

    // counter for paddle connections
    private int connect = 0;

    // For game over window
    Label gameOver = new Label("Game Over");

    /**
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        // Set score line
        //scoreLabel.setTextFill(Color.DARKBLUE);
        //scoreLabel.setFont(Font.font("Arial",FontWeight.LIGHT, 100));
        scoreValue.setOpacity(.1);
        scoreValue.setTextFill(Color.BLUE);
        scoreValue.setFont(Font.font("Arial", FontWeight.BOLD, 400));
        // center scoreValue
        scoreLine.translateXProperty().
                bind(primaryStage.widthProperty().divide(4.7));
        scoreLine.translateYProperty().
                bind(primaryStage.heightProperty().divide(35));
        scoreLine.getChildren().add(scoreValue);

        // Set paddle
        double paddlePos = WINDOW_H - PADDLE_H - radius;
        paddle.setX(WINDOW_W / 2 - paddle.getWidth() / 2); // set x of paddle
        paddle.setY(paddlePos); // set y of paddle
        paddle.setFill(Color.BLACK);
        paddle.setOnMouseDragged(e -> {
            paddle.setX(e.getX());
            // set paddle bound
            if (paddle.getX() <= 0) {
                paddle.setX(0);
            } else if (paddle.getX() + paddle.getWidth() >= WINDOW_W) {
                paddle.setX(WINDOW_W - paddle.getWidth());
            }
        });

        // Set ball
        ball.setFill(Color.BROWN);
        // Animate the ball by calling gameOn()
        animation = new Timeline(
                new KeyFrame(Duration.millis(10), e -> gameOn(primaryStage)));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play(); // Start animation

        // put everything in the same pane
        game.getChildren().addAll(scoreLine, ball, paddle);

        Scene scene = new Scene(game, WINDOW_W, WINDOW_H);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Pong For One by PK");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     *
     * @param s bind width property, re-center the score
     */
    public void gameOn(Stage s) {
        // Check boundaries
        // when ball hits left and right wall
        if (x - radius < 0 || x + radius > WINDOW_W) {
            dx *= -1; // Change ball move direction
        }
        // when ball hits top wall
        if (y - radius < 0) {
            dy *= -1;
        } // when ball hits paddle
        if (y + radius >= paddle.getY()
                && x + radius >= paddle.getX()
                && x - radius <= paddle.getX() + paddle.getWidth()) {
            dy *= -1; // Change ball move direction
            connect++; // count paddle connections
            // For every 10 connections
            if (connect % 10 == 0) {
                // the increase ball's speed
                if (animation.getRate() <= 12) { // limit the ball's speed
                    animation.setRate(animation.getRate() + .5);
                }
                // the ball's color changes
                ball.setFill(Color.color(Math.random(),
                        Math.random(), Math.random()));
            }
        } // When paddle missed the ball
        else if (y + radius > WINDOW_H) {
            connect = 0; // connection reset
            score--; // scores decrease
            scoreValue.setText(Integer.toString(score));

            // recenter x value the score when there's a single number
            if (score <= 9) {
                scoreLine.translateXProperty().
                        bind(s.widthProperty().divide(2.68));
            }

            // every 2 missed connection, paddle grows
            if (score % 2 == 0) {
                paddle_w += 30;
                paddle.setWidth(paddle_w);
                if (paddle_w >= 200) {
                    paddle.setWidth(200);
                }
            }

            // color changes when score decreased
            if (score <= 10) {
                scoreValue.setTextFill(Color.GREEN);
            }
            if (score <= 3) {
                scoreValue.setTextFill(Color.RED);
            }

            // when score is 0, game ends
            if (score == 0) {
                animation.pause();
                gameOver.setFont(Font.font("Arial", 40));
                gameOver.setTextFill(Color.RED);
                Stage end = new Stage();
                StackPane gameEnd = new StackPane(gameOver);
                Scene scene = new Scene(gameEnd, 250, 80);
                end.setTitle("Game Over");
                end.setScene(scene);
                end.show();
            }
            // Adjust new game ball position
            x = radius;
            y = radius;
            ball.setCenterX(x);
            ball.setCenterY(y);
        }
        // Adjust ball position
        x += dx;
        y += dy;
        ball.setCenterX(x);
        ball.setCenterY(y);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
