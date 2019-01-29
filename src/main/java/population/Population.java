package population;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.StoreDAO;
import dao.UserDAO;
import model.Category;
import model.Product;
import model.Store;
import model.User;

/**
 * Author: brianfroschauer
 * Date: 2019-01-29
 */
public class Population {

    private UserDAO userDAO = new UserDAO();
    private StoreDAO storeDAO = new StoreDAO();
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    public Population() {
        populate();
    }

    private void populate() {
        final User user = new User(
                "brianfroschauer@gmail.com",
                "brianfroschauer",
                "password",
                "name",
                "surname");

        final Integer userId = userDAO.create(user);

        final Store store = new Store(
                "name",
                "description");

        final Integer storeId = storeDAO.create(store);

        userDAO.addStoreToUser(userId, storeId);

        final Category category = new Category("Shirt");

        categoryDAO.create(category);

        final Product product = new Product("Tshirt", 120, 10, category);

        productDAO.create(product);
    }
}
