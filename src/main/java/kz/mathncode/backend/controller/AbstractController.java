package kz.mathncode.backend.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import kz.mathncode.backend.dao.DAO;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public abstract class AbstractController<T> implements Controller<T> {
    public static String ID_PATH_PARAM = "id";

    DAO<T> dao;

    public AbstractController(DAO<T> dao) {
        this.dao = dao;
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
        T entity = dao.readOne(idPathParam(ctx));
        ctx.result(entity.toString());
    }

    @Override
    public void getMany(Context context) {
        List<T> entities= dao.readMany(0, 100);
        String result = entities.toString();
        context.result(result);
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
