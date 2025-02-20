package kz.mathncode.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import jakarta.persistence.*;
import kz.mathncode.backend.controller.ClickController;
import kz.mathncode.backend.controller.URLResourceController;
import kz.mathncode.backend.controller.UserController;
import kz.mathncode.backend.dao.ClickDAO;
import kz.mathncode.backend.dao.URLResourceDAO;
import kz.mathncode.backend.dao.UserDAO;
import kz.mathncode.backend.entity.URLResource;
import kz.mathncode.backend.entity.User;
import kz.mathncode.backend.json.deserializers.URLResourceDeserializer;
import kz.mathncode.backend.json.deserializers.UserDeserializer;
import kz.mathncode.backend.json.serializers.UserSerializer;
import kz.mathncode.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("kz.mathncode.hibernate-tutorial");

        UserDAO userDAO = new UserDAO(emf.createEntityManager());
        URLResourceDAO urlResourceDAO = new URLResourceDAO(emf.createEntityManager());
        ClickDAO clickDAO = new ClickDAO(emf.createEntityManager());

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(
                        new SimpleModule()
                                .addDeserializer(User.class, new UserDeserializer())
                                .addDeserializer(URLResource.class, new URLResourceDeserializer(userDAO))
                                .addSerializer(User.class, new UserSerializer())
                )
                .registerModule(new JavaTimeModule());

        UserService userService = new UserService(userDAO);

        UserController userController = new UserController(userDAO, objectMapper, userService);
        URLResourceController urlResourceController = new URLResourceController(urlResourceDAO, objectMapper, userService, clickDAO);
        ClickController clickController = new ClickController(clickDAO, objectMapper, userService);

        Logger logger = LoggerFactory.getLogger(Main.class);

        Javalin app = Javalin.create(config -> {
            config.http.prefer405over404 = true;
            config.http.defaultContentType = "application/json";

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


                path("urls/", () -> {
                    get(urlResourceController::getMany);
                    post(urlResourceController::create);
                    path("/{id}", () -> {
                        get(urlResourceController::getOne);
                        patch(urlResourceController::update);
                        delete(urlResourceController::delete);
                    });
                });


                path("clicks/", () -> {
                    get(clickController::getMany);
                    path("/{id}", () -> {
                        get(clickController::getOne);
                        delete(clickController::delete);
                    });
                });

                path("/{shortURL}", () -> {
                    get(urlResourceController::redirect);
                });
            });
        });

        app.start(7001);
    }
}