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

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    GridPane grid = new GridPane();
    GridPane titleGrid = new GridPane();
    GridPane bottomgrid = new GridPane();

    public int clicks = 0;
    public ArrayList<Card> lastRound = new ArrayList<>();
    public int unvield = 0;

    public static void main(String... args) {
        launch(args);
    }

    static void setFrame(Card card, HashMap<String, Image> frames) {
        Image img = card.isFlipped() ? frames.get(Integer.toString(card.getIdCard())):frames.get("flipped");
        ImageView view = new ImageView(img);
        view.setFitHeight(100);
        view.setPreserveRatio(true);
        card.setGraphic(view);
    }

    public void start(Stage primaryStage) {
        Board game = new Board(4,4);
        game.generateBoard();

        Text title = new Text("Keep going...");
        title.setId("title");

        Text score = new Text(String.format("Score %d", unvield));

        lastRound.add(new Card());
        lastRound.add(new Card());

        int dimx = game.getDimx();
        int dimy = game.getDimy();

        ArrayList<ArrayList<Card>> buttons = new ArrayList<>();
        HashMap<String, Image> frames = new HashMap<>();
        String[] frames_list = {"react", "angular", "django", "python", "java", "typescript", "javascript", "flutter"};

        frames.put("flipped", new Image("/images/flipped.png"));
        frames.put("refresh", new Image("/images/refresh.png"));

        for (int i = 0; i < frames_list.length; i++) {
            frames.put(Integer.toString(i), new Image(String.format("/images/%s.png",frames_list[i])));
        }

        for (int i = 0; i < dimx; i++) {
            buttons.add(new ArrayList<>());
            for (int j = 0; j < dimy; j++) {
                Card card = game.getCard(i,j);
                buttons.get(i).add(card);
                card.setPrefSize(100, 100);
                setFrame(card, frames);

                card.setOnAction(e -> {
                    if (clicks == 2) {
                        clicks = 0;
                        if (lastRound.get(0).getIdCard() != lastRound.get(1).getIdCard()) {
                            lastRound.get(0).hide(); lastRound.get(1).hide();
                            setFrame(lastRound.get(0), frames); setFrame(lastRound.get(1), frames);
                        }
                    }

                    if (clicks < 2 && !card.isFlipped()) {
                        card.unveil();
                        setFrame(card, frames);
                        lastRound.set(clicks, card);
                        clicks++;

                        if (clicks == 2) {
                            if (lastRound.get(0).getIdCard() == lastRound.get(1).getIdCard()) {
                                unvield += 2;
                                score.setText(String.format("Score %d", unvield));
                                if (unvield == dimx*dimy) {
                                    System.out.println("You did it!");
                                    title.setFill(Color.web("#FF7F59"));
                                    title.setText("\uD83C\uDF89 Congrats \uD83C\uDF89");
                                }
                            };
                        }
                    }
                });
            }
        }

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(50, 50, 50, 50));

        titleGrid.setAlignment(Pos.CENTER);
        titleGrid.setPadding(new Insets(10, 0, 10, 0));
        titleGrid.add(title, 0,0, 1,1);

        bottomgrid.setAlignment(Pos.CENTER);
        bottomgrid.setPadding(new Insets(10, 0, 10, 0));

        Button refresh = new Button();
        refresh.setPrefSize(5, 5);
        Image refreshImg = frames.get("refresh");
        ImageView refreshView = new ImageView(refreshImg);
        refreshView.setFitHeight(25);
        refreshView.setPreserveRatio(true);
        refresh.setGraphic(refreshView);
        refresh.setOnAction (e -> {
           unvield = 0;
           clicks = 0;
           game.generateBoard();
           title.setText("Keep Going");
           title.setFill(Color.web("gray"));
           score.setText(String.format("Score %d", unvield));
           for (int i = 0; i < dimx; i++) {
                for (int j = 0; j < dimy; j++) {
                    buttons.get(i).get(j).hide();
                    setFrame(buttons.get(i).get(j), frames);

                }
            }
        });
        bottomgrid.add(refresh, 0,0, 1,1);
        bottomgrid.add(score, 1,0, 3,1);


        grid.add(titleGrid,0, 0, dimx, 1);
        grid.add(bottomgrid, 0, dimy+1, dimx, 1);

        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimy; j++) {
                grid.add(buttons.get(i).get(j), i, j+1, 1,1);
            }
        }

        Scene scene = new Scene(grid, 500, 600);
        scene.getStylesheets().add("/style.css");

        primaryStage.setTitle("Memory");
        primaryStage.onCloseRequestProperty()
                .setValue(e -> System.out.println("\nBye! See you later!"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
