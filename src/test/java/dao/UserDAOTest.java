package dao;

import model.Category;
import model.Product;
import model.Store;
import model.User;

import org.junit.Test;

import java.util.Optional;

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

    @Test
    public void addProductToUserTest() {
        final UserDAO userDAO = new UserDAO();
        final ProductDAO productDAO = new ProductDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Category category = new Category("category");
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        Integer categoryId = categoryDAO.create(category);

        final Product product = new Product(
                "name",
                0,
                1,
                category);
        final Integer productId = productDAO.create(product);

        // Add product to user
        userDAO.addProductToUser(userId, productId);

        // The user's products must not be empty.
        assertThat(userDAO.getProductsFromUser(userId).isEmpty()).isFalse();
        assertThat(userDAO.getProductsFromUser(userId).size() == 1).isTrue();

        // Delete user
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());

        // Products should not be deleted from the database if the user is deleted
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        productDAO.delete(product);
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();

        // Categories should not be deleted from the database if the products are deleted
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isFalse();
    }

    @Test
    public void deleteProductFromUser() {
        final UserDAO userDAO = new UserDAO();
        final ProductDAO productDAO = new ProductDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Category category = new Category("category");
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product(
                "name",
                0,
                1,
                category);
        final Integer productId = productDAO.create(product);

        userDAO.addProductToUser(userId, productId);
        assertThat(userDAO.getProductsFromUser(userId).isEmpty()).isFalse();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();

        // Products must be deleted from the user, it should not be deleted from database
        userDAO.deleteProductFromUser(userId, productId);
        assertThat(userDAO.getProductsFromUser(userId).isEmpty()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();

        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        productDAO.delete(product);

        // Categories should not be deleted from the database if the products are deleted
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isFalse();
    }

    @Test
    public void deleteAllProductsFromUser() {
        final UserDAO userDAO = new UserDAO();
        final ProductDAO productDAO = new ProductDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Category category1 = new Category("category1");
        final Category category2 = new Category("category2");
        final Integer categoryId1 = categoryDAO.create(category1);
        final Integer categoryId2 = categoryDAO.create(category2);

        final Product product1 = new Product(
                "name1",
                0,
                1,
                category1);
        final Integer productId1 = productDAO.create(product1);

        final Product product2 = new Product(
                "name2",
                0,
                1,
                category2);
        final Integer productId2 = productDAO.create(product2);

        userDAO.addProductToUser(userId, productId1);
        userDAO.addProductToUser(userId, productId2);

        assertThat(userDAO.getProductsFromUser(userId).isEmpty()).isFalse();
        assertThat(productDAO.get(Product.class, productId1).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId2).isPresent()).isTrue();

        userDAO.deleteAllProductsFromUser(userId);

        assertThat(userDAO.getProductsFromUser(userId).isEmpty()).isTrue();
        assertThat(productDAO.get(Product.class, productId1).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId2).isPresent()).isTrue();

        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());

        productDAO.delete(product1);
        productDAO.delete(product2);

        assertThat(categoryDAO.get(Category.class, categoryId1).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId2).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId1).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId2).get());
        assertThat(categoryDAO.get(Category.class, categoryId1).isPresent()).isFalse();
        assertThat(categoryDAO.get(Category.class, categoryId2).isPresent()).isFalse();
    }
}