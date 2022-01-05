import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Board {
    int dimx = 4;
    int dimy = 4;
    int unvield = 0;
    int round = 0;
    ArrayList<ArrayList<Card>> board;

    public int getDimx() {
        return dimx;
    }

    public int getDimy() {
        return dimy;
    }

    public Card getCard(int i, int j) {
        return board.get(i).get(j);
    }

    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }

    public Board(int dimx, int dimy){
        this.dimx = dimx;
        this.dimy = dimy;

        board = new ArrayList<>();

        for (int i = 0; i < dimx; i++) {
            board.add(new ArrayList<Card>());
            for (int j = 0; j < dimy; j++) {
                board.get(i).add(new Card());
            }
        }
    }

    public static void main(String[] args) {
        Board game = new Board(4, 4);
        game.generateBoard();

        while (game.unvield < game.dimx * game.dimy ) {
            game.playTurn();
            System.out.println(game);
        }

        System.out.println("Congrats you did it!");
    }

    private Boolean exist_twice(int x) {
        int count = 0;
        for (ArrayList<Card> arr: this.board) {
            for (Card i: arr) {
                if (x == i.getIdCard()) {
                    count++;
                    if (count == 2) return true;
                };
            }
        }
        return false;
    }

    private Card flipCard() {
        int x, y;
        System.out.println("Choose a case: ");
        Scanner input = new Scanner(System.in);
        // input the coordinate
        x = input.nextInt();
        y = input.nextInt();

        Card card = board.get(x).get(y);

        if (card.isFlipped()) {
            System.out.println("already flipped");
            flipCard();
        }
        card.unveil();
        return card;
    }

    private void playTurn() {
        System.out.println(String.format("round %d", round));
        Card card1 = flipCard();
        System.out.println(this);
        Card card2 = flipCard();
        System.out.println(this);

        if (card1.getId() != card2.getId()) {
            card1.hide();
            card2.hide();
        } else {
            unvield += 2;
        }

        round++;
    }

    public void generateBoard() {
        // this function generates the board of the game randomly
        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimy; j++) {
                board.get(i).get(j).setIdCard(-1);
            }
        }
        Random generator = new Random();
        int count = 0;
        int position;
        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimy; j++) {
                do {
                    position = generator.nextInt((dimx*dimy)/2);
                } while(exist_twice(position));
                count++;
                board.get(i).get(j).setIdCard(position);
            }
        }

    }

    @Override
    public String toString() {
        String output = "";

        for (ArrayList<Card> arr: this.board) {
            for (Card i: arr) {
                output = output + i.toString() + " ";
            }
            output = output + "\n";
        }

        return output;
    }
}
