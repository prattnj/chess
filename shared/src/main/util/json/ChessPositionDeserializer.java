package util.json;

import chess.ChessPosition;
import com.google.gson.*;
import util.Factory;

import java.lang.reflect.Type;

public class ChessPositionDeserializer implements JsonDeserializer<ChessPosition> {

    @Override
    public ChessPosition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        int row = object.get("row").getAsInt();
        int col = object.get("col").getAsInt();
        return Factory.getNewPosition(row, col);
    }
}
