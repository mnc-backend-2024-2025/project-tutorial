package kz.mathncode.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import jakarta.persistence.*;
import kz.mathncode.backend.controller.UserController;
import kz.mathncode.backend.dao.URLResourceDAO;
import kz.mathncode.backend.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    // Получить от клиента: имя, фамилию и email и создать нового пользователя с этими параметрами и сохранить его в базу

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("kz.mathncode.hibernate-tutorial");
        UserDAO userDAO = new UserDAO(emf.createEntityManager());
        ObjectMapper objectMapper = new ObjectMapper();
        UserController userController = new UserController(userDAO, objectMapper);
        Logger logger = LoggerFactory.getLogger(Main.class);

        URLResourceDAO urlResourceDAO = new URLResourceDAO(emf.createEntityManager());

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