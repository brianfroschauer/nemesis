package dao;

import model.Category;
import model.Product;
import model.User;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemDAOTest {

    @Test
    public void getItemsFromUser() {
    }

    @Test
    public void addItemToUser() {
        final CategoryDAO categoryDAO = new CategoryDAO();
        final ProductDAO productDAO = new ProductDAO();
        final UserDAO userDAO = new UserDAO();
        final ItemDAO itemDAO = new ItemDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Category category = new Category("category");
        final Product product = new Product(
                "name",
                0,
                1,
                category);

        final Integer userId = userDAO.create(user);
        final Integer categoryId = categoryDAO.create(category);
        final Integer productId = productDAO.create(product);

        // Add item to user
        itemDAO.addItemToUser(userId, productId, 1);

        // The user's items must not be empty.
        assertThat(itemDAO.getItemsFromUser(userId).isEmpty()).isFalse();
        assertThat(itemDAO.getItemsFromUser(userId).size() == 1).isTrue();

        // Delete user
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        assertThat(userDAO.get(User.class, userId).isPresent()).isFalse();

        // Products should not be deleted from the database if the user is deleted
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();

        // Items should be deleted from the database if the user is deleted
        assertThat(itemDAO.getItemFromUser(userId, productId).isPresent()).isFalse();

        // Delete product
        productDAO.delete(productDAO.get(Product.class, productId).get());
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();

        // Categories should not be deleted from the database if the products are deleted
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isFalse();
    }

    @Test
    public void deleteItemFromUser() {
        final CategoryDAO categoryDAO = new CategoryDAO();
        final ProductDAO productDAO = new ProductDAO();
        final UserDAO userDAO = new UserDAO();
        final ItemDAO itemDAO = new ItemDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Category category = new Category("category");
        final Product product = new Product(
                "name",
                0,
                1,
                category);

        final Integer userId = userDAO.create(user);
        final Integer categoryId = categoryDAO.create(category);
        final Integer productId = productDAO.create(product);

        // Add item to user
        itemDAO.addItemToUser(userId, productId, 1);

        // The user's items must not be empty.
        assertThat(itemDAO.getItemsFromUser(userId).isEmpty()).isFalse();
        assertThat(itemDAO.getItemsFromUser(userId).size() == 1).isTrue();

        // Delete item to the user
        itemDAO.deleteItemFromUser(userId, productId);

        // The user's items must be empty.
        assertThat(itemDAO.getItemsFromUser(userId).isEmpty()).isTrue();
        assertThat(itemDAO.getItemsFromUser(userId).size() == 1).isFalse();

        // Delete user
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        assertThat(userDAO.get(User.class, userId).isPresent()).isFalse();

        // Delete product
        productDAO.delete(productDAO.get(Product.class, productId).get());
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();

        // Categories should not be deleted from the database if the products are deleted
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isFalse();
    }

    @Test
    public void deleteAllItemsFromUser() {
        final CategoryDAO categoryDAO = new CategoryDAO();
        final ProductDAO productDAO = new ProductDAO();
        final UserDAO userDAO = new UserDAO();
        final ItemDAO itemDAO = new ItemDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Category category = new Category("category");
        final Product product = new Product(
                "name",
                0,
                1,
                category);

        final Integer userId = userDAO.create(user);
        final Integer categoryId = categoryDAO.create(category);
        final Integer productId = productDAO.create(product);

        // Add item to user
        itemDAO.addItemToUser(userId, productId, 1);

        // The user's items must not be empty.
        assertThat(itemDAO.getItemsFromUser(userId).isEmpty()).isFalse();
        assertThat(itemDAO.getItemsFromUser(userId).size() == 1).isTrue();

        // Delete item to the user
        itemDAO.deleteAllItemsFromUser(userId);

        // The user's items must be empty.
        assertThat(itemDAO.getItemsFromUser(userId).isEmpty()).isTrue();
        assertThat(itemDAO.getItemsFromUser(userId).size() == 1).isFalse();

        // Delete user
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        assertThat(userDAO.get(User.class, userId).isPresent()).isFalse();

        // Delete product
        productDAO.delete(productDAO.get(Product.class, productId).get());
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();

        // Categories should not be deleted from the database if the products are deleted
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isFalse();
    }

    @Test
    public void getItemFromUser() {
        final CategoryDAO categoryDAO = new CategoryDAO();
        final ProductDAO productDAO = new ProductDAO();
        final UserDAO userDAO = new UserDAO();
        final ItemDAO itemDAO = new ItemDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Category category = new Category("category");
        final Product product = new Product(
                "name",
                0,
                1,
                category);

        final Integer userId = userDAO.create(user);
        final Integer categoryId = categoryDAO.create(category);
        final Integer productId = productDAO.create(product);

        // Add item to user
        itemDAO.addItemToUser(userId, productId, 1);

        assertThat(itemDAO.getItemFromUser(userId, productId).isPresent()).isTrue();

        // Delete user
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        assertThat(userDAO.get(User.class, userId).isPresent()).isFalse();

        // Products should not be deleted from the database if the user is deleted
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();

        // Items should be deleted from the database if the user is deleted
        assertThat(itemDAO.getItemFromUser(userId, productId).isPresent()).isFalse();

        // Delete product
        productDAO.delete(productDAO.get(Product.class, productId).get());
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();

        // Categories should not be deleted from the database if the products are deleted
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isFalse();
    }
}