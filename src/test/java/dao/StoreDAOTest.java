package dao;

import model.Category;
import model.Product;
import model.Store;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: brianfroschauer
 * Date: 2018-12-29
 */
public class StoreDAOTest {

    @Test
    public void getProductsFromStoreByCategoryTest() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store(
                "name",
                "description");
        final Integer storeId = storeDAO.create(store);

        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Category category1 = new Category("category1");
        final Category category2 = new Category("category2");
        final Integer categoryId1 = categoryDAO.create(category1);
        final Integer categoryId2 = categoryDAO.create(category2);

        final ProductDAO productDAO = new ProductDAO();
        final Product product1 = new Product(
                "name1",
                1,
                1,
                category1);

        final Product product2 = new Product(
                "name2",
                1,
                1,
                category1);

        final Product product3 = new Product(
                "name3",
                1,
                1,
                category2);

        final Integer productId1 = productDAO.create(product1);
        final Integer productId2 = productDAO.create(product2);
        final Integer productId3 = productDAO.create(product3);

        storeDAO.addProductToStore(storeId, productId1);
        storeDAO.addProductToStore(storeId, productId2);
        storeDAO.addProductToStore(storeId, productId3);

        final List<Product> products1 = storeDAO.getProductsFromStore(storeId, categoryId1);
        final List<Product> products2 = storeDAO.getProductsFromStore(storeId, categoryId2);
        final List<Product> products3 = storeDAO.getProductsFromStore(storeId, 0);

        // Store should be contains exactly the products 1 and 2
        assertThat(products1).containsExactly(product1, product2);
        assertThat(products2).containsExactly(product3);
        assertThat(products3).isEmpty();
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        assertThat(productDAO.getAll(Product.class).isEmpty()).isFalse();

