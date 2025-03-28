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
    final int FPS = 60;
    public int currentColor = WHITE;

    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();
    Piece activePiece = null;
    boolean canMove;
    boolean validSquare;
    public static boolean  isCastling = false;

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

    //setting pieces
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

                        // Finalize move
                        if(isCastling) {
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
                            } else if (activePiece.col==2 && activePiece.row == 0) {
                                for (Piece piece : simpieces) {
                                    if (piece.col == 0 && piece.row == 0) {
                                        piece.col = 3;
                                        piece.updatePosition();
                                    }
                                }
                                isCastling = false;
                            }
                            else if (activePiece.col==6 && activePiece.row == 0) {
                                for (Piece piece : simpieces) {
                                    if (piece.col == 7 && piece.row == 0) {
                                        piece.col = 6;
                                        piece.updatePosition();
                                    }
                                }
                                isCastling = false;
                            }
                        }
                        activePiece.updatePosition();
                        activePiece = null;

                        // Switch turns
                        currentColor = (currentColor == WHITE) ? BLACK : WHITE;
                    }
                } else {
                    // Reset position for invalid moves
                    activePiece.resetPosition();
                    activePiece = null;
                }
            }
        }
    }

    //simulate movement
    private void simulate() {
        canMove = false;
        validSquare = false;

        // Update piece position based on mouse coordinates
        activePiece.x = mouse.x - Board.SQUARE_SIZE / 2;
        activePiece.y = mouse.y - Board.SQUARE_SIZE / 2;
        activePiece.col = activePiece.getCol(activePiece.x);
        activePiece.row = activePiece.getRow(activePiece.y);

        // Check if move is valid
        if (activePiece.canMove(activePiece.col, activePiece.row, activePiece)) {
            // Check for obstacles
            if (!activePiece.isInTheWay(activePiece.col, activePiece.row)) {
                Piece occupyingPiece = activePiece.isOccupied(activePiece.col, activePiece.row);

                // Allow move if square is empty or contains opponent's piece
                if (occupyingPiece == null || occupyingPiece.color != activePiece.color) {
                    canMove = true;
                    validSquare = true;


                }
            }
        }
    }


    //painting components
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        board.draw(g2);
        for (Piece piece : simpieces) {
            piece.draw(g2);
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
    }

}
