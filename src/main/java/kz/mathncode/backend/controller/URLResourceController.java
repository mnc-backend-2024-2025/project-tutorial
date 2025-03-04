package kz.mathncode.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HttpStatus;
import kz.mathncode.backend.dao.DAO;
import kz.mathncode.backend.entity.Click;
import kz.mathncode.backend.entity.URLResource;
import kz.mathncode.backend.service.UserService;

import java.time.ZonedDateTime;
import java.util.UUID;

public class URLResourceController extends AbstractController<URLResource> {
    public final static String PATH_PARAM_SHORT_URL = "shortURL";
    public final static String FIELD_SHORT_URL = "shortURL";

    private final DAO<Click> clickDAO;

    public URLResourceController(DAO<URLResource> dao, ObjectMapper objectMapper, UserService userService, DAO<Click> clickDAO) {
        super(dao, objectMapper, userService);
        this.clickDAO = clickDAO;
    }

    @Override
    public URLResource parseBodyCreate(Context ctx) {
        try {
            URLResource resource = getObjectMapper().readValue(ctx.body(), URLResource.class);
            resource.setId(null);
            resource.setCreatedAt(ZonedDateTime.now());
            return resource;
        } catch (JsonProcessingException e) {
            throw new BadRequestResponse("Invalid JSON provided");
        }
    }

    @Override
    public URLResource parseBodyUpdate(Context ctx) {
        try {
            UUID id = idPathParam(ctx);
            URLResource oldResource = getDao().readOne(id);
            URLResource resource = getObjectMapper().readValue(ctx.body(), URLResource.class);
            resource.setId(id);
            resource.setCreatedAt(oldResource.getCreatedAt());

            return resource;
        } catch (JsonProcessingException e) {
            throw new BadRequestResponse("Invalid JSON provided");
        }
    }

    public void redirect(Context ctx) {
        String shortURL = ctx.pathParam(PATH_PARAM_SHORT_URL);
        URLResource found = getDao().findFirstByField(FIELD_SHORT_URL, shortURL);
        if (found == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }

        Click click = new Click(null, found, ctx.ip(), ZonedDateTime.now());
        clickDAO.create(click);

        ctx.redirect(found.getFullURL(), HttpStatus.TEMPORARY_REDIRECT);
    }

    public DAO<Click> getClickDAO() {
        return clickDAO;
    }

    @Override
    public void create(Context ctx) {
        URLResource entity = parseBody(ctx, this::parseBodyCreate);
        if (userOf(ctx) == null) {
            throw new ForbiddenResponse("Only authenticated users can perform this action");
        }
        if (entity.getCreatedBy() != userOf(ctx)) {
            throw new ForbiddenResponse("Can't create record with createdBy to different user");
        }

        getDao().create(entity);
        ctx.status(HttpStatus.CREATED);
    }

    @Override
    public void update(Context ctx) {
        UUID id = idPathParam(ctx);
        URLResource entity = parseBody(ctx, this::parseBodyUpdate);
        if (userOf(ctx) == null) {
            throw new ForbiddenResponse("Only authenticated users can perform this action");
        }
        if (entity.getCreatedBy() != userOf(ctx)) {
            throw new ForbiddenResponse("Can't update createdBy to different user");
        }

        getDao().update(id, entity);
    }
}
