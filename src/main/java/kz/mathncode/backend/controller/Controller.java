package kz.mathncode.backend.controller;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;

import java.util.List;

// Понять что мне написали по HTTP
// Запустить логику которая это делает
interface Controller<T> {
    void getOne(Context ctx);
    void getMany(Context ctx);
    void create(Context ctx);
    void update(Context ctx);
    void delete(Context ctx);
}
