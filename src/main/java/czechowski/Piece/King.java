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

            if (!moved) {
                //left castling
                if (this.color == GamePanel.WHITE) {
                    if (targetCol == preCol - 2 && targetRow == preRow) {
                        for (Piece piece1 : GamePanel.simpieces) {
                            System.out.println(preCol);
                            if (piece1.col == preCol-4 && piece1.row == preRow && !piece1.moved) {
                                if (isOccupied(targetCol, targetRow) == null && isOccupied(preCol - 1, targetRow) == null && isOccupied(preCol - 2, targetRow) == null) {
                                    GamePanel.isCastling = true;
                                    return true;
                                }

                            }

                        }

                    } else if (targetCol == preCol + 2 && targetRow == preRow) {
                        for (Piece piece1 : GamePanel.simpieces) {
                            if (piece1.col == preCol-4 && piece1.row == preRow && !piece1.moved) {
                                if (isOccupied(targetCol, targetRow) == null && isOccupied(preCol + 1, targetRow) == null) {
                                    GamePanel.isCastling = true;
                                    return true;
                                }
                            }

                        }
                    }

                } else {
                    if (targetCol == preCol - 2 && targetRow == preRow) {
                        for (Piece piece1 : GamePanel.simpieces) {
                            if (piece1.col == preCol+3 && piece1.row == preRow && !piece1.moved) {
                                if (isOccupied(targetCol, targetRow) == null && isOccupied(preCol - 1, targetRow) == null && isOccupied(preCol - 2, targetRow) == null) {
                                    GamePanel.isCastling = true;
                                    return true;
                                }
                            }

                        }

                    } else if (targetCol == preCol + 2 && targetRow == preRow) {
                        for (Piece piece1 : GamePanel.simpieces) {
                            if (piece1.col == preCol+3 && piece1.row == preRow && !piece1.moved) {
                                if (isOccupied(targetCol, targetRow) == null && isOccupied(preCol + 1, targetRow) == null) {
                                    GamePanel.isCastling = true;
                                    return true;
                                }
                            }

                        }
                    }
                }

            }
            return Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1 || Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 1;
        }


        return false;
    }
}
