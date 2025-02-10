package kz.mathncode.backend.json.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import kz.mathncode.backend.dao.DAO;
import kz.mathncode.backend.entity.URLResource;
import kz.mathncode.backend.entity.User;

import java.io.IOException;
import java.util.UUID;

public class URLResourceDeserializer extends StdDeserializer<URLResource> {
    private final DAO<User> userDAO;

    public static final String FIELD_SHORT_URL = "shortURL";
    public static final String FIELD_FULL_URL = "fullURL";
    public static final String FIELD_CREATED_BY = "createdBy";

    public URLResourceDeserializer(DAO<User> userDAO) {
        super(URLResource.class);
        this.userDAO = userDAO;
    }

    @Override
    public URLResource deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String shortURL = root.get(FIELD_SHORT_URL).asText();
        String fullURL = root.get(FIELD_FULL_URL).asText();

        UUID createdByUUID = UUID.fromString(root.get(FIELD_CREATED_BY).asText());
        User createdBy = userDAO.readOne(createdByUUID);

        return new URLResource(null, shortURL, fullURL, createdBy, null);
    }
}
