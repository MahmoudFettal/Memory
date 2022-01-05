import javafx.scene.control.Button;

public class Card extends Button{
    private int idCard = -1;
    private boolean flipped = false;
    private boolean found = false;

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public int getIdCard() {
        return idCard;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void unveil() {
        this.flipped = true;
    }

    public void hide() {
        this.flipped = false;
    }

    public void kill() { this.found = true; }

    public void revive() {this.found = false; }

    @Override
    public String toString() {
        if (found) return "empty";
        if (flipped) return Integer.toString(idCard);
        return "flipped";
    }
}
