package czechowski.Piece;

import czechowski.Board;
import czechowski.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Piece {

    public BufferedImage image;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;

    boolean moved = false;


    public Piece(int color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        preCol = col;
        preRow = row;
    }

    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public int getX(int col) {

        return col * Board.SQUARE_SIZE;
    }

    public int getY(int row) {

        return row * Board.SQUARE_SIZE;
    }

    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public void updatePosition() {
        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
        moved = true;
    }

    public boolean canMove(int targetCol, int targetRow, Piece piece) {
        return false;
    }

    public Piece isOccupied(int targetCol, int targetRow) {
        for (Piece piece : GamePanel.simpieces) {
            if (piece.col == targetCol && piece.row == targetRow && piece != this) {
                return piece;
            }
        }
        return null;
    }

    public boolean isInTheWay(int targetCol, int targetRow) {
        List<Point> route = new ArrayList<>();

        int dx = Integer.compare(targetCol, preCol);
        int dy = Integer.compare(targetRow, preRow);

        int x = preCol + dx;
        int y = preRow + dy;

        while (x != targetCol || y != targetRow) {
            route.add(new Point(x, y));
            x += dx;
            y += dy;
        }

        for (Point p : route) {

            for (Piece piece : GamePanel.simpieces) {
                if (piece.col == p.x && piece.row == p.y) {
                    return true;
                }
            }


        }
        return false;
    }


    public boolean isWithinBoard(int targetCol, int targetRow) {
        return targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7;
    }

    public void resetPosition() {
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }


}
