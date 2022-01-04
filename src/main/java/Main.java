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
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    GridPane grid = new GridPane();
    public int clicks = 0;
    public ArrayList<Card> lastRound = new ArrayList<>();
    public int unvield = 0;

    public static void main(String... args) {
        launch(args);
    }

    static void setFrame(Card card) {

    }

    public void start(Stage primaryStage) {
        Board game = new Board(4,4);
        game.generateBoard();

        Text title = new Text("Keep going...");
        title.setId("title");

        lastRound.add(new Card());
        lastRound.add(new Card());

        int dimx = game.getDimx();
        int dimy = game.getDimy();

        ArrayList<ArrayList<Button>> buttons = new ArrayList<>();
        HashMap<String, Image> frames = new HashMap<>();
        String[] frames_list = {"react", "angular", "django", "python", "java", "typescript", "javascript", "flutter"};

        frames.put("flipped", new Image("/images/flipped.png"));

        for (int i = 0; i < frames_list.length; i++) {
            frames.put(Integer.toString(i), new Image(String.format("/images/%s.png",frames_list[i])));
        }

        for (int i = 0; i < dimx; i++) {
            buttons.add(new ArrayList<>());
            for (int j = 0; j < dimy; j++) {
                Card card = game.getCard(i,j);
                buttons.get(i).add(card);
                Image img = card.isFlipped()?frames.get(Integer.toString(card.getIdCard())):frames.get("flipped");
                ImageView view = new ImageView(img);
                view.setFitHeight(100);
                view.setPreserveRatio(true);
                card.setPrefSize(100, 100);
                card.setGraphic(view);
                card.setText(card.toString());

                card.setOnAction(e -> {
                    if (clicks == 2) {
                        clicks = 0;
                        if (lastRound.get(0).getIdCard() != lastRound.get(1).getIdCard()) {
                            Image flippedimage = frames.get("flipped");
                            ImageView flippedview1 = new ImageView(flippedimage);
                            ImageView flippedview2 = new ImageView(flippedimage);
                            flippedview1.setFitHeight(100);
                            flippedview1.setPreserveRatio(true);
                            flippedview2.setFitHeight(100);
                            flippedview2.setPreserveRatio(true);
                            lastRound.get(0).hide();
                            lastRound.get(1).hide();
                            lastRound.get(0).setGraphic(flippedview1);
                            lastRound.get(1).setGraphic(flippedview2);
                        }
                    }

                    if (clicks < 2 && !card.isFlipped()) {
                        card.unveil();
                        Image unvieldImage = frames.get(Integer.toString(card.getIdCard()));
                        ImageView unvieldView = new ImageView(unvieldImage);
                        unvieldView.setFitHeight(100);
                        unvieldView.setPreserveRatio(true);
                        card.setGraphic(unvieldView);
                        lastRound.set(clicks, card);
                        clicks++;

                        if (clicks == 2) {
                            if (lastRound.get(0).getIdCard() != lastRound.get(1).getIdCard()) {
                                unvield += 2;
                                if (unvield == dimx*dimy) {
                                    System.out.println("You did it!");
                                    title.setText("Congrats");
                                }
                            };
                        }
                    }

                });
            }
        }

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(50, 50, 50, 50));

        //grid.add(title, 0,0, dimx, 1);
        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimy; j++) {
                grid.add(buttons.get(i).get(j), i, j, 1,1);
            }
        }

        Scene scene = new Scene(grid, 500, 500);
        scene.getStylesheets().add("/style.css");

        primaryStage.setTitle("Memory");
        primaryStage.onCloseRequestProperty()
                .setValue(e -> System.out.println("\nBye! See you later!"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
