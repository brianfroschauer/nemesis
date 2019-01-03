package dao;

import model.User;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: brianfroschauer
 * Date: 2018-12-27
 */
public class AbstractDAOTest {

    @Test
    public void createUserTest() {
        final AbstractDAO<User> abstractDAO = new UserDAO();
        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");

        final Integer userId = abstractDAO.create(user);
        assertThat(userId).isNotNull();
        assertThat(abstractDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(abstractDAO.getAll(User.class).size() == 1).isTrue();
        final Optional<User> optionalUser = abstractDAO.get(User.class, userId);
        optionalUser.ifPresent(abstractDAO::delete);
    }

    @Test
    public void updateUserTest() {
        final AbstractDAO<User> abstractDAO = new UserDAO();
        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = abstractDAO.create(user);
        user.setUsername("newUsername");
        final User updatedUser = abstractDAO.update(user);
        assertThat(updatedUser.getUsername().equals("newUsername")).isTrue();
        assertThat(abstractDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(abstractDAO.getAll(User.class).size() == 1).isTrue();
        abstractDAO.delete(user);
    }

    @Test
    public void removeUserTest() {
        final AbstractDAO<User> abstractDAO = new UserDAO();
        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = abstractDAO.create(user);
        abstractDAO.delete(user);
        assertThat(abstractDAO.get(User.class, userId).isPresent()).isFalse();
        assertThat(abstractDAO.getAll(User.class).isEmpty()).isTrue();
    }

    @Test
    public void getUserTest() {
        final AbstractDAO<User> abstractDAO = new UserDAO();
        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = abstractDAO.create(user);
        assertThat(abstractDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(abstractDAO.get(User.class, userId).get().getUsername().equals(user.getUsername())).isTrue();
        abstractDAO.delete(user);
    }

    @Test
    public void getAllUserTest() {
        final AbstractDAO<User> abstractDAO = new UserDAO();
        final User user1 = new User(
                "email1@mail.com",
                "username1",
                "password1",
                "name1",
                "surname1");
        abstractDAO.create(user1);
        final User user2 = new User(
                "email2@mail.com",
                "username2",
                "password2",
                "name2",
                "surname2");
        abstractDAO.create(user2);
        assertThat(abstractDAO.getAll(User.class).isEmpty()).isFalse();
        assertThat(abstractDAO.getAll(User.class).size() == 2).isTrue();
        abstractDAO.delete(user1);
        abstractDAO.delete(user2);
    }
}