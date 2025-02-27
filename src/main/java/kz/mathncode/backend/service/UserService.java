package kz.mathncode.backend.service;

import kz.mathncode.backend.dao.DAO;
import kz.mathncode.backend.entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserService extends AbstractService<User> {
    public UserService(DAO<User> dao) {
        super(dao);
    }

    public User findByEmail(String email) {
        User user = getDao().findFirstByField("email", email);
        return user;
    }

    public User authenticate(String email, String password) {
        User user = findByEmail(email);
        if (user == null) {
            return null;
        }

        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }
}
