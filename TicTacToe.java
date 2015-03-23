/**
 * Represents the internal state of a TicTacToe game and contains gameplay
 * logic.
 */

import java.util.Arrays;
import java.util.HashMap;

public class TicTacToe {

  public enum Mark {
    X, O
  }

  private Mark[][] board;
  // default to O, since X is the first move by convention.
  private Mark lastMove = Mark.O;
  private int totalMarked = 0;

  public TicTacToe (){
    this.board = new Mark[3][3];
  }

  /**
   * Checks if this is a valid tic-tac-toe move.
   *
   * A move in tic-tac-toe is valid if:
   *  - The x and y value are within the range (0..2)
   *  - The mark being used is not the same as the last mark.
   *  - The space at x,y is unoccupied.
   *
   * @param x x-coordinate of the move being attempted
   * @param y y-coordinate of the move being attempted
   * @param mark
   * @return true if this move is valid; false otherwise.
   */
  public boolean isValidMove(int x, int y, Mark mark) {
    return ((x >= 0 && x <= 2 && y >= 0 && y <= 2)
        || (this.board[x][y] == null))
        && (this.lastMove != mark);
  }

  /**
   * Checks if this game was a tie.
   *
   * A tie occurs if all spaces on the board are marked, and neither player won.
   *
   * @return true if all spaces marked and no one won, false otherwise
   */
  public boolean checkTie() {
    return (this.checkWin() == null && this.totalMarked == 9);
  }

  /**
   * Checks if this game was a tie.
   *
   * Overloaded function just for if we've already checked for a win condition.
   *
   * @param winResult
   * @return true if tie, false otherwise
   */
  public boolean checkTie(Mark winResult) {
    return (winResult == null && this.totalMarked == 9);
  }

  /**
   * Checks for tic-tac-toe win condition.
   *
   * @return Mark.X or Mark.O if either mark has won, null otherwise.
   */
  public Mark checkWin() {
    // Can't have a win if at least one person didn't place at least 3 marks.
    if (this.totalMarked < 5) {
      return null;
    }
    for (int i = 0 ; i < 3 ; ++i) {
      // Check each column
      // [ 00 01 02 ] [ 10 11 12 ] [ 20 21 22 ]
      if ((board[i][0] != null)
          && (board[i][0] == board[i][1])
          && (board[i][1] == board[i][2])) {
        return board[i][0];
      }
      // Check each row
      // [ 00 10 20 ] [ 01 11 21 ] [ 02 12 22 ]
      else if ((board[0][i] != null)
          && (board[0][i] == board[1][i])
          && (board[1][i] == board[2][i])) {
        return board[0][i];
      }
    }
    // Check each diagonal
    // [ 00 11 22 ] [ 20 11 02]
    if ((board[0][0] != null)
        && (board[0][0] == board[1][1])
        && (board[1][1] == board[2][2])) {
      return board[0][0];
    } else if ((board[2][0] != null)
      && (board[2][0] == board[1][1])
      && (board[1][1] == board[0][2])) {
        return board[2][0];
    }
    return null;
  }

  /**
   * Marks the player's position on the board.
   *
   * @param x x-coordinate to mark
   * @param y y-coordinate to mark
   * @param mark mark to place on the board
   */
  public void move(int x, int y, Mark mark) {
    this.totalMarked++;
    this.board[x][y] = mark;
    this.lastMove = mark;
  }

}
