import java.util.ArrayList;
import java.util.Random;


public class Board {
    int dimx, dimy;
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

    public Board(int dimx, int dimy){
        this.dimx = dimx;
        this.dimy = dimy;

        board = new ArrayList<>();

        for (int i = 0; i < dimx; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < dimy; j++) {
                board.get(i).add(new Card());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("This is the board app!");
    }

    private Boolean exist_twice(int x) {
        int count = 0;
        for (ArrayList<Card> arr: this.board) {
            for (Card i: arr) {
                if (x == i.getIdCard()) {
                    count++;
                    if (count == 2) return true;
                }
            }
        }
        return false;
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
