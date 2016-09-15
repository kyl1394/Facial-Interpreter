package FireBaseCalls;



import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by bptrent on 9/10/2016.
 */
public class FirebaseSDK {


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
    }

    private static int studentFound = 0;
    private static Student queriedStudent;

    // This functino will look through the database for the student name
    public static Student findStudentInfo(String name){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/test/student");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterator iter = snapshot.getChildren().iterator();
                while (iter.hasNext()) {
                    DataSnapshot snap = (DataSnapshot) iter.next();
                    if (snap.child("name").getValue().equals(name)) {
                        queriedStudent = new Student(snap.child("name").getValue().toString(), snap.child("notes").getValue().toString(), snap.child("lastSeen").getValue().toString());
                        studentFound = 1;
                        return;
                    }
                }

                studentFound = -1;
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                studentFound  = -1;
                queriedStudent = null;
                return;
            }
        });

        while (studentFound == 0) {
            // You must give this loop something to do otherwise running it out of debug mode will just cause it to hang
            // for some unknown reason...
            System.out.println("loading...");
        }

        studentFound = 0;
        return queriedStudent;
    }
}
