package util.json;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.*;
import util.Factory;

import java.lang.reflect.Type;

public class ChessMoveDeserializer implements JsonDeserializer<ChessMove> {

    @Override
    public ChessMove deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        Gson gson = new GsonBuilder().registerTypeAdapter(ChessPosition.class, new ChessPositionDeserializer()).create();

        ChessPosition start = gson.fromJson(object.get("start").toString(), ChessPosition.class);
        ChessPosition end = gson.fromJson(object.get("end").toString(), ChessPosition.class);
        JsonElement promotionElement = object.get("promotion");
        ChessPiece.PieceType promotion = null;
        if (promotionElement != null) promotion = gson.fromJson(promotionElement.toString(), ChessPiece.PieceType.class);

        return Factory.getNewMove(start, end, promotion);
    }
}
