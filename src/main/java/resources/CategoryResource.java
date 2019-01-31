package resources;

import dao.CategoryDAO;
import dao.exception.ConstraintViolationException;
import filter.Secured;
import model.Category;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 2019-01-29
 */
@Path("/categories")
public class CategoryResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Get category with de specified ID.
     *
     * @param categoryId to get from database.
     *
     * @return a store with the specified ID in the response.
     */
    @GET
    @Path("/{categoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategory(@PathParam("categoryId") Integer categoryId) {
        final CategoryDAO categoryDAO = new CategoryDAO();
        final Optional<Category> optionalCategory = categoryDAO.get(Category.class, categoryId);

        if (optionalCategory.isPresent()) {
            final Category category = optionalCategory.get();
            return Response.ok(category).build();
        }

        throw new dao.exception.NotFoundException("Category, " + categoryId + ", is not found");
    }

    /**
     * Get category with de specified ID.
     *
     * @return a store with the specified ID in the response.
     */
    @GET
    @Path("/stores/{storeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsedCategories(@PathParam("storeId") Integer storeId) {
        final CategoryDAO categoryDAO = new CategoryDAO();
        final List<Category> categories = categoryDAO.getUsedCategories(storeId);
        return Response.ok(categories).build();
    }

    /**
     * Add a new store in the database.
     *
     * @param category to be created.
     *
     * @return the URI of the new resource in the response.
     */
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategory(@Valid Category category) {
        final CategoryDAO categoryDAO = new CategoryDAO();
        try {
            final Integer categoryId = categoryDAO.create(category);
            final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(categoryId.toString());
            return Response.created(builder.build()).entity(category).build();
        } catch (PersistenceException e) {
            throw new ConstraintViolationException("Category, " + category.getCategory() + ", already exists");
        }
    }

}
