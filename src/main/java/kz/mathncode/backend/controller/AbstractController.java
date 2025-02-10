package kz.mathncode.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.InternalServerErrorResponse;
import kz.mathncode.backend.dao.DAO;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public abstract class AbstractController<T> implements Controller<T> {
    public static String ID_PATH_PARAM = "id";

    private DAO<T> dao;
    private ObjectMapper objectMapper;


    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DAO<T> getDao() {
        return dao;
    }

    public void setDao(DAO<T> dao) {
        this.dao = dao;
    }

    public AbstractController(DAO<T> dao, ObjectMapper objectMapper) {
        this.dao = dao;
        this.objectMapper = objectMapper;
    }

    public T parseBody(Context ctx, Function<Context, T> parserFunction) {
        return parserFunction.apply(ctx);
    }

    public abstract T parseBodyCreate(Context ctx);
    public abstract T parseBodyUpdate(Context ctx);

    public UUID idPathParam(Context ctx) {
        String id = ctx.pathParam(ID_PATH_PARAM);
        return UUID.fromString(id);
    }

    @Override
    public void getOne(Context ctx) {
        try {
            T entity = dao.readOne(idPathParam(ctx));
            String json = getObjectMapper().writeValueAsString(entity);
            ctx.result(json);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorResponse("Error during JSON serialization");
        }
    }

    @Override
    public void getMany(Context ctx) {
        try {
            List<T> entities= dao.readMany(0, 100);
            String json = getObjectMapper().writeValueAsString(entities);
            ctx.result(json);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorResponse("Error during JSON serialization");
        }
    }

    @Override
    public void create(Context ctx) {
        T entity = parseBody(ctx, this::parseBodyCreate);
        dao.create(entity);
        ctx.status(HttpStatus.CREATED);
    }

    @Override
    public void update(Context ctx) {
        UUID id = idPathParam(ctx);
        T entity = parseBody(ctx, this::parseBodyUpdate);
        dao.update(id, entity);
    }

    @Override
    public void delete(Context ctx) {
        UUID id = idPathParam(ctx);
        dao.delete(id);
        ctx.status(HttpStatus.NO_CONTENT);
    }
}
