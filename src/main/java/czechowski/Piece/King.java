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

    @Override
    public boolean isInTheWay(int targetCol, int targetRow) {


        return false;
    }

    public boolean canMove(int targetCol, int targetRow, Piece piece) {

        if (isWithinBoard(targetCol, targetRow)) {

            if (!moved) {
                //left castling
                if (this.color == GamePanel.WHITE) {
                    if (targetCol == preCol - 2 && targetRow == preRow) {
                        //checking if rook hasn't moved
                        for (Piece piece1 : GamePanel.simpieces) {

                            if (piece1.col == preCol - 4 && piece1.row == preRow && !piece1.moved) {
                                //checking if squares between king and rook are empty
                                if (isOccupied(targetCol, targetRow) == null && isOccupied(preCol - 1, targetRow) == null && isOccupied(preCol - 2, targetRow) == null) {
                                    //checking if any square between king and rook is attacked by black piece
                                    for (Piece piece2 : GamePanel.simpieces) {
                                        if (piece2.color == GamePanel.BLACK &&
                                                ((piece2.canMove(3, 7, piece2) && !piece2.isInTheWay(3, 7))
                                                        || (piece2.canMove(2, 7, piece2) && !piece2.isInTheWay(2, 7))
                                                        || (piece2.canMove(1, 7, piece2) && !piece2.isInTheWay(1, 7))
                                                        || (piece2.canMove(4, 7, piece2) && !piece2.isInTheWay(4, 7)))) {
                                            {
                                                System.out.println(piece2.isInTheWay(3, 7));
                                                System.out.println(piece2 + " " + piece2.x + " " + piece2.y);
                                                return false;
                                            }
                                        }
                                    }
                                    GamePanel.isCastling = true;
                                    return true;
                                }

                            }

                        }

                    } else if (targetCol == preCol + 2 && targetRow == preRow) {
                        for (Piece piece1 : GamePanel.simpieces) {
                            if (piece1.col == preCol - 4 && piece1.row == preRow && !piece1.moved) {
                                if (isOccupied(targetCol, targetRow) == null && isOccupied(preCol + 1, targetRow) == null) {
                                    for (Piece piece2 : GamePanel.simpieces) {
                                        if (piece2.color == GamePanel.BLACK &&
                                                ((piece2.canMove(4, 7, piece2) && piece2.isInTheWay(4, 7))
                                                        || (piece2.canMove(5, 7, piece2) && piece2.isInTheWay(5, 7))
                                                        || (piece2.canMove(6, 7, piece2) && piece2.isInTheWay(6, 7)))) {
                                            {

                                                return false;
                                            }
                                        }
                                    }
                                    GamePanel.isCastling = true;
                                    return true;
                                }
                            }

                        }
                    }

                } else {
                    if (targetCol == preCol - 2 && targetRow == preRow) {
                        for (Piece piece1 : GamePanel.simpieces) {
                            if (piece1.col == preCol + 3 && piece1.row == preRow && !piece1.moved) {
                                if (isOccupied(targetCol, targetRow) == null && isOccupied(preCol - 1, targetRow) == null && isOccupied(preCol - 2, targetRow) == null) {
                                    for (Piece piece2 : GamePanel.simpieces) {
                                        if (piece2.color == GamePanel.BLACK &&
                                                ((piece2.canMove(3, 0, piece2) && piece2.isInTheWay(3, 0))
                                                        || (piece2.canMove(2, 0, piece2) && piece2.isInTheWay(2, 0))
                                                        || (piece2.canMove(1, 0, piece2) && piece2.isInTheWay(1, 0))
                                                        || (piece2.canMove(4, 0, piece2) && piece2.isInTheWay(4, 0)))) {
                                            {

                                                return false;
                                            }
                                        }
                                    }
                                    GamePanel.isCastling = true;
                                    return true;
                                }
                            }

                        }

                    } else if (targetCol == preCol + 2 && targetRow == preRow) {
                        for (Piece piece1 : GamePanel.simpieces) {
                            if (piece1.col == preCol + 3 && piece1.row == preRow && !piece1.moved) {
                                if (isOccupied(targetCol, targetRow) == null && isOccupied(preCol + 1, targetRow) == null) {
                                    for (Piece piece2 : GamePanel.simpieces) {
                                        if (piece2.color == GamePanel.BLACK &&
                                                ((piece2.canMove(6, 0, piece2) && piece2.isInTheWay(6, 0))
                                                        || (piece2.canMove(5, 0, piece2) && piece2.isInTheWay(5, 0))
                                                        || (piece2.canMove(4, 0, piece2) && piece2.isInTheWay(4, 0)))) {
                                            {

                                                return false;
                                            }
                                        }
                                    }
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
