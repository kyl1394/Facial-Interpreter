/**
 * Created by krohlfing on 8/30/2016.
 */

import GUI.DesktopUI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Main {
    public static void main(String args[]) {
        DesktopUI.main(args);
    }

    public static void enroll() {
        Client client = ClientBuilder.newClient();
        Entity<String> payload = Entity.text("{\n" +
                "    \"image\":\" http://media.kairos.com/kairos-elizabeth.jpg \",\n" +
                "    \"subject_id\":\"subtest1\",\n" +
                "    \"gallery_name\":\"gallerytest1\",\n" +
                "    \"selector\":\"SETPOSE\",\n" +
                "    \"symmetricFill\":\"true\"\n" +
                "}");
        Response response = client.target("https://api.kairos.com/enroll")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header("app_id", "b9ed4d88")
                .header("app_key", "70cb2baf9f2af37e3b7cb90e4dfb88db")
                .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));
    }
}
