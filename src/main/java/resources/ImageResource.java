package resources;

import dao.exception.DAOException;
import dao.exception.NotFoundException;
import filter.Secured;
import org.glassfish.jersey.media.multipart.FormDataParam;
import util.Image;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Author: brianfroschauer
 * Date: 2019-01-05
 */
@Path("(/images")
public class ImageResource {

    private static final String imagePath = "/Users/brianfroschauer/sandbox/nemesis/src/main/resources/images/";

    /**
     * Gets entity image.
     *
     * @param entityId to identify the image.
     * @return the image in the response.
     */
    @GET
    @Path("/{entityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserImage(@PathParam("entityId") Integer entityId) {
        try {
            final File file = new File(imagePath + entityId + ".jpg");
            if (file.exists()) {
                final Image image = getUserImage(file);
                return Response.ok(image).build();
            } else {
                final File defaultFile = new File(imagePath + "default.png");
                final Image defaultImage = getUserImage(defaultFile);
                return Response.ok(defaultImage).build();
            }
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    @POST
    @Secured
    @Path("/{entityId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadUserImage(@PathParam("entityId") Integer userId,
                                    @FormDataParam("file") InputStream inputStream) {
        try {
            final String fileName = "/" + userId.toString() + ".jpg";
            writeImage(inputStream, fileName);
            return Response.ok("Image uploaded successfully").build();
        } catch (RuntimeException | IOException e) {
            throw new DAOException(e.getMessage());
        }
    }

    private Image getUserImage(File file) {
        try {
            final String encodedFile = Base64
                    .getEncoder()
                    .withoutPadding()
                    .encodeToString(Files.readAllBytes(file.toPath()));
            return new Image(encodedFile);
        } catch (IOException e) {
            throw new NotFoundException("Image not found");
        }
    }

    private void writeImage(InputStream inputStream, String fileName) throws IOException {
        final File file = new File(imagePath + fileName);
        final BufferedImage image = ImageIO.read(inputStream);
        ImageIO.write(image, "png", file);
    }
}
