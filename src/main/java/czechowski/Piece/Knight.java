package czechowski.Piece;

import czechowski.GamePanel;

public class Knight extends Piece {


    public Knight(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.WHITE) {
            image = getImage("/white_knight");
        } else {
            image = getImage("/black_knight");

        }

    }

    @Override
    public boolean isInTheWay(int targetCol, int targetRow) {


        return false;
    }


    public boolean canMove(int targetCol, int targetRow, Piece piece) {

        if (isWithinBoard(targetCol, targetRow)) {
            return (Math.abs(targetCol - preCol) == 1 && Math.abs(targetRow - preRow) == 2) || (Math.abs(targetCol - preCol) == 2 && Math.abs(targetRow - preRow) == 1);
        }
        return false;
    }
}
