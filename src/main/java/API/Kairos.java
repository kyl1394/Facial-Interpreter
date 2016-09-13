package API;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by krohlfing
 */
public class Kairos {
    public static void enroll(String imageUrl, String subjectId, String galleryName) {
        enroll(imageUrl, subjectId, galleryName, "", "");
    }

    public static void enroll(String imageUrl, String subjectId, String galleryName, String selector) {
        enroll(imageUrl, subjectId, galleryName, selector, "");
    }

    public static void enroll(String imageUrl, String subjectId, String galleryName, String selector, String symmetricFill) {
        Client client = ClientBuilder.newClient();
        String text = "{\n" +
                "    \"image\":\"" + imageUrl + "\",\n" +
                "    \"subject_id\":\"" + subjectId + "\",\n" +
                "    \"gallery_name\":\"" + galleryName + "\",\n";
        if (!selector.equals("")) {
            text += "\"selector\":\"" + selector + "\",\n";
        } else {
            text += "\"selector\":\"SETPOSE\",\n";
        }
        if (!symmetricFill.equals("")) {
            text += "\"symmetricFill\":\"" + symmetricFill + "\"\n" +
                    "}";
        } else {
            text += "\"symmetricFill\":\"true\"\n" +
                    "}";
        }

        Entity<String> payload = Entity.text(text);

        Response response = client.target("https://api.kairos.com/enroll")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header("app_id", "b9ed4d88")
                .header("app_key", "70cb2baf9f2af37e3b7cb90e4dfb88db")
                .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));
    }

    public static void recognize(String imageUrl, String galleryName) {
        recognize(imageUrl, galleryName, "0.2");
    }

    public static void recognize(String imageUrl, String galleryName, String threshold) {
        Client client = ClientBuilder.newClient();
        Entity<String>  payload = Entity.text("{\n" +
                "    \"image\":\" " + imageUrl + " \",\n" +
                "    \"gallery_name\":\"" + galleryName + "\",\n" +
                "    \"threshold\":\"" + threshold + "\"\n" +
                "}");
        Response response = client.target("http://api.kairos.com/recognize")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("app_id", "b9ed4d88")
                .header("app_key", "70cb2baf9f2af37e3b7cb90e4dfb88db")
                .post(payload);

        String body = response.readEntity(String.class);

        JsonElement root = new JsonParser().parse(body);
        JsonArray jsonArray =  root.getAsJsonObject().get("images").getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject transaction = jsonArray.get(i).getAsJsonObject().get("transaction").getAsJsonObject();
            System.out.println("Found: " + transaction.get("subject") + " at X: " + transaction.get("topLeftX") +  ", Y: " + transaction.get("topLeftY")  +  ", Width: " + transaction.get("width")  +  ", height: " + transaction.get("height") + "; ");
        }
//        System.out.println("status: " + response.getStatus());
//        System.out.println("headers: " + response.getHeaders());
//        System.out.println("body:" + body);
    }

    public static void listGallery() {
        Client client = ClientBuilder.newClient();
        Entity<String> payload = Entity.text("");
        Response response = client.target("https://api.kairos.com/gallery/list_all")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .header("app_id", "b9ed4d88")
                .header("app_key", "70cb2baf9f2af37e3b7cb90e4dfb88db")
                .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));
    }

    public static void removeGallery(String galleryName) {
        Client client = ClientBuilder.newClient();
        Entity payload = Entity.json("{  \"gallery_name\": \"" + galleryName + "\"}");
        Response response = client.target("https://api.kairos.com/gallery/remove")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("app_id", "b9ed4d88")
                .header("app_key", "70cb2baf9f2af37e3b7cb90e4dfb88db")
                .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));
    }

    public static void viewGalleryFaces(String galleryName) {
        Client client = ClientBuilder.newClient();
        Entity payload = Entity.json("{  \"gallery_name\": \"" + galleryName + "\"}");
        Response response = client.target("https://api.kairos.com/gallery/view")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .header("app_id", "b9ed4d88")
                        .header("app_key", "70cb2baf9f2af37e3b7cb90e4dfb88db")
                        .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));
    }

    public static void removeFace(String galleryName, String subjectId) {
        Client client = ClientBuilder.newClient();
        Entity payload = Entity.json("{  \"gallery_name\": \"" + galleryName + "\",  \"subject_id\": \"" + subjectId + "\"}");
        Response response = client.target("https://api.kairos.com/gallery/remove_subject")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .header("app_id", "b9ed4d88")
                        .header("app_key", "70cb2baf9f2af37e3b7cb90e4dfb88db")
                        .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));
    }

}
