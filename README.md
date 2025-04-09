# Chess Game ♟️

A fully functional chess game implemented in Java with a graphical user interface. This project follows standard chess rules including special moves like castling, en passant, and pawn promotion.

## ✨ Features

- ✅ Complete implementation of chess rules  
- 🎨 Graphical user interface using Java Swing  
- 🔁 Special moves: castling, en passant, pawn promotion  
- ♚ Check, checkmate, and stalemate detection  
- 🔄 Turn-based gameplay  

## 🗂️ Project Structure

The project is organized in the `czechowski` package with the following main components:

- `Main.java` - Entry point that sets up the game window  
- `GamePanel.java` - Core game logic and rendering  
- `Board.java` - Chess board representation and rendering  
- `Mouse.java` - Mouse input handling  

### ♟️ Piece package

Contains all chess piece implementations:

- `Piece.java` - Base class for all chess pieces  
- Individual piece classes:  
  - `Pawn.java`  
  - `Rook.java`  
  - `Knight.java`  
  - `Bishop.java`  
  - `Queen.java`  
  - `King.java`  

## ▶️ How to Run

### ✅ Prerequisites

- Java JDK 8 or higher  
- Maven  

### 🔧 Building and Running

1. Clone the repository:
   ```bash
   git clone [repository-url]
   ```

2. Navigate to the project directory:
   ```bash
   cd chess-game
   ```

3. Build the project with Maven:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   java -cp target/chess-game-1.0.jar czechowski.Main
   ```

## 📜 Game Rules

- White moves first  
- Players take turns moving their pieces  
- A piece cannot move through other pieces (except for the knight)  
- When a king is under attack (in check), the player must make a move to get out of check  
- If a player cannot make any legal move and their king is in check, it's checkmate and the game ends  
- If a player cannot make any legal move but their king is not in check, it's stalemate and the game ends in a draw  

## 🔮 Future Improvements

- ⏱️ Adding a timer for timed matches  
- 📝 Implementing move history and notation  
- 💾 Adding save/load game functionality  
- 🌐 Network play for online matches  
- 🤖 AI opponent  

## 🛠️ Technologies Used

- Java  
- Swing (for GUI)  
- Maven (for build and dependency management)  
