package czechowski.Piece;


import czechowski.GamePanel;

public class Pawn extends Piece {


    public Pawn(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.WHITE) {
            image = getImage("/white_pawn");
        } else {
            image = getImage("/black_pawn");

        }

    }

    public boolean canMove(int targetCol, int targetRow, Piece piece) {

        if (isWithinBoard(targetCol, targetRow)) {
            // if the pawn is not moved yet it can move 1 or 2 squares
            if (!moved) {

                if (piece.color == GamePanel.WHITE) {
                    return (targetCol == preCol && targetRow == preRow - 2) || targetCol == preCol && targetRow == preRow - 1;
                } else {
                    return (targetCol == preCol && targetRow == preRow + 2) || targetCol == preCol && targetRow == preRow + 1;
                }
            } else {
                // if the pawn is moved it can move only 1 square
                if (piece.color == GamePanel.WHITE) {
                    return targetCol == preCol && targetRow == preRow - 1;
                } else {
                    return targetCol == preCol && targetRow == preRow + 1;
                }
            }

        }
        return false;
    }
}
