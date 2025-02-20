package kz.mathncode.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import kz.mathncode.backend.dao.DAO;
import kz.mathncode.backend.entity.Click;
import kz.mathncode.backend.service.UserService;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ClickController extends AbstractController<Click> {
    public ClickController(DAO<Click> dao, ObjectMapper objectMapper, UserService userService) {
        super(dao, objectMapper, userService);
    }

    @Override
    public Click parseBodyCreate(Context ctx) {
        try {
            Click click = getObjectMapper().readValue(ctx.body(), Click.class);
            click.setId(null);
            click.setCreatedAt(ZonedDateTime.now());
            return click;
        } catch (JsonProcessingException e) {
            throw new BadRequestResponse("Invalid JSON provided");
        }
    }

    @Override
    public Click parseBodyUpdate(Context ctx) {
        try {
            UUID id = idPathParam(ctx);
            Click oldClick = getDao().readOne(id);
            Click click = getObjectMapper().readValue(ctx.body(), Click.class);
            click.setId(id);
            click.setCreatedAt(oldClick.getCreatedAt());
            return click;
        } catch (JsonProcessingException e) {
            throw new BadRequestResponse("Invalid JSON provided");
        }
    }
}
