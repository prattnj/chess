package utilTests;

import chess.*;
import org.junit.jupiter.api.Test;
import util.Factory;

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
}
