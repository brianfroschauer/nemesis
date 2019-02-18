package resources;

/**
 * Author: brianfroschauer
 * Date: 2019-02-17
 */

//@Path("/payments")
public class PaymentResource {

    /*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPayment(PaymentData data) throws Exception {
        MercadoPago.SDK.setAccessToken("TEST-6866646233682371-012716-56b6fea9b848e49879c3d23c5b7db3fc-170532291");


        Payment payment = new Payment();
        payment.setTransactionAmount(150f)
                .setToken(data.getToken())
                .setDescription("Fantastic Plastic Gloves")
                .setInstallments(1)
                .setPaymentMethodId(data.getMethod())
                .setPayer(new Payer().setEmail("modesto@hotmail.com"));


        payment.save();

        System.out.println(payment.getStatus());

        return Response.ok().build();
    }
    */
}
