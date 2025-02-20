package kz.mathncode.backend.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import kz.mathncode.backend.entity.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {
    public static final String FIELD_ID = "id";
    public static final String FIELD_FIRST_NAME = "firstName";
    public static final String FIELD_LAST_NAME = "lastName";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_CREATED_AT = "createdAt";

    public UserSerializer() {
        super(User.class);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField(FIELD_ID, user.getId().toString());
        jsonGenerator.writeStringField(FIELD_FIRST_NAME, user.getFirstName());
        jsonGenerator.writeStringField(FIELD_LAST_NAME, user.getLastName());
        jsonGenerator.writeStringField(FIELD_EMAIL, user.getEmail());
        jsonGenerator.writeObjectField(FIELD_CREATED_AT, user.getCreatedAt());

        jsonGenerator.writeEndObject();
    }
}
