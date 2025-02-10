package kz.mathncode.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import kz.mathncode.backend.dao.DAO;
import kz.mathncode.backend.entity.URLResource;

import java.time.ZonedDateTime;
import java.util.UUID;

public class URLResourceController extends AbstractController<URLResource> {
    public URLResourceController(DAO<URLResource> dao, ObjectMapper objectMapper) {
        super(dao, objectMapper);
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
            URLResource oldResource = getObjectMapper().readValue(ctx.body(), URLResource.class);
            URLResource resource = getObjectMapper().readValue(ctx.body(), URLResource.class);
            resource.setId(id);
            resource.setCreatedAt(oldResource.getCreatedAt());

            return resource;
        } catch (JsonProcessingException e) {
            throw new BadRequestResponse("Invalid JSON provided");
        }
    }
}
