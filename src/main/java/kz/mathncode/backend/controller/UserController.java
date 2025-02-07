package kz.mathncode.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import kz.mathncode.backend.dao.DAO;
import kz.mathncode.backend.entity.User;

import java.time.ZonedDateTime;
import java.util.UUID;

public class UserController extends AbstractController<User> {
    private ObjectMapper objectMapper;

    public static String FIRST_NAME_QUERY_PARAM = "first_name";
    public static String LAST_NAME_QUERY_PARAM = "last_name";
    public static String EMAIL_QUERY_PARAM = "email";

    public UserController(DAO<User> dao, ObjectMapper objectMapper) {
        super(dao);
        this.objectMapper = objectMapper;
    }

    @Override
    public User parseBodyCreate(Context ctx) {
        try {
            User user = objectMapper.readValue(ctx.body(), User.class);
            user.setId(null);
            user.setCreatedAt(ZonedDateTime.now());
            return user;
        } catch (JsonProcessingException e) {
            throw new BadRequestResponse("Invalid JSON provided");
        }
    }

    @Override
    public User parseBodyUpdate(Context ctx) {
        UUID id = idPathParam(ctx);
        User oldUser = dao.readOne(id);

        String firstName = ctx.queryParam("first_name");
        String lastName = ctx.queryParam("last_name");
        String email = ctx.queryParam("email");

        return new User(
                oldUser.getId(),
                firstName,
                lastName,
                email,
                oldUser.getCreatedAt()
        );
    }
}
