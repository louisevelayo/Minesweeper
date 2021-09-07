import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.peer.MouseInfoPeer;

public class Board extends JPanel implements MouseListener {
    private Tile[][] board = new Tile[8][10];
    private int flagCount = 10;
    private int numberOfBombs = 10;
    private JLabel flagIndicator;
    private JFrame frame;
    private boolean firstClick = false;
    private boolean isOver = false;
    private String[] options = {"Yes", "No"};

    //generate mines
    public void generateMines() {
        int count = 0;
        while (count < this.numberOfBombs) {
            int row = (int) (Math.random() * board.length);
            int col = (int) (Math.random() * board[0].length);
            while (board[row][col].isMine) {
                row = (int) (Math.random() * board.length);
                col = (int) (Math.random() * board[0].length);
            }
            System.out.println("mines added");
            board[row][col].isMine = true;
            count++;
        }
    }

    public void countExplosiveNeighbours() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                updateCountExplosiveNeighbours(row, col);
            }
        }
    }

    public void updateCountExplosiveNeighbours(int r, int c) {
        int nrOfBombs = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (r + i >= 0 && r + i < board.length && c + j >= 0 && c + j < board[0].length) {
                    if (board[r + i][c + j].isMine) {
                        nrOfBombs++;
                    }
                }
            }
        }
        board[r][c].setCount(nrOfBombs);

    }

    public void showMines() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col].isMine) {
                    board[row][col].setText("BOMB");
                } else {
                    board[row][col].setText(board[row][col].getCount() + "");
                }
            }
        }
        System.out.println("mines painted");

    }

    public boolean canFlag() {
        return flagChecker() == 0;
    }


    public int flagChecker() {
        int count = 0;
        int temp = this.flagCount;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col].flagged) {
                    count++;
                }
            }
        }
        temp -= count;
        System.out.println(temp);
        System.out.println(this.flagCount);
        flagIndicator.setText("Flags: " + temp);
        return temp;

    }


    public Board() {
        frame = new JFrame("Minesweeper");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        flagIndicator = new JLabel("Flags: " + this.flagCount);
        flagIndicator.setPreferredSize(new Dimension(50,50));
        flagIndicator.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel bombIndicator = new JLabel("                                                Bombs: " + this.numberOfBombs);
        bombIndicator.setFont(new Font("Arial", Font.PLAIN, 20));
        JPanel header = new JPanel(new GridLayout(1,3));
        header.add(flagIndicator);
        int i = 1;
        int j = 3;
        JPanel[][] panelHolder = new JPanel[i][j];
        setLayout(new GridLayout(i,j));
        for(int m = 0; m < i; m++) {
            for(int n = 0; n <j; n++) {
                panelHolder[m][n] = new JPanel();
                add(panelHolder[m][n]);
            }
        }
        header.add(bombIndicator);

        JPanel panel = new JPanel(new GridLayout(8, 10));
        panel.setSize(800, 800);

        for (int row = 0; row < board.length; row++) { //goes through the big array
            for (int col = 0; col < board[0].length; col++) { //goes through the array at each index
                Tile t = new Tile(row, col);
                t.addMouseListener(this);
                panel.add(t); //adds tiles to the GUI
                board[row][col] = t; //adds the tile to the data structure (2D array)
            }
        }



        frame.add(header, BorderLayout.PAGE_START);
        frame.add(panel, BorderLayout.CENTER);

       /* JFrame frame = new JFrame("Minesweeper");
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");


        //add a layout
        frame.setLayout(new GridLayout(8,10));


        //add the board!
        for(int row = 0; row< board.length; row++){ //goes through the big array
            for(int col = 0; col<board[0].length; col++){ //goes through the array at each index
                Tile t = new Tile(row,col);
                t.addMouseListener(this);
                frame.add(t); //adds tiles to the GUI
                board[row][col] = t; //adds the tile to the data structure (2D array)

            }
        }*/


        //showMines();

        frame.setVisible(true);
    }

    public void gameOver() {
        int count = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col].isMine) {
                    showMines();
                }
            }
        }
        setIsOver(true);
        if (getIsOver()) {
            JOptionPane gameOver = new JOptionPane();
            int playAgain = gameOver.showOptionDialog(null, "Play Again?", "GAME OVER",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            if (playAgain == 0) {
                Board minesweeperNew = new Board();
            }
        }
    }
        //gameOver.showMessageDialog(frame, "Wrong click! Better luck next time.", "GAME OVER", JOptionPane.ERROR_MESSAGE);


    public void reveal(int r, int c) { //This method is an example of recursion
        if (r < 0 || r > board.length ||
                c < 0 || c > board[0].length || board[r][c].getText().length() > 0 || board[r][c].isMine
                || !board[r][c].isEnabled()) {
            return; //exit condition set 1
        }
        if (board[r][c].getCount() > 0) { // exit condition set 2
            board[r][c].setText(board[r][c].getCount() + "");
            board[r][c].setEnabled(false);
        } else {
            board[r][c].setEnabled(false); // recursive part
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (r + i >= 0 && r + i < board.length && c + j >= 0 && c + j < board[0].length) {
                        reveal(r + i, c + j);
                    }
                }
            }
        }

    }

    public boolean win(){
        return(tilesActive()==10);

    }

    public int tilesActive() {
        int count = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col].isEnabled()) {
                    count++;
                }
            }
        }
        System.out.println(count);
        return count;
    }



    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!firstClick){
            System.out.println("mines printed");
            generateMines();
            countExplosiveNeighbours();
            setFirstClick(true);
        }
        if (e.getButton() == 1) {
            System.out.println("left");
            Tile t = (Tile) (e.getComponent());
            if (t.isMine) {
                gameOver();
            } else {
                reveal(t.getRow(), t.getColumn());
            }
        } else if (e.getButton() == 3) {
            System.out.println("right");
            if (!canFlag()) {
                Tile t = (Tile) (e.getComponent());
                t.toggle();
            } else {
                Tile t = (Tile) (e.getComponent());
                if (t.getText().equals("FLAG")) {
                    t.toggle();
                } else {
                    System.out.println("No more flags available");
                }
            }
        }
        flagChecker();
        if (win()) {
            System.out.println("in Win");
            JOptionPane winMsg = new JOptionPane();
            int playAgain = winMsg.showOptionDialog(null, "Play Again?", "CONGRATULATIONS, YOU WON!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (playAgain == 0) {
                Board minesweeperNew = new Board();
            }
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setFirstClick(boolean firstClick) {
        this.firstClick = firstClick;
    }

    public void setIsOver(boolean over) {
        isOver = over;
    }

    public boolean getIsOver(){
        return this.isOver;
    }
}
