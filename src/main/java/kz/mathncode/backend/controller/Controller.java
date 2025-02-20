package kz.mathncode.backend.controller;

import io.javalin.http.Context;
import kz.mathncode.backend.entity.User;


interface Controller<T> {
    void getOne(Context ctx);
    void getMany(Context ctx);
    void create(Context ctx);
    void update(Context ctx);
    void delete(Context ctx);

    User userOf(Context ctx);
}
