import javax.swing.*;

public class Tile extends JButton {
    private int row, column; //To find the position
    private int count; //number of explosive neighbors
    private boolean isRevealed;
    boolean isMine;
    boolean flagged;

    public Tile(int r, int c){
        row = r;
        column = c;
    }

    public void toggle(){
        flagged = !flagged;
        if(flagged){
            this.setText("FLAG");
        }
        else {
            if (this.getCount() == 0 || this.isEnabled() || this.getText().equals("BOMB")) {
                this.setText("");
            } else{
                this.setText(this.getCount()+"");
            }
        }
    }

    //getters and setters;


    public boolean isRevealed() {
        return(this.isEnabled());
    }

    public int getColumn() {
        return column;
    }


    public int getRow() {
        return row;
    }

    public int getCount() {
        return count;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRow(int row) {
        this.row = row;
    }


}
