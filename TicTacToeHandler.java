import java.util.HashMap;
import java.util.UUID;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * A handler for a tic-tac-toe game.
 */
public class TicTacToeHandler implements HttpHandler {

  // For keeping track of multiple tic-tac-toe games.  Not fully utilized.
  // Ideally, next iteration would maintain a queue of users waiting to join a
  // game.
  private HashMap<String, TicTacToe> games = new HashMap<String, TicTacToe>();


  /**
   * Creates a new game of tic-tac-toe.
   *
   * @return a TicTacToeResponse with the new game ID set.
   */
  public TicTacToeResponse newGame() {
    TicTacToeResponse response = new TicTacToeResponse();
    TicTacToe game = new TicTacToe();
    String gameId = UUID.randomUUID().toString();
    games.put(gameId, game);
    response.gameId = gameId;
    return response;
  }

  /**
   * Sets/validates a user's move.
   *
   * @param move info to indicate which game, which move, and which player
   * @return a TicTacToeResponse that will contain information about the state
   *         of the game, including an error if an invalid move is made
   */
  public TicTacToeResponse move(TicTacToeMove move) {
    TicTacToeResponse response = new TicTacToeResponse();
    TicTacToe game = this.games.get(move.gameId);
    if (game == null) {
      response.error = TicTacToeResponse.INVALID_GAME;
      return response;
    }
    TicTacToe.Mark playerMark = null;
    try {
      playerMark = TicTacToe.Mark.valueOf(move.playerMark);
    } catch (IllegalArgumentException ex) {
      response.error = TicTacToeResponse.INVALID_MARK;
      return response;
    }
    if (game.isValidMove(move.x, move.y, playerMark)) {
      game.move(move.x, move.y, playerMark);
      TicTacToe.Mark winMark = game.checkWin();
      if (winMark == null) {
        if (game.checkTie(winMark)) {
          response.gameState = TicTacToeResponse.TIE;
        } else {
          response.gameState = TicTacToeResponse.ONGOING;
        }
      } else {
        response.gameState = TicTacToeResponse.WIN;
        response.winner = playerMark.toString();
      }
    } else {
      response.error = TicTacToeResponse.INVALID_MOVE;
    }
    return response;
  }

  /**
   * Handles requests to the tic-tac-toe server.
   * /new calls newGame(), /move calls move()
   * 
   */
  public void handle(HttpExchange t) throws IOException {
    TicTacToeResponse response = new TicTacToeResponse();
    Gson gson = new Gson();
    String path = t.getRequestURI().getPath();
    // Probably a better way to do this but this is simple/dumb enough.
    if (path.equals("/new")) {
      response = this.newGame();
    } else if (path.equals("/move")) {
      // Parse request body
      BufferedReader br = new BufferedReader(
          new InputStreamReader(t.getRequestBody()));
      String body = br.readLine();
      System.out.println("Request body received: " + body);
      TicTacToeMove move = gson.fromJson(body, TicTacToeMove.class);
      response = this.move(move);
      br.close();
    }
    String responseBody = gson.toJson(response);
    t.sendResponseHeaders(200, responseBody.length());
    OutputStream os = t.getResponseBody();
    os.write(responseBody.getBytes());
    os.close();
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Starting...");
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    TicTacToeHandler handler = new TicTacToeHandler();
    server.createContext("/new", handler);
    server.createContext("/move", handler);
    server.start();
    System.out.println("Started.");
  }

}
