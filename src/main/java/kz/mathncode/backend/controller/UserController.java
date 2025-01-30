package kz.mathncode.backend.controller;

import io.javalin.http.Context;
import kz.mathncode.backend.dao.DAO;
import kz.mathncode.backend.entity.User;

import java.time.ZonedDateTime;
import java.util.UUID;

public class UserController extends AbstractController<User> {
    public static String FIRST_NAME_QUERY_PARAM = "first_name";
    public static String LAST_NAME_QUERY_PARAM = "last_name";
    public static String EMAIL_QUERY_PARAM = "email";

    public UserController(DAO<User> dao) {
        super(dao);
    }

    @Override
    public User parseBodyCreate(Context ctx) {
        String firstName = ctx.queryParam(FIRST_NAME_QUERY_PARAM);
        String lastName = ctx.queryParam(LAST_NAME_QUERY_PARAM);
        String email = ctx.queryParam(EMAIL_QUERY_PARAM);

        return new User(
                null, //из-за @GeneratedValue
                firstName,
                lastName,
                email,
                ZonedDateTime.now()
        );
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
