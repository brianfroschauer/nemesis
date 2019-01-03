package dao;

import model.Category;
import model.Product;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: brianfroschauer
 * Date: 28/04/2018
 */
public class ProductDAOTest {

    @Test
    public void searchProductTest() {
        final ProductDAO productDAO = new ProductDAO();
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();

        final Category category = new Category("category");

        final Integer categoryId = categoryDAO.create(category);

        final Product product1 = new Product(
                "name1",
                0,
                1,
                category);

        final Product product2 = new Product(
                "name2",
                0,
                1,
                category);

        final Integer productId1 = productDAO.create(product1);
        final Integer productId2 = productDAO.create(product2);

        final List<Product> list1 = productDAO.searchProducts("name");
        final List<Product> list2 = productDAO.searchProducts("name1");

        assertThat(list1).containsExactly(product1, product2);
        assertThat(list2).containsExactly(product1);

        productDAO.delete(product1);
        productDAO.delete(product2);
        categoryDAO.delete(category);

        assertThat(productDAO.get(Product.class, productId1).isPresent()).isFalse();
        assertThat(productDAO.get(Product.class, productId2).isPresent()).isFalse();

        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isFalse();
    }
}