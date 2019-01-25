package dao;

import model.*;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SaleDAOTest {

    @Test
    public void getUserPurchasesTest() {
        final UserDAO userDAO = new UserDAO();
        final StoreDAO storeDAO = new StoreDAO();
        final CategoryDAO categoryDAO = new CategoryDAO();
        final ProductDAO productDAO = new ProductDAO();
        final SaleDAO saleDAO = new SaleDAO();

        final User user = new User(
                "mail@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Store store = new Store(
                "name",
                "description");
        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product1 = new Product("product1", 1, 1, category);
        final Product product2 = new Product("product2", 1, 1, category);
        final Product product3 = new Product("product3", 1, 1, category);
        final Product product4 = new Product("product4", 1, 1, category);

        final Integer userId = userDAO.create(user);
        final Integer storeId = storeDAO.create(store);
        final Integer productId1 = productDAO.create(product1);
        final Integer productId2 = productDAO.create(product2);
        final Integer productId3 = productDAO.create(product3);
        final Integer productId4 = productDAO.create(product4);

        storeDAO.addProductToStore(storeId, productId1);
        storeDAO.addProductToStore(storeId, productId2);
        storeDAO.addProductToStore(storeId, productId3);
        storeDAO.addProductToStore(storeId, productId4);

        // Sale 1
        userDAO.addProductToUser(userId, productId1);
        userDAO.addProductToUser(userId, productId2);
        final List<Product> products1 = userDAO.getProductsFromUser(userId);
        final Sale sale1 = new Sale(store, user, products1);
        final Integer saleId1 = saleDAO.create(sale1);

        // Sale 2
        userDAO.addProductToUser(userId, productId3);
        userDAO.addProductToUser(userId, productId4);
        final List<Product> products2 = userDAO.getProductsFromUser(userId);
        final Sale sale2 = new Sale(store, user, products2);
        final Integer saleId2 = saleDAO.create(sale2);


        assertThat(saleDAO.getUserPurchases(userId).size() == 2).isTrue();

        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());

        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        storeDAO.delete(storeDAO.get(Store.class, storeId).get());

        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());

        // If user is deleted, the purchase must be deleted
        assertThat(saleDAO.get(Sale.class, saleId1).isPresent()).isFalse();
        assertThat(saleDAO.get(Sale.class, saleId2).isPresent()).isFalse();
    }
}