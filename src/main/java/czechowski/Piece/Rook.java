package czechowski.Piece;

import czechowski.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {


    public Rook(int color, int col, int row) {
        super(color, col, row);
        if (color == GamePanel.WHITE) {
            image = getImage("/white_rook");
        } else {
            image = getImage("/black_rook");

        }


    }


    public boolean canMove(int targetCol, int targetRow, Piece piece) {

        if (isWithinBoard(targetCol, targetRow)) {

            return targetCol == preCol || targetRow == preRow;


        }
        return false;
    }
}
