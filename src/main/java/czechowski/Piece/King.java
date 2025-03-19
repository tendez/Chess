package czechowski.Piece;

import czechowski.GamePanel;

public class King extends Piece {


    public King(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.WHITE) {
            image = getImage("/white_king");
        } else {
            image = getImage("/black_king");

        }

    }


    public boolean canMove(int targetCol, int targetRow, Piece piece) {

        if (isWithinBoard(targetCol, targetRow)) {
            return Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1 || Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 1;
        }
        return false;
    }
}
