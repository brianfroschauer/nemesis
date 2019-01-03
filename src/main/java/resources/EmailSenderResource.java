package resources;

import model.Product;
import util.EmailSender;

import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 2018-12-31
 */
public class EmailSenderResource {


    private void sendEmail(String recipient, String username, List<Product> productList) {
        EmailSender emailSender = new EmailSender();
        StringBuilder products = new StringBuilder();
        for (Product product : productList) {
            products.append(product.getName()).append(", ");
        }
        String subject = "Purchase successful";
        String text = "Hola, " + username +
                "\n\nFelicitaciones de parte de Nemesis por haber terminado exitosamente con el proceso de compra!" +
                "\nEstos fueron los productos adquiridos: \n\n" + products +
                "\n\nQue tengas un buen día. " +
                "\n\nGracias por comprar con nosotros.";
        emailSender.send(recipient, subject, text);
    }

}
