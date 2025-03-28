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
            Piece targetPiece = isOccupied(targetCol, targetRow);
            int colDiff = Math.abs(targetCol - preCol);
            int rowDiff = targetRow - preRow;

            if (piece.color == GamePanel.WHITE) {
                // Forward move for white
                if (targetCol == preCol && rowDiff == -1 && targetPiece == null) {
                    return true;
                }
                // Diagonal capture for white
                if (colDiff == 1 && rowDiff == -1 && targetPiece != null && targetPiece.color == GamePanel.BLACK) {
                    return true;
                }
                // First move (two squares) for white
                return !moved && targetCol == preCol && rowDiff == -2 && targetPiece == null && isOccupied(preCol, preRow - 1) == null;
            } else {
                // Forward move for black
                if (targetCol == preCol && rowDiff == 1 && targetPiece == null) {
                    return true;
                }
                // Diagonal capture for black
                if (colDiff == 1 && rowDiff == 1 && targetPiece != null && targetPiece.color == GamePanel.WHITE) {
                    return true;
                }
                // First move (two squares) for black
                if (!moved && targetCol == preCol && rowDiff == 2 && targetPiece == null && isOccupied(preCol, preRow + 1) == null) {
                    return true;
                }
            }
        }
        return false;
    }



}
