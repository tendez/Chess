package czechowski.Piece;

import czechowski.GamePanel;


public class Bishop extends Piece {


    public Bishop(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.WHITE) {
            image = getImage("/white_bishop");
        } else {
            image = getImage("/black_bishop");

        }

    }


    public boolean canMove(int targetCol, int targetRow, Piece piece) {

        if (isWithinBoard(targetCol, targetRow)) {

            return targetCol - preCol == targetRow - preRow || targetCol - preCol == preRow - targetRow;

        }
        return false;
    }
}
