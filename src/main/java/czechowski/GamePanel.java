package czechowski;

import czechowski.Piece.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    //color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    //pieces
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simpieces = new ArrayList<>();
    public static ArrayList<Piece> promotionpieces = new ArrayList<>();
    final int FPS = 60;
    public int currentColor = WHITE;
    public static Piece lastMovedPiece;

    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();
    public static Piece activePiece = null;
    boolean canMove;
    boolean validSquare;
    public static boolean isCastling = false;
    public static boolean enPassant = false;
    public static boolean promotion = false;
    public static boolean isInCheck = false;
    public static boolean gameOver = false;
    public static boolean stalemate = false;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        setPieces();
        copyPieces(pieces, simpieces);

    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Initializes and places all chess pieces in their starting positions on the board.
     * Creates both white and black pieces including pawns, rooks, knights, bishops, queens, and kings.
     */
    public void setPieces() {
        //white pieces
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new King(WHITE, 4, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        // black pieces

        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new King(BLACK, 4, 0));
        pieces.add(new Queen(BLACK, 3, 0));
    }
    /**
     * Creates a copy of pieces from the source collection to the target collection.
     * Used for simulation and move validation purposes.
     *
     * @param source The collection of pieces to copy from
     * @param target The collection to copy pieces to
     */
    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();
        target.addAll(source);
    }

    //game loop
    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

    }

    /**
     * Updates the game state based on mouse input and piece movement.
     * Handles piece selection, movement validation, captures, special moves (castling, en passant, promotion),
     * and checks for check, checkmate, and stalemate conditions.
     */
    private void update() {
        if (mouse.pressed) {

            if (activePiece == null) {
                // Select piece if none is active
                for (Piece piece : simpieces) {
                    if (piece.color == currentColor &&
                            piece.col == mouse.x / Board.SQUARE_SIZE &&
                            piece.row == mouse.y / Board.SQUARE_SIZE) {
                        activePiece = piece;
                        break;
                    }
                }
            } else {
                simulate();
            }
        }

        if (!mouse.pressed) {
            if (activePiece != null) {
                if (validSquare) {
                    // Check if piece stayed in same position
                    if (activePiece.col == activePiece.preCol && activePiece.row == activePiece.preRow) {
                        activePiece.updatePosition();
                    } else {
                        // Handle capture logic
                        Piece occupyingPiece = activePiece.isOccupied(activePiece.col, activePiece.row);

                        // Remove opponent's piece if present
                        if (occupyingPiece != null && occupyingPiece.color != activePiece.color) {
                            simpieces.remove(occupyingPiece);
                        }


                        //check if the move is a castling move

                        if (enPassant) {
                            if (Math.abs(lastMovedPiece.row - activePiece.row) == 1 && lastMovedPiece.col == activePiece.col) {
                                simpieces.remove(lastMovedPiece);
                                enPassant = false;
                            }


                        }
                        if (isCastling) {
                            if (activePiece.col == 2 && activePiece.row == 7) {


                                for (Piece piece : simpieces) {
                                    if (piece.col == 0 && piece.row == 7) {
                                        piece.col = 3;
                                        piece.updatePosition();
                                    }
                                }
                                isCastling = false;
                            } else if (activePiece.col == 6 && activePiece.row == 7) {
                                for (Piece piece : simpieces) {
                                    if (piece.col == 7 && piece.row == 7) {
                                        piece.col = 5;
                                        piece.updatePosition();
                                    }
                                }
                                isCastling = false;
                            } else if (activePiece.col == 2 && activePiece.row == 0) {
                                for (Piece piece : simpieces) {
                                    if (piece.col == 0 && piece.row == 0) {
                                        piece.col = 3;
                                        piece.updatePosition();
                                    }
                                }
                                isCastling = false;
                            } else if (activePiece.col == 6 && activePiece.row == 0) {
                                for (Piece piece : simpieces) {
                                    if (piece.col == 7 && piece.row == 0) {
                                        piece.col = 5;
                                        piece.updatePosition();
                                    }
                                }
                                isCastling = false;
                            }
                        }
                        //finalizing the move
                        activePiece.updatePosition();
                        if (activePiece instanceof Pawn && (activePiece.row == 7 || activePiece.row == 0)) {
                            promotion = true;
                        }
                        lastMovedPiece = activePiece;
                        //checking if promoting
                        if (promotion) {
                            setPromotion(activePiece.col, activePiece.row);
                            activePiece.updatePosition();

                        }
                        checkForCheck();
                        activePiece = null;
                        if (isInCheck) {
                            currentColor = (currentColor == WHITE) ? BLACK : WHITE;
                            if (isCheckmate()) {

                                // Handle checkmate (e.g., display message, end game)
                                gameOver = true;
                            }
                            currentColor = (currentColor == WHITE) ? BLACK : WHITE;


                        }


                        // Switch turns
                        currentColor = (currentColor == WHITE) ? BLACK : WHITE;
                        if (stalemate()) {
                            stalemate = true;
                        }

                    }
                } else {
                    // Reset position for invalid moves
                    activePiece.resetPosition();
                    activePiece = null;
                }
            }
        }
    }

    /**
     * Simulates piece movement based on mouse position.
     * Validates if the move is legal according to chess rules and updates the piece's visual position.
     * Checks if the destination square is valid and if the move would leave the king in check.
     */
    private void simulate() {
        canMove = false;
        validSquare = false;

        activePiece.x = mouse.x - Board.SQUARE_SIZE / 2;
        activePiece.y = mouse.y - Board.SQUARE_SIZE / 2;
        activePiece.col = activePiece.getCol(activePiece.x);
        activePiece.row = activePiece.getRow(activePiece.y);

        if (activePiece.canMove(activePiece.col, activePiece.row, activePiece)) {
            if (!activePiece.isInTheWay(activePiece.col, activePiece.row)) {
                Piece occupyingPiece = activePiece.isOccupied(activePiece.col, activePiece.row);
                if (occupyingPiece == null || occupyingPiece.color != activePiece.color) {
                    // Add this check to prevent moving into check
                    if (wouldBeInCheck(activePiece, activePiece.col, activePiece.row)) {
                        canMove = true;
                        validSquare = true;
                    }
                }
            }
        }
    }

    /**
     * Checks if a move would leave the king in check.
     * This function is used for validating moves and determining checkmate.
     *
     * @param pieceToMove The piece that is being moved
     * @param targetCol   The target column for the move
     * @param targetRow   The target row for the move
     * @return true if the move would leave the king in check, false otherwise
     */
    private boolean wouldBeInCheck(Piece pieceToMove, int targetCol, int targetRow) {
        // Check if the piece can move according to its movement rules
        if (!pieceToMove.canMove(targetCol, targetRow, pieceToMove)) {
            return false; // Piece can't move there, so we treat it as "king would be in check"
        }

        // Check if there are any obstacles in the path of movement
        if (pieceToMove.isInTheWay(targetCol, targetRow)) {
            return false; // Obstacle in the path, so we treat it as "king would be in check"
        }

        // Store original position
        int originalCol = pieceToMove.col;
        int originalRow = pieceToMove.row;

        // Check for and store any captured piece
        Piece capturedPiece = null;
        for (Piece piece : simpieces) {
            if (piece != pieceToMove && piece.col == targetCol && piece.row == targetRow) {
                // Check if it's a piece of the same color (can't capture own pieces)
                if (piece.color == pieceToMove.color) {
                    return false; // Can't capture own piece, so we treat it as "king would be in check"
                }
                capturedPiece = piece;
                break;
            }
        }

        // Temporarily make the move
        pieceToMove.col = targetCol;
        pieceToMove.row = targetRow;
        if (capturedPiece != null) {
            simpieces.remove(capturedPiece);
        }

        // Find the king of the current player
        Piece king = null;
        for (Piece piece : simpieces) {
            if (piece instanceof King && piece.color == currentColor) {
                king = piece;
                break;
            }
        }

        // Check if any opponent's piece can attack the king
        boolean inCheck = false;
        int opponentColor = (currentColor == WHITE) ? BLACK : WHITE;

        for (Piece piece : simpieces) {
            if (piece.color == opponentColor) {
                assert king != null;
                if (piece.canMove(king.col, king.row, piece) &&
                        !piece.isInTheWay(king.col, king.row)) {
                    inCheck = true;
                    break;
                }
            }
        }

        // Restore the original board state
        pieceToMove.col = originalCol;
        pieceToMove.row = originalRow;
        if (capturedPiece != null) {
            simpieces.add(capturedPiece);
        }

        return !inCheck;
    }

    /**
     * Determines if the current player is in checkmate.
     * Checks if the king is in check and if there are any legal moves that can get the king out of check.
     * If no legal moves exist to escape check, the player is in checkmate.
     *
     * @return true if the current player is in checkmate, false otherwise
     */
    public boolean isCheckmate() {
        // First verify the king is in check
        Piece king = getKing();
        if (!isInCheck) {
            return false;
        }


        // Try every possible move for every piece of the current player
        for (Piece piece : new ArrayList<>(simpieces)) {
            if (piece.color == currentColor) {
                // Store original position


                // Try all possible squares
                for (int col = 0; col < board.MAX_COL; col++) {
                    for (int row = 0; row < board.MAX_ROW; row++) {
                        assert king != null;
                        if (king.col == col && king.row == row) {
                            continue;
                        }
                        if (wouldBeInCheck(piece, col, row)) {


                            return false;
                        }

                    }
                }
            }
        }

        // If we get here, no legal moves exist to get out of check

        return true;
    }
    /**
     * Determines if the current game state is a stalemate.
     * A stalemate occurs when the current player is not in check but has no legal moves.
     * Checks all possible moves for all pieces of the current player.
     *
     * @return true if the game is in stalemate, false otherwise
     */
    public boolean stalemate() {
        // First, check if the current player's king is in check
        if (isInCheck) {
            return false; // If the king is in check, it's not a stalemate
        }

        // Try every possible move for every piece of the current player
        for (Piece piece : new ArrayList<>(simpieces)) {
            if (piece.color == currentColor) {
                // Try all possible squares
                for (int col = 0; col < board.MAX_COL; col++) {
                    for (int row = 0; row < board.MAX_ROW; row++) {
                        // Check if the move is legal and doesn't put the king in check
                        if (piece.canMove(col, row, piece) &&
                                !piece.isInTheWay(col, row) &&
                                wouldBeInCheck(piece, col, row)) {
                            return false; // Found a legal move, not a stalemate
                        }
                    }
                }
            }
        }

        // If we get here, no legal moves exist for the current player and their king is not in check
        return true;
    }

    /**
     * Handles the pawn promotion process when a pawn reaches the opposite end of the board.
     * Displays promotion piece options and waits for player selection.
     *
     * @param col The column position of the pawn to be promoted
     * @param row The row position of the pawn to be promoted
     */
    public void setPromotion(int col, int row) {
        addPromotionPieces(currentColor);
        repaint();
        simpieces.remove(activePiece);
        activePiece = null;
        while (promotion) {
            handleMousePressForPromotion(col, row);
        }
    }
    /**
     * Adds the promotion piece options (Knight, Rook, Queen, Bishop) to the promotion pieces list.
     * These pieces are displayed for selection when a pawn is being promoted.
     *
     * @param color The color of the promotion pieces (WHITE or BLACK)
     */
    private void addPromotionPieces(int color) {
        promotionpieces.add(new Knight(color, 9, 2));
        promotionpieces.add(new Rook(color, 9, 3));
        promotionpieces.add(new Queen(color, 9, 4));
        promotionpieces.add(new Bishop(color, 9, 5));
    }
    /**
     * Handles mouse input during the pawn promotion process.
     * Waits for the player to select a promotion piece and replaces the pawn with the selected piece.
     *
     * @param col The column position where the promoted piece will be placed
     * @param row The row position where the promoted piece will be placed
     */
    private void handleMousePressForPromotion(int col, int row) {
        try {
            Thread.sleep(10); //
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        if (mouse.pressed) {
            for (Piece piece : promotionpieces) {
                if (piece.col == mouse.x / Board.SQUARE_SIZE && piece.row == mouse.y / Board.SQUARE_SIZE) {
                    activePiece = piece;
                    activePiece.col = col;
                    activePiece.row = row;
                    simpieces.add(activePiece);
                    promotionpieces.clear();
                    promotion = false;
                    break;
                }
            }
        }
    }
    /**
     * Finds and returns the king piece of the current player.
     *
     * @return The king piece of the current player, or null if not found
     */
    private Piece getKing() {
        for (Piece piece : simpieces) {
            if (piece instanceof King && piece.color == currentColor) {
                return piece;
            }
        }
        return null;
    }
    /**
     * Checks if the opponent's king is in check after the current player's move.
     * A king is in check when it can be captured by an opponent's piece on their next move.
     * Sets the isInCheck flag accordingly.
     */
    public void checkForCheck() {
        // Check if the OPPONENT's king is in check after current player's move
        // (before switching turns)
        int opponentColor = (currentColor == WHITE) ? BLACK : WHITE;

        int kingOpponentCol = -1;
        int kingOpponentRow = -1;

        // Find the opponent's king
        for (Piece piece : simpieces) {
            if (piece instanceof King && piece.color == opponentColor) {
                kingOpponentCol = piece.col;
                kingOpponentRow = piece.row;
                break;
            }
        }

        if (kingOpponentCol == -1 || kingOpponentRow == -1) {
            return; // King not found
        }

        // Check if any of current player's pieces can attack opponent's king
        for (Piece piece : simpieces) {
            if (piece.color == currentColor) {
                if (piece.canMove(kingOpponentCol, kingOpponentRow, piece) &&
                        !piece.isInTheWay(kingOpponentCol, kingOpponentRow)) {
                    isInCheck = true;

                    return;
                }
            }
        }

        // No pieces can attack the king
        isInCheck = false;
    }


    /**
     * Renders the game board, pieces, and game state information.
     * Handles special rendering for active pieces, promotion options, and game end messages.
     *
     * @param g The Graphics context to paint on
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        board.draw(g2);
        for (Piece piece : simpieces) {
            piece.draw(g2);
        }
        if (promotion) {
            for (Piece piece : promotionpieces) {
                piece.draw(g2);
            }
        }

        if (activePiece != null) {
            if (canMove) {
                g2.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activePiece.col * Board.SQUARE_SIZE, activePiece.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                activePiece.draw(g2);

            }
        }


        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g2.setColor(Color.white);
        if (currentColor == WHITE) {
            g2.drawString("White's turn", 890, 50);
        } else {
            g2.drawString("Black's turn", 890, 50);
        }

        if (gameOver) {
            String s ;
            if (currentColor == WHITE) {
                s = "Black wins!";
            } else {
                s = "White wins!";
            }
            g2.setFont(new Font("SansSerif", Font.PLAIN, 90));
            g2.setColor(Color.GREEN);
            g2.drawString(s, 200, 420);
        }
        if (stalemate) {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 90));
            g2.setColor(Color.GREEN);
            g2.drawString("Stalemate!", 200, 420);
        }

    }


}
