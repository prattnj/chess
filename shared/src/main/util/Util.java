package util;

import chess.*;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

/**
 * A class containing static methods for use across the project
 */
public class Util {

    /**
     * Represents the current database being used. Currently, supports "ram" and "mysql"
     */
    public static String CURRENT_DAO_TYPE;

    public static String INVALID_TOKEN = "invalid authToken";

    public static String BAD_REQUEST = "invalid or incomplete request";

    public static String SERVER_ERROR = "Internal server error";

    /**
     * Makes sure that the given position falls within the bounds of an 8 by 8 chess board
     * @param pos The position to be validated
     * @return A boolean representing whether the given position if valid
     */
    public static boolean validatePosition(ChessPosition pos) {
        return pos.getRow() >= 1 && pos.getRow() <= 8 && pos.getColumn() >= 1 && pos.getColumn() <= 8;
    }

    /**
     * Given the parts of a potential ChessMove, this method validates said move and adds it to a Collection of ChessMove
     * objects if it is valid. This method does not check for king endangerment, only if the end ChessPosition in question
     * is already occupied, and by whom it is occupied.
     * @param oldPos The initial position of a potential ChessMove
     * @param newPos The final position of a potential ChessMove
     * @param board The board in question, used for reference
     * @param moves A Collection of ChessMove objects to potentially add a new move to
     * @param promotion Where applicable, the PieceType to be added to the new move
     * @return A boolean representing whether bishops and rooks (and therefore queens) need to keep scanning for available spaces.
     * Otherwise, unused.
     */
    public static boolean addMove(ChessPosition oldPos, ChessPosition newPos, ChessBoard board, Collection<ChessMove> moves, ChessPiece.PieceType promotion) {

        // The return value is only used for bishops, rooks, and queens who need to know to keep looking

        if (!validatePosition(newPos)) return false;
        ChessPiece mover = board.getPiece(oldPos);
        ChessPiece resident = board.getPiece(newPos);

        if (resident != null && resident.getTeamColor() == mover.getTeamColor()) return false;

        moves.add(Factory.getNewMove(oldPos, newPos, promotion));

        // Returns true if the space is unoccupied, false if there is an enemy piece
        return resident == null;
    }

    /**
     * Generates a random n-digit integer
     * @param digits The number of digits in this random ID
     * @return An integer from 10^n-1 to 10^n
     */
    public static int getRandomID(int digits) {
        return new Random().nextInt((int) Math.pow(10, digits - 1), (int) Math.pow(10, digits));
    }

    /**
     * Generates a new authToken using Java's UUID class
     * @return The new authToken as a String
     */
    public static String getNewAuthToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Given a String, returns the corresponding TeamColor. Returns null if the String is not a valid color
     * @param str The String to be converted
     * @return The corresponding TeamColor
     */
    public static ChessGame.TeamColor getColorForString(String str) {
        if (str.equalsIgnoreCase("white") || str.equalsIgnoreCase("w")) return ChessGame.TeamColor.WHITE;
        else if (str.equalsIgnoreCase("black") || str.equalsIgnoreCase("b")) return ChessGame.TeamColor.BLACK;
        else return null;
    }

    /**
     * Given a TeamColor, returns the corresponding String. Returns null if the TeamColor is not valid
     * @param color The TeamColor to be converted
     * @return The corresponding String
     */
    public static String getStringForColor(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) return "white";
        else if (color == ChessGame.TeamColor.BLACK) return "black";
        else return null;
    }
}
