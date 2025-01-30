package kz.mathncode.backend;

import io.javalin.Javalin;
import io.javalin.config.RequestLoggerConfig;
import jakarta.persistence.*;
import kz.mathncode.backend.controller.UserController;
import kz.mathncode.backend.dao.ClickDAO;
import kz.mathncode.backend.dao.URLResourceDAO;
import kz.mathncode.backend.dao.UserDAO;
import kz.mathncode.backend.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    // Получить от клиента: имя, фамилию и email и создать нового пользователя с этими параметрами и сохранить его в базу

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("kz.mathncode.hibernate-tutorial");
        UserDAO userDAO = new UserDAO(emf.createEntityManager());
        UserController userController = new UserController(userDAO);
        Logger logger = LoggerFactory.getLogger(Main.class);

        Javalin app = Javalin.create(config -> {
            config.http.prefer405over404 = true;

            config.requestLogger.http((ctx, ms) -> {
                logger.info("Handled request {} {} at {}", ctx.method(), ctx.path(), ms);
            });

            config.router.apiBuilder(()-> {
                path("users/", () -> {
                   get(userController::getMany);
                   post(userController::create);
                   path("/{id}", () -> {
                       get(userController::getOne);
                       patch(userController::update);
                       delete(userController::delete);
                   });
                });
            });
        });

        app.start(7001);
    }
}