package kz.mathncode.backend.dao;

import jakarta.persistence.EntityManager;
import kz.mathncode.backend.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserDAO extends AbstractDAO<User> {
    public static final int BCRYPT_ROUNDS = 12;

    public UserDAO(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    @Override
    public void create(User entity) {
        String hashedPassword = hashPassword(entity.getPassword());
        entity.setPassword(hashedPassword);
        super.create(entity);
    }

    @Override
    public void update(UUID id, User entity) {
        User storedEntity = this.readOne(id);
        boolean passwordNotChanged = BCrypt.checkpw(entity.getPassword(), storedEntity.getPassword());
        if (passwordNotChanged) {
            entity.setPassword(storedEntity.getPassword());
        } else {
            String newPassword = hashPassword(entity.getPassword());
            entity.setPassword(newPassword);
        }

        super.update(id, entity);
    }
}
