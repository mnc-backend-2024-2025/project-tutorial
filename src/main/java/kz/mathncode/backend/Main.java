package kz.mathncode.backend;

import io.javalin.Javalin;
import jakarta.persistence.*;
import kz.mathncode.backend.dao.ClickDAO;
import kz.mathncode.backend.dao.URLResourceDAO;
import kz.mathncode.backend.dao.UserDAO;
import kz.mathncode.backend.entity.User;

import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.UUID;

public class Main {

    // Получить от клиента: имя, фамилию и email и создать нового пользователя с этими параметрами и сохранить его в базу

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("kz.mathncode.hibernate-tutorial");
        UserDAO userDAO = new UserDAO(emf.createEntityManager());

        Javalin app = Javalin.create();
        app.post("/users", ctx -> {
            String firstName = ctx.queryParam("first_name");
            String lastName = ctx.queryParam("last_name");
            String email = ctx.queryParam("email");

            User user = new User(
                    null, //из-за @GeneratedValue
                    firstName,
                    lastName,
                    email,
                    ZonedDateTime.now()
            );

            userDAO.create(user);
            System.out.printf("Created user with parameters: %s, %s, %s\n", firstName, lastName, email);
        });

        app.start(7001);
    }
}