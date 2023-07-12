package dao;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.bean.GameBean;

import java.util.Collection;

public interface GameDAO {

    /**
     * Inserts a game into the database
     * @param bean The game to insert
     * @throws DataAccessException
     */
    void insert(GameBean bean) throws DataAccessException;

    /**
     * Finds a game from the database
     * @param gameID The gameID of the game to find
     * @return The game in question, or null if it doesn't exist
     * @throws DataAccessException
     */
    GameBean find(int gameID) throws DataAccessException;

    /**
     * Finds every game in the database
     * @return A non-null collection of games
     * @throws DataAccessException
     */
    Collection<GameBean> findAll() throws DataAccessException;

    /**
     * Updates a given game in the database
     * @param game The game to update
     * @throws DataAccessException
     */
    void update(GameBean game) throws DataAccessException;

    /**
     * Removes a game from the database
     * @param gameID The gameID of the game to remove
     * @throws DataAccessException
     */
    void delete(int gameID) throws DataAccessException;

    /**
     * Removes all games from the database
     * @throws DataAccessException
     */
    void clear() throws DataAccessException;

    /**
     * Updates the players in a game
     * @param gameID The gameID of the game to update
     * @param color The color whose player will be updated
     * @param playerID The player claiming the spot
     * @throws DataAccessException
     */
    void claimSpot(int gameID, ChessGame.TeamColor color, int playerID) throws DataAccessException;

}
