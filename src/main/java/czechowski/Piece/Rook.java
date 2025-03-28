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

    public boolean isInTheWay(int targetCol, int targetRow) {
        List<Point> sciezka = new ArrayList<>();

        int dx = Integer.compare(targetCol, preCol);
        int dy = Integer.compare(targetRow, preRow);

        int x = preCol + dx;
        int y = preRow + dy;

        while (x != targetCol || y != targetRow) {
            sciezka.add(new Point(x, y));
            x += dx;
            y += dy;
        }

        for (Point p : sciezka) {

            for (Piece piece : GamePanel.simpieces) {
                if (piece.col == p.x && piece.row == p.y) {
                    return true;
                }
            }


        }
        return false;
    }

    public boolean canMove(int targetCol, int targetRow, Piece piece) {

        if (isWithinBoard(targetCol, targetRow)) {

                return targetCol == preCol || targetRow == preRow;


        }
        return false;
    }
}