        // If the store is deleted, the products must also be eliminated (composition).
        // But the categories should not be eliminated.
        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
        assertThat(productDAO.getAll(Product.class).isEmpty()).isTrue();
        assertThat(categoryDAO.getAll(Category.class).isEmpty()).isFalse();
        assertThat(categoryDAO.get(Category.class, categoryId1).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId2).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId1).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId2).get());
        assertThat(categoryDAO.getAll(Category.class).isEmpty()).isTrue();
    }

    @Test
    public void getProductsFromStoreTest() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store(
                "name",
                "description");
        final Integer storeId = storeDAO.create(store);

        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Category category1 = new Category("category1");
        final Category category2 = new Category("category2");
        final Integer categoryId1 = categoryDAO.create(category1);
        final Integer categoryId2 = categoryDAO.create(category2);

        final ProductDAO productDAO = new ProductDAO();
        final Product product1 = new Product(
                "name1",
                1,
                1,
                category1);

        final Product product2 = new Product(
                "name2",
                1,
                1,
                category1);

        final Product product3 = new Product(
                "name3",
                1,
                1,
                category2);

        final Integer productId1 = productDAO.create(product1);
        final Integer productId2 = productDAO.create(product2);
        final Integer productId3 = productDAO.create(product3);

        storeDAO.addProductToStore(storeId, productId1);
        storeDAO.addProductToStore(storeId, productId2);
        storeDAO.addProductToStore(storeId, productId3);

        final List<Product> products = storeDAO.getProductsFromStore(storeId);

        // Store should be contains exactly the products 1, 2 and 3
        assertThat(products).containsExactly(product1, product2, product3);

        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        assertThat(productDAO.getAll(Product.class).isEmpty()).isFalse();

        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
        assertThat(productDAO.getAll(Product.class).isEmpty()).isTrue();
        assertThat(categoryDAO.getAll(Category.class).isEmpty()).isFalse();
        assertThat(categoryDAO.get(Category.class, categoryId1).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId2).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId1).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId2).get());
        assertThat(categoryDAO.getAll(Category.class).isEmpty()).isTrue();
    }

    @Test
    public void searchStoresTest() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store1 = new Store(
                "name1",
                "description1");

        final Store store2 = new Store(
                "name2",
                "description2");

        final Store store3 = new Store(
                "name3",
                "description3");

        storeDAO.create(store2);
        storeDAO.create(store1);
        storeDAO.create(store3);

        final List<Store> stores1 = storeDAO.searchStores("name");
        assertThat(stores1).containsExactly(store1, store2, store3);

        final List<Store> stores2 = storeDAO.searchStores("name1");
        assertThat(stores2).containsExactly(store1);

        storeDAO.delete(store1);
        storeDAO.delete(store2);
        storeDAO.delete(store3);
    }

    @Test
    public void searchProductsFromStoreTest() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store(
                "name",
                "description");
        final Integer storeId = storeDAO.create(store);

        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Category category1 = new Category("category1");
        final Category category2 = new Category("category2");
        final Integer categoryId1 = categoryDAO.create(category1);
        final Integer categoryId2 = categoryDAO.create(category2);

        final ProductDAO productDAO = new ProductDAO();
        final Product product1 = new Product(
                "name1",
                1,
                1,
                category1);

        final Product product2 = new Product(
                "name2",
                1,
                1,
                category1);

        final Product product3 = new Product(
                "name3",
                1,
                1,
                category2);

        final Integer productId1 = productDAO.create(product1);
        final Integer productId2 = productDAO.create(product2);
        final Integer productId3 = productDAO.create(product3);

        storeDAO.addProductToStore(storeId, productId1);
        storeDAO.addProductToStore(storeId, productId2);
        storeDAO.addProductToStore(storeId, productId3);

        final List<Product> products1 = storeDAO.searchProductsFromStore(storeId, "name");
        final List<Product> products2 = storeDAO.searchProductsFromStore(storeId, "1");

        assertThat(products1).containsExactly(product1, product2, product3);
        assertThat(products2).containsExactly(product1);

        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        assertThat(productDAO.getAll(Product.class).isEmpty()).isFalse();

        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
        assertThat(productDAO.getAll(Product.class).isEmpty()).isTrue();
        assertThat(categoryDAO.getAll(Category.class).isEmpty()).isFalse();
        assertThat(categoryDAO.get(Category.class, categoryId1).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId2).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId1).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId2).get());
        assertThat(categoryDAO.getAll(Category.class).isEmpty()).isTrue();
    }

    @Test
    public void addProductToStoreTest() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store(
                "name",
                "description");
        final Integer storeId = storeDAO.create(store);

        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Category category1 = new Category("category1");
        final Category category2 = new Category("category2");
        final Integer categoryId1 = categoryDAO.create(category1);
        final Integer categoryId2 = categoryDAO.create(category2);

        final ProductDAO productDAO = new ProductDAO();
        final Product product1 = new Product(
                "name1",
                1,
                1,
                category1);

        final Product product2 = new Product(
                "name2",
                1,
                1,
                category1);

        final Integer productId1 = productDAO.create(product1);
        final Integer productId2 = productDAO.create(product2);

        assertThat(productId1).isNotNull();
        assertThat(productId2).isNotNull();
        assertThat(productDAO.get(Product.class, productId1).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId2).isPresent()).isTrue();

        storeDAO.addProductToStore(storeId, productId1);
        storeDAO.addProductToStore(storeId, productId2);

        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        assertThat(storeDAO.get(Store.class, storeId).get().getProducts().size() == 2).isTrue();
        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
        assertThat(categoryDAO.get(Category.class, categoryId1).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId2).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId1).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId2).get());
    }

    @Test
    public void deleteProductFromStoreTest() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store(
                "name",
                "description");
        final Integer storeId = storeDAO.create(store);

        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final ProductDAO productDAO = new ProductDAO();
        final Product product = new Product(
                "name",
                1,
                1,
                category);

        final Integer productId = productDAO.create(product);
        storeDAO.addProductToStore(storeId, productId);

        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        assertThat(storeDAO.get(Store.class, storeId).get().getProducts().size() == 1).isTrue();

        productDAO.delete(productDAO.get(Product.class, productId).get());
        assertThat(storeDAO.get(Store.class, storeId).get().getProducts().isEmpty()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();

        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }
}
