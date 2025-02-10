package kz.mathncode.backend.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import kz.mathncode.backend.entity.User;

import java.io.IOException;

public class UserDeserializer extends StdDeserializer<User> {
    public static String FIELD_FIRST_NAME = "firstName";
    public static String FIELD_LAST_NAME = "lastName";
    public static String FIELD_EMAIL = "email";

    public UserDeserializer() {
        super(User.class);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String firstName = root.get(FIELD_FIRST_NAME).asText();
        String lastName = root.get(FIELD_LAST_NAME).asText();
        String email = root.get(FIELD_EMAIL).asText();

        return new User(null, firstName, lastName, email, null);
    }
}
