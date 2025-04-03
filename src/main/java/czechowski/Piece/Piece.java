package czechowski.Piece;

import czechowski.Board;
import czechowski.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.Objects;

/**
 * Base class for all chess pieces in the game.
 * Provides common functionality for movement, position tracking, collision detection,
 * and rendering of chess pieces.
 */
public class Piece {

    public BufferedImage image;
    public int x, y;                  // Pixel coordinates on the screen
    public int col, row, preCol, preRow;  // Board coordinates (column and row)
    public int color;                 // Piece color (WHITE or BLACK)

    boolean moved = false;            // Tracks if the piece has moved (for special moves like castling)
    boolean twoStepped = false;       // Tracks if a pawn moved two squares (for en passant)

    /**
     * Creates a new chess piece with the specified color and position.
     *
     * @param color The color of the piece (WHITE or BLACK)
     * @param col The initial column position on the board
     * @param row The initial row position on the board
     */
    public Piece(int color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        preCol = col;
        preRow = row;
    }

    /**
     * Loads and returns an image for the chess piece from the specified path.
     *
     * @param imagePath The path to the image file (without the .png extension)
     * @return The loaded BufferedImage, or null if loading failed
     */
    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Converts a board column to a pixel x-coordinate.
     *
     * @param col The column on the chess board
     * @return The corresponding x-coordinate in pixels
     */
    public int getX(int col) {
        return col * Board.SQUARE_SIZE;
    }

    /**
     * Converts a board row to a pixel y-coordinate.
     *
     * @param row The row on the chess board
     * @return The corresponding y-coordinate in pixels
     */
    public int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }

    /**
     * Converts a pixel x-coordinate to a board column.
     *
     * @param x The x-coordinate in pixels
     * @return The corresponding column on the chess board
     */
    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    /**
     * Converts a pixel y-coordinate to a board row.
     *
     * @param y The y-coordinate in pixels
     * @return The corresponding row on the chess board
     */
    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    /**
     * Updates the piece's position after a move and sets appropriate flags.
     * Updates the pixel coordinates based on the new board position and
     * tracks special move conditions like two-step pawn moves.
     */
    public void updatePosition() {
        twoStepped = this instanceof Pawn && Math.abs(row - preRow) == 2;

        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
        moved = true;
    }

    /**
     * Determines if the piece can move to the specified position according to chess rules.
     * This method is overridden by each specific piece type to implement their movement rules.
     *
     * @param targetCol The target column position
     * @param targetRow The target row position
     * @param piece The piece being moved
     * @return true if the move is valid according to the piece's movement rules, false otherwise
     */
    public boolean canMove(int targetCol, int targetRow, Piece piece) {
        return false;
    }

    /**
     * Checks if a square is occupied by another piece.
     *
     * @param targetCol The column to check
     * @param targetRow The row to check
     * @return The piece occupying the square, or null if the square is empty
     */
    public Piece isOccupied(int targetCol, int targetRow) {
        for (Piece piece : GamePanel.simpieces) {
            if (piece != this && piece.col == targetCol && piece.row == targetRow) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Checks if there are any pieces blocking the path between the current position and the target position.
     * Used for pieces like bishops, rooks, and queens that cannot jump over other pieces.
     *
     * @param targetCol The target column position
     * @param targetRow The target row position
     * @return true if there is a piece blocking the path, false otherwise
     */
    public boolean isInTheWay(int targetCol, int targetRow) {
        int dx = Integer.compare(targetCol, preCol);
        int dy = Integer.compare(targetRow, preRow);

        int x = preCol + dx;
        int y = preRow + dy;

        while (x != targetCol || y != targetRow) {
            for (Piece piece : GamePanel.simpieces) {
                if (piece.col == x && piece.row == y) {
                    return true;
                }
            }
            x += dx;
            y += dy;
        }
        return false;
    }

    /**
     * Checks if the target position is within the boundaries of the chess board.
     *
     * @param targetCol The target column position
     * @param targetRow The target row position
     * @return true if the position is within the board, false otherwise
     */
    public boolean isWithinBoard(int targetCol, int targetRow) {
        return targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7;
    }

    /**
     * Resets the piece's position to its previous position.
     * Used when a move is invalid or canceled.
     */
    public void resetPosition() {
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }

    /**
     * Draws the piece on the game board.
     *
     * @param g2 The Graphics2D context to draw on
     */
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }
}
