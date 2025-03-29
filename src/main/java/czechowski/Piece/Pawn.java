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

    @Override
    public boolean isInTheWay(int targetCol, int targetRow) {


        return false;
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

                //checking if the last moved piece is a pawn
                if (GamePanel.lastMovedPiece instanceof Pawn) {

                    //checking if the last move was a two-step move
                    if (GamePanel.lastMovedPiece.twoStepped) {

                        //checking if current pawn is on the right side of the last moved pawn
                        if (this.preRow == GamePanel.lastMovedPiece.row && this.preCol == GamePanel.lastMovedPiece.col - 1) {
                            //checking if current pawn want's to capture the last moved pawn
                            if (targetRow == GamePanel.lastMovedPiece.row - 1 && targetCol == GamePanel.lastMovedPiece.col) {

                                GamePanel.enPassant = true;
                                return true;
                            }
                        }//checking if current pawn is on the left side of the last moved pawn
                        else if (this.preRow == GamePanel.lastMovedPiece.row && this.preCol == GamePanel.lastMovedPiece.col + 1) {
                            if (targetRow == GamePanel.lastMovedPiece.row - 1 && targetCol == GamePanel.lastMovedPiece.col) {

                                GamePanel.enPassant = true;
                                return true;
                            }
                        }
                    }


                }
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


                if (GamePanel.lastMovedPiece instanceof Pawn) {

                    //checking if the last move was a two-step move
                    if (GamePanel.lastMovedPiece.twoStepped) {

                        //checking if current pawn is on the right side of the last moved pawn
                        if (this.preRow == GamePanel.lastMovedPiece.row && this.preCol == GamePanel.lastMovedPiece.col - 1) {
                            //checking if current pawn want's to capture the last moved pawn
                            if (targetRow == GamePanel.lastMovedPiece.row +1 && targetCol == GamePanel.lastMovedPiece.col) {
                                GamePanel.enPassant = true;
                                return true;
                            }
                        }//checking if current pawn is on the left side of the last moved pawn
                        else if (this.preRow == GamePanel.lastMovedPiece.row && this.preCol == GamePanel.lastMovedPiece.col + 1) {
                            if (targetRow == GamePanel.lastMovedPiece.row +1 && targetCol == GamePanel.lastMovedPiece.col) {
                                GamePanel.enPassant = true;
                                return true;
                            }
                        }
                    }


                }



                // Diagonal capture for black
                if (colDiff == 1 && rowDiff == 1 && targetPiece != null && targetPiece.color == GamePanel.WHITE) {
                    return true;
                }
                // First move (two squares) for black
                return !moved && targetCol == preCol && rowDiff == 2 && targetPiece == null && isOccupied(preCol, preRow + 1) == null;
            }
        }
        return false;
    }


}
