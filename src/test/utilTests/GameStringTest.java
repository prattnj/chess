package utilTests;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import util.Factory;
import util.json.ChessMoveDeserializer;
import util.json.ChessPositionDeserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static passoffTests.TestFactory.getNewMove;
import static passoffTests.TestFactory.getNewPosition;
import static util.Factory.*;

public class GameStringTest {

    @Test
    public void testGameToString() throws InvalidMoveException {

        ChessGame game = getNewGame();
        game.makeMove(getNewMove(getNewPosition(2, 4), getNewPosition(4, 4), null));
        game.makeMove(getNewMove(getNewPosition(7, 5), getNewPosition(5, 5), null));
        game.makeMove(getNewMove(getNewPosition(4, 4), getNewPosition(5, 5), null));

        String str = game.toString();

        ChessGame game2 = Factory.getNewGame(str);
        assertEquals(str, game2.toString());
    }

    @Test
    public void testPositionToString() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ChessPosition.class, new ChessPositionDeserializer())
                .create();

        ChessPosition pos1 = Factory.getNewPosition(2, 2);
        String json1 = gson.toJson(pos1);
        ChessPosition pos2 = gson.fromJson(json1, ChessPosition.class);

        assertEquals(json1, gson.toJson(pos2));
    }

    @Test
    public void testMoveToString() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ChessMove.class, new ChessMoveDeserializer())
                .create();

        ChessMove move1 = Factory.getNewMove(Factory.getNewPosition(1, 1), Factory.getNewPosition(2, 2), null);
        String json1 = gson.toJson(move1);
        ChessMove move2 = gson.fromJson(json1, ChessMove.class);

        assertEquals(json1, gson.toJson(move2));
    }
}
