package kz.mathncode.backend;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import kz.mathncode.backend.dao.ClickDAO;
import kz.mathncode.backend.dao.URLResourceDAO;
import kz.mathncode.backend.dao.UserDAO;
import kz.mathncode.backend.entity.User;

import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("kz.mathncode.hibernate-tutorial");

        UserDAO userDAO = new UserDAO(emf.createEntityManager());
        ClickDAO clickDAO = new ClickDAO(emf.createEntityManager());
        URLResourceDAO urlResourceDAO = new URLResourceDAO(emf.createEntityManager());

        User user = new User(
                null,
                "John",
                "Doe",
                "jdoe@gmail.com",
                ZonedDateTime.now()
        );

        userDAO.create(user);

    }
}