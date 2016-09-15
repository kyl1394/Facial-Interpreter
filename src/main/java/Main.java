/**
 * Created by krohlfing on 8/30/2016.
 */

import API.Kairos;
import FireBaseCalls.FirebaseSDK;
import FireBaseCalls.Student;
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
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;

public class Main {
    public static void main(String args[]) throws FileNotFoundException{
        FirebaseSDK.initFirebaseSDK();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/test");
        DatabaseReference studentRef = ref.child("student");

        Student tmp = new Student("Maggie","Wishes she was as cool as Kyle","2/1/0020");

        //studentRef.push().setValue(tmp);  // this will add a student to the db if passed the object.

        DesktopUI.main(args);
    }
}