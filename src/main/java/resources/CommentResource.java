package resources;

import dao.CommentDAO;
import filter.Secured;
import model.Comment;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 2019-02-14
 */
@Path("/comments")
public class CommentResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Get comment with de specified ID.
     *
     * @param commentId to get from database.
     *
     * @return a comment with the specified ID in the response.
     */
    @GET
    @Path("/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComment(@PathParam("commentId") Integer commentId) {
        final CommentDAO commentDAO = new CommentDAO();
        final Optional<Comment> optionalComment = commentDAO.get(Comment.class, commentId);

        if (optionalComment.isPresent()) {
            final Comment comment = optionalComment.get();
            return Response.ok(comment).build();
        }

        throw new dao.exception.NotFoundException("Comment, " + commentId + ", is not found");
    }

    /**
     * Add a new comment to the product.
     *
     * @param comment to be created.
     *
     * @return the created comment in the response.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(Comment comment) {
        final CommentDAO commentDAO = new CommentDAO();
        final Integer commentId = commentDAO.create(comment);
        final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(commentId.toString());
        return Response.created(builder.build()).entity(comment).build();
    }

    /**
     * Delete comment with the specified ID.
     *
     * @param commentId to be deleted.
     *
     * @return a 204 HTTP status to confirm that the comment has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{commentId}")
    public Response deleteComment(@PathParam("commentId") Integer commentId) {
        final CommentDAO commentDAO = new CommentDAO();
        final Optional<Comment> optionalComment = commentDAO.get(Comment.class, commentId);

        if (optionalComment.isPresent()) {
            final Comment comment = optionalComment.get();
            commentDAO.delete(comment);
            return Response.noContent().build();
        }

        throw new dao.exception.NotFoundException("Comment, " + commentId + ", is not found");
    }

}
