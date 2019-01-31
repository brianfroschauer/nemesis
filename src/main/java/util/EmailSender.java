package util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Author: brianfroschauer
 * Date: 13/06/2018
 */
public class EmailSender {

    private static final String username = "nemesisservice2018@gmail.com";
    private static final String password = "Nemesis2018";

    private static final String html = "<!doctype html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "    <title>Simple Transactional Email</title>\n" +
            "    <style media=\"all\" type=\"text/css\">\n" +
            "        @media all {\n" +
            "            .btn-primary table td:hover {\n" +
            "                background-color: #34495e !important;\n" +
            "            }\n" +
            "            .btn-primary a:hover {\n" +
            "                background-color: #34495e !important;\n" +
            "                border-color: #34495e !important;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        @media all {\n" +
            "            .btn-secondary a:hover {\n" +
            "                border-color: #34495e !important;\n" +
            "                color: #34495e !important;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        @media only screen and (max-width: 620px) {\n" +
            "            table[class=body] h1 {\n" +
            "                font-size: 28px !important;\n" +
            "                margin-bottom: 10px !important;\n" +
            "            }\n" +
            "            table[class=body] h2 {\n" +
            "                font-size: 22px !important;\n" +
            "                margin-bottom: 10px !important;\n" +
            "            }\n" +
            "            table[class=body] h3 {\n" +
            "                font-size: 16px !important;\n" +
            "                margin-bottom: 10px !important;\n" +
            "            }\n" +
            "            table[class=body] p,\n" +
            "            table[class=body] ul,\n" +
            "            table[class=body] ol,\n" +
            "            table[class=body] td,\n" +
            "            table[class=body] span,\n" +
            "            table[class=body] a {\n" +
            "                font-size: 16px !important;\n" +
            "            }\n" +
            "            table[class=body] .wrapper,\n" +
            "            table[class=body] .article {\n" +
            "                padding: 10px !important;\n" +
            "            }\n" +
            "            table[class=body] .content {\n" +
            "                padding: 0 !important;\n" +
            "            }\n" +
            "            table[class=body] .container {\n" +
            "                padding: 0 !important;\n" +
            "                width: 100% !important;\n" +
            "            }\n" +
            "            table[class=body] .header {\n" +
            "                margin-bottom: 10px !important;\n" +
            "            }\n" +
            "            table[class=body] .main {\n" +
            "                border-left-width: 0 !important;\n" +
            "                border-radius: 0 !important;\n" +
            "                border-right-width: 0 !important;\n" +
            "            }\n" +
            "            table[class=body] .btn table {\n" +
            "                width: 100% !important;\n" +
            "            }\n" +
            "            table[class=body] .btn a {\n" +
            "                width: 100% !important;\n" +
            "            }\n" +
            "            table[class=body] .img-responsive {\n" +
            "                height: auto !important;\n" +
            "                max-width: 100% !important;\n" +
            "                width: auto !important;\n" +
            "            }\n" +
            "            table[class=body] .alert td {\n" +
            "                border-radius: 0 !important;\n" +
            "                padding: 10px !important;\n" +
            "            }\n" +
            "            table[class=body] .span-2,\n" +
            "            table[class=body] .span-3 {\n" +
            "                max-width: none !important;\n" +
            "                width: 100% !important;\n" +
            "            }\n" +
            "            table[class=body] .receipt {\n" +
            "                width: 100% !important;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        @media all {\n" +
            "            .ExternalClass {\n" +
            "                width: 100%;\n" +
            "            }\n" +
            "            .ExternalClass,\n" +
            "            .ExternalClass p,\n" +
            "            .ExternalClass span,\n" +
            "            .ExternalClass font,\n" +
            "            .ExternalClass td,\n" +
            "            .ExternalClass div {\n" +
            "                line-height: 100%;\n" +
            "            }\n" +
            "            .apple-link a {\n" +
            "                color: inherit !important;\n" +
            "                font-family: inherit !important;\n" +
            "                font-size: inherit !important;\n" +
            "                font-weight: inherit !important;\n" +
            "                line-height: inherit !important;\n" +
            "                text-decoration: none !important;\n" +
            "            }\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body class=\"\" style=\"font-family: sans-serif; -webkit-font-smoothing: antialiased; font-size: 14px; line-height: 1.4; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; background-color: #f6f6f6; margin: 0; padding: 0;\">\n" +
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; background-color: #f6f6f6;\" width=\"100%\" bgcolor=\"#f6f6f6\">\n" +
            "    <tr>\n" +
            "        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\" valign=\"top\">&nbsp;</td>\n" +
            "        <td class=\"container\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; display: block; Margin: 0 auto !important; max-width: 580px; padding: 10px; width: 580px;\" width=\"580\" valign=\"top\">\n" +
            "            <div class=\"content\" style=\"box-sizing: border-box; display: block; Margin: 0 auto; max-width: 580px; padding: 10px;\">\n" +
            "\n" +
            "                <!-- START CENTERED WHITE CONTAINER -->\n" +
            "                <span class=\"preheader\" style=\"color: transparent; display: none; height: 0; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; visibility: hidden; width: 0;\"></span>\n" +
            "                <table class=\"main\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; background: #fff; border-radius: 3px;\" width=\"100%\">\n" +
            "\n" +
            "                    <!-- START MAIN CONTENT AREA -->\n" +
            "                    <tr>\n" +
            "                        <td class=\"wrapper\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; box-sizing: border-box; padding: 20px;\" valign=\"top\">\n" +
            "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\" width=\"100%\">\n" +
            "                                <tr>\n" +
            "                                    <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\" valign=\"top\">\n" +
            "                                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">Hola,</p>\n" +
            "                                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">Felicitaciones de parte de Nemesis por haber terminado exitosamente con el proceso de compra.\n" +
            "                                            Aquí puedes ver los productos adquiridos: </p>\n" +
            "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"btn btn-primary\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; box-sizing: border-box;\" width=\"100%\">\n" +
            "                                            <tbody>\n" +
            "                                            <tr>\n" +
            "                                                <td align=\"left\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; padding-bottom: 15px;\" valign=\"top\">\n" +
            "                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: auto;\">\n" +
            "                                                        <tbody>\n" +
            "                                                        <tr>\n" +
            "                                                            <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; background-color: #3498db; border-radius: 5px; text-align: center;\" valign=\"top\" bgcolor=\"#3498db\" align=\"center\"> <a href=\"http://htmlemail.io\" target=\"_blank\" style=\"display: inline-block; color: #ffffff; background-color: #3498db; border: solid 1px #3498db; border-radius: 5px; box-sizing: border-box; cursor: pointer; text-decoration: none; font-size: 14px; font-weight: bold; margin: 0; padding: 12px 25px; text-transform: capitalize; border-color: #3498db;\">Ver productos</a> </td>\n" +
            "                                                        </tr>\n" +
            "                                                        </tbody>\n" +
            "                                                    </table>\n" +
            "                                                </td>\n" +
            "                                            </tr>\n" +
            "                                            </tbody>\n" +
            "                                        </table>\n" +
            "                                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">Gracias por comprar con nosotros!</p>\n" +
            "                                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">Que tengas un buen día.</p>\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                            </table>\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "\n" +
            "                    <!-- END MAIN CONTENT AREA -->\n" +
            "                </table>\n" +
            "\n" +
            "                <!-- START FOOTER -->\n" +
            "                <div class=\"footer\" style=\"clear: both; padding-top: 10px; text-align: center; width: 100%;\">\n" +
            "                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\" width=\"100%\">\n" +
            "                        <tr>\n" +
            "                            <td class=\"content-block\" style=\"font-family: sans-serif; vertical-align: top; padding-top: 10px; padding-bottom: 10px; font-size: 12px; color: #999999; text-align: center;\" valign=\"top\" align=\"center\">\n" +
            "                                <span class=\"apple-link\" style=\"color: #999999; font-size: 12px; text-align: center;\">Nemesis, Pilar, Buenos Aires</span>\n" +
            "                                <br> No quieres recibir más correos? <a href=\"http://i.imgur.com/CScmqnj.gif\" style=\"text-decoration: underline; color: #999999; font-size: 12px; text-align: center;\">Unsubscribe</a>.\n" +
            "                            </td>\n" +
            "                        </tr>\n" +
            "                    </table>\n" +
            "                </div>\n" +
            "\n" +
            "                <!-- END FOOTER -->\n" +
            "\n" +
            "                <!-- END CENTERED WHITE CONTAINER --></div>\n" +
            "        </td>\n" +
            "        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\" valign=\"top\">&nbsp;</td>\n" +
            "    </tr>\n" +
            "</table>\n" +
            "</body>\n" +
            "</html>";

    public void send(String recipient, String subject) {
        final Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setContent(html, "text/html");
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
