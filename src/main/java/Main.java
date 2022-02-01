import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    GridPane grid = new GridPane();
    GridPane bottomgrid = new GridPane();

    int clicks = 0;
    ArrayList<Card> lastRound = new ArrayList<>();
    int unvield = 0;
    int time = 0;

    public static void main(String... args) {
        launch(args);
    }

    static void setFrame(Card card, HashMap<String, Image> frames) {
        Image img = frames.get(card.toString());
        ImageView view = new ImageView(img);
        view.setFitHeight(300);
        view.setPreserveRatio(true);
        card.setGraphic(view);
    }

    static String timeText(int i) {
        String min = String.format("%s%d",i/60 < 10 ?"0":"", i/60);
        String sec = String.format("%s%d",i%60 < 10 ?"0":"", i%60);

        return String.format("%s:%s",min,sec);
    }

    public void start(Stage primaryStage) {
        Board game = new Board(6,2);
        game.generateBoard();

        Text title = new Text("Keep going...");
        title.setId("title");

        Text score = new Text(String.format("Score %d", unvield));
        Text timerText = new Text("Time: " + timeText(time));
        score.setFill(Color.web("white"));
        timerText.setFill(Color.web("white"));
        timerText.setId("time");
        score.setTextAlignment(TextAlignment.RIGHT);

        lastRound.add(new Card());
        lastRound.add(new Card());

        int dimx = game.getDimx();
        int dimy = game.getDimy();

        ArrayList<ArrayList<Card>> buttons = new ArrayList<>();
        HashMap<String, Image> frames = new HashMap<>();
        String[] frames_list = {"react", "angular", "django", "python", "java", "typescript", "javascript", "flutter"};

        frames.put("flipped", new Image("/images/flipped.png"));
        frames.put("refresh", new Image("/images/refresh.png"));
        frames.put("empty", new Image("/images/empty.png"));


        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1),
                (event) -> {
                    time++;
                    timerText.setText("Time: " + timeText(time));
                }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        for (int i = 0; i < frames_list.length; i++) {
            frames.put(Integer.toString(i), new Image(String.format("/images/%s.png",frames_list[i])));
        }

        for (int i = 0; i < dimx; i++) {
            buttons.add(new ArrayList<>());
            for (int j = 0; j < dimy; j++) {
                Card card = game.getCard(i,j);
                buttons.get(i).add(card);
                card.setPrefSize(214, 300);
                setFrame(card, frames);

                card.setOnAction(e -> {
                    if (clicks < 2 && !card.isFlipped()) {
                        card.unveil();
                        setFrame(card, frames);
                        lastRound.set(clicks, card);
                        clicks++;

                        if (clicks == 2) {
                            if (lastRound.get(0).getIdCard() != lastRound.get(1).getIdCard()) {
                                Timeline incorrect = new Timeline(new KeyFrame(Duration.millis(500),
                                        (event) -> {
                                            lastRound.get(0).hide(); lastRound.get(1).hide();
                                            setFrame(lastRound.get(0), frames); setFrame(lastRound.get(1), frames);
                                            clicks = 0;
                                        }));
                                incorrect.play();
                            } else {
                                unvield += 2;
                                Timeline correct = new Timeline(new KeyFrame(Duration.millis(200),
                                        (event) -> {
                                            lastRound.get(0).kill(); lastRound.get(1).kill();
                                            setFrame(lastRound.get(0), frames); setFrame(lastRound.get(1), frames);
                                            clicks = 0;
                                        }));
                                correct.play();
                                score.setText(String.format("Score %d", unvield));
                                if (unvield == dimx*dimy) {
                                    System.out.println("You did it!");
                                    title.setFill(Color.web("white"));
                                    timer.stop();
                                    title.setText("\uD83C\uDF89 Congrats \uD83C\uDF89");
                                }
                            }
                        }
                    }
                });
            }
        }

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(50, 50, 50, 50));

        bottomgrid.setAlignment(Pos.CENTER);
        bottomgrid.setPadding(new Insets(10, 0, 10, 0));

        Button refresh = new Button();
        refresh.setPrefSize(15, 15);
        Image refreshImg = frames.get("refresh");
        ImageView refreshView = new ImageView(refreshImg);
        refreshView.setFitHeight(40);
        refreshView.setPreserveRatio(true);
        refresh.setGraphic(refreshView);
        refresh.setOnAction (e -> {
           unvield = 0;
           clicks = 0;
           game.generateBoard();
           title.setText("Keep Going...");
           title.setFill(Color.web("white"));
           score.setText(String.format("Score %d", unvield));
           time = 0;
           timer.play();
           for (int i = 0; i < dimx; i++) {
                for (int j = 0; j < dimy; j++) {
                    buttons.get(i).get(j).hide();
                    buttons.get(i).get(j).revive();
                    setFrame(buttons.get(i).get(j), frames);
                }
            }
        });
        bottomgrid.setAlignment(Pos.CENTER);
        bottomgrid.setHgap(200);
        bottomgrid.add(title, 0,0, 1,1);
        bottomgrid.add(refresh, 1,0, 1,1);
        bottomgrid.add(timerText, 2,0, 1,1);

        grid.add(bottomgrid, 0, dimy+1, dimx, 1);

        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimy; j++) {
                grid.add(buttons.get(i).get(j), i, j+1, 1,1);
            }
        }

        Scene scene = new Scene(grid, 1400, 750);
        scene.getStylesheets().add("/style.css");

        primaryStage.setTitle("Memory");
        primaryStage.onCloseRequestProperty()
                .setValue(e -> System.out.println("\nBye! See you later!"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
