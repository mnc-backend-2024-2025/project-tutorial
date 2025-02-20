package kz.mathncode.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import kz.mathncode.backend.dao.DAO;
import kz.mathncode.backend.entity.User;
import kz.mathncode.backend.service.UserService;

import java.time.ZonedDateTime;
import java.util.UUID;

public class UserController extends AbstractController<User> {
    public UserController(DAO<User> dao, ObjectMapper objectMapper, UserService userService) {
        super(dao, objectMapper, userService);
    }

    @Override
    public User parseBodyCreate(Context ctx) {
        try {
            User user = getObjectMapper().readValue(ctx.body(), User.class);
            user.setId(null);
            user.setCreatedAt(ZonedDateTime.now());
            return user;
        } catch (JsonProcessingException e) {
            throw new BadRequestResponse("Invalid JSON provided");
        }
    }

    @Override
    public User parseBodyUpdate(Context ctx) {
        try {
            UUID id = idPathParam(ctx);
            User oldUser = getDao().readOne(id);
            User newUser = getObjectMapper().readValue(ctx.body(), User.class);

            newUser.setId(oldUser.getId());
            newUser.setCreatedAt(oldUser.getCreatedAt());

            return newUser;
        } catch (JsonProcessingException e) {
            throw new BadRequestResponse("Invalid JSON provided");
        }
    }
}
