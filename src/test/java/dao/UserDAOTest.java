package dao;

import model.Store;
import model.User;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: brianfroschauer
 * Date: 24/03/2018
 */
public class UserDAOTest {

    @Test
    public void getValidUserByUsernameTest() {
        final UserDAO userDAO = new UserDAO();
        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        userDAO.create(user);
        assertThat(userDAO.getUserByUsername("username").isPresent()).isTrue();
        userDAO.delete(user);
    }

    @Test
    public void getInvalidUserByUsernameTest() {
        final UserDAO userDAO = new UserDAO();
        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        userDAO.create(user);
        assertThat(userDAO.getUserByUsername("username1").isPresent()).isFalse();
        userDAO.delete(user);
    }

    @Test
    public void addStoreToUserTest() {
        final UserDAO userDAO = new UserDAO();
        final StoreDAO storeDAO = new StoreDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Store store = new Store(
                "name",
                "description");
        final Integer storeId = storeDAO.create(store);

        userDAO.addStoreToUser(userId, storeId);
        assertThat(userDAO.getStoresFromUser(userId).isEmpty()).isFalse();
        assertThat(userDAO.getStoresFromUser(userId).size() == 1).isTrue();

        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());

        // If a user is deleted, the store should not be deleted
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        storeDAO.delete(store);
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isFalse();
    }

    @Test
    public void deleteStoreFromUserTest() {
        final UserDAO userDAO = new UserDAO();
        final StoreDAO storeDAO = new StoreDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Store store = new Store(
                "name",
                "description");
        final Integer storeId = storeDAO.create(store);

        // Add store to user
        userDAO.addStoreToUser(userId, storeId);

        // The user's stores must not be empty.
        assertThat(userDAO.getStoresFromUser(userId).isEmpty()).isFalse();
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();

        // Delete store to user
        userDAO.deleteStoreFromUser(userId, storeId);

        // The user's stores must be empty.
        assertThat(userDAO.getStoresFromUser(userId).isEmpty()).isTrue();

        // The store should not be deleted from database,
        // it should be deleted only from the user (many to many relationship)
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();

        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        storeDAO.delete(store);
    }
}