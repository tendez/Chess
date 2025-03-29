package czechowski.Piece;

import czechowski.GamePanel;


public class Queen extends Piece {


    public Queen(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.WHITE) {
            image = getImage("/white_queen");
        } else {
            image = getImage("/black_queen");

        }

    }


    public boolean canMove(int targetCol, int targetRow, Piece piece) {

        if (isWithinBoard(targetCol, targetRow)) {

            return (targetCol == preCol || targetRow == preRow) || targetCol - preCol == targetRow - preRow || targetCol - preCol == preRow - targetRow;


        }
        return false;
    }
}
