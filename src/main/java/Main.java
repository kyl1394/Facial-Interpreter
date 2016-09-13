/**
 * Created by krohlfing on 8/30/2016.
 */

import GUI.DesktopUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String args[]) throws FileNotFoundException{
        initFirebaseSDK();

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

    public static void initFirebaseSDK(){
        //the following string contains our authentication information for the firebase server.
        // we were having issues reading in the .json file using a relative path so we got this.
        String a = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"faceoff-42676\",\n" +
                "  \"private_key_id\": \"b91129674a0bcd85b5a7a836a22a60c1ef20dfa2\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDWPs3brKn/A4gp\\n+oupxPt6r7McpfPAQ1G5AImCJSkLHitv4Pjf9RnqWcnArx++aVCBsAq+scLfeMk3\\nlom2yVG2Jvrk6m/wUDc043ZCoPJ1zjsa38bpsgH67UZpC4Os1CJfLGxRwhIJFrhK\\nZjcia0UondWqyLvwO3EWKgCBmyt97rMwCUnLb9tmXVm1Y06fyYgtCjhIb1Xwd2cZ\\nTHU9U4Kp99kXIx7jYtCUoS7Y4abZDiQGEBPSn9jsUPDdMGOSy4K9HRQOfzsLN95r\\nFgaB4Y/+UTx1NPUf9D8vaUxo6P0CNpNlDcNEoV4WANGYanYBNAksumYbQSKnqkWq\\nsl8HFBYXAgMBAAECggEAfQnwiVoPaRc9nApvAqlFgEgVCA5O4ZIomwmWN4/KkYON\\nJGGvvr3E30KB/mP26eF7LAV7b+o9asGK5leEjlqWxvGfEoVEyYN3hPWQKrjWaSzw\\nz/F0nSXYBAQBSQOAWRkB6nv5yAm1Dvrkmf9oBD2JAniDoIZBMabvjFj9LKdMTdU1\\nWep9iPGZvBUMYoQqTyc/DNNvuWRye9ZKexuGFwO0OkE04JF1CNtqImEDwFby6pcd\\nK337I/zelLZuJTujGj4aRirYEKBT4JanektI/hsdoPVCWwYH7Tb66rpSSM6tAQB9\\nlOyzJvEYylBcwgkDJA9naigl6tOdFKOomE13zeaCoQKBgQD63ArIXNVHL1iUhVpq\\nGtoCu3UKw4m5HogP4lOrtTamTRqcbpqV514yo/hXvALQ8CyUrHlbfx96TK2hyxPv\\ncY7txwZ9lTr+Ijah8b+DguFisANyNFcqubTh7yZ4s3cm3bYtgNL977AtZo0cAv//\\naadluuP+cE/illkcVUQqGxSZhwKBgQDaorD8Atv3Ax9nvIROR5qlamQl4+jOU3/O\\ngEsffierGJDqe73cW1tKikhadBvcSouAzwlHlUKMpbjGM5IiV6R+ZAbDloTa9zC+\\nGqP2BOWAIY4pPQPrv7hP9DolZwlfpvHM0D/G06U4SuOMcibuCo0hbEkHTWQW0SxX\\nftilAb+C8QKBgBcS3jsHruZZSICAs4xG4/6UV3OiAKzyTfmIC8IjbN9nACebVMSs\\n/1ERG+qV9HxbWv5hztfHnZGWM/JJx4+ilk7wm3w5ma9+XiAPKFpkU27MWcTLY6ke\\nY7DUWmmHuFHu2q7cxots3HuQjzIgZIONqEQHKKlikzZ4NcIRZa+pxf9JAoGAS0/F\\nZVnayXCi7cnrTUevBpHqY37pX8E1gj2BIx0CrxYsPQlY8kHp5XhSyg261xw6h6lJ\\nigXpQaeISYXULc5A1GTCS7dalk6V1LY6inFPWN62KJBi/F8zLx9ytBWVDgTkv+dw\\nQJ4BCK4LSgK8VD0ANmYllcVb19w/yPfhyp0aihECgYBZSdmEfnvk0p5tHGW7JO23\\nSusJDGvop34aVWXSawTbwHIqCe3NZY+mvtHn/izEkqXHDsNfJ7Sq+YANNgB8Zc/m\\nxwo9ABF91WTOAXaXk6frTX3Ll10hnvzy9NFUr6weKR84P6DCpLWXYo9uXhTEtUNA\\nSySZsSG73tXZEpDr/yijIA==\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"app-default@faceoff-42676.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"113456467467303819223\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://accounts.google.com/o/oauth2/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/app-default%40faceoff-42676.iam.gserviceaccount.com\"\n" +
                "}";
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(new ByteArrayInputStream(a.getBytes()))
                .setDatabaseUrl("https://faceoff-42676.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);

        //dump database contents to terminal (used for debugging)
        /*
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object res = dataSnapshot.getValue();  // returns contents of database as hash map
                System.out.println(res.toString()); //prints database in JSON format to console.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/test");
        DatabaseReference studentRef = ref.child("student");

        Student tmp = new Student("Sunny","","09/21/2016","https://url.somewhere.io");

        studentRef.push().setValue(tmp);  // this will add a student to the db if passed the object.

        System.out.println("data set to DB");

    }
}
