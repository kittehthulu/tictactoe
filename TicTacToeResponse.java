public class TicTacToeResponse {

  public static String INVALID_GAME = "Game does not exist.";
  public static String INVALID_MARK = "Invalid player mark.";
  public static String INVALID_MOVE = "Invalid move.";
  public static String TIE = "tie";
  public static String ONGOING = "ongoing";
  public static String WIN = "win";

  public String gameId;
  public String playerMark;
  public String gameState;
  public String winner;
  public String error;

  public TicTacToeResponse() {}
}
