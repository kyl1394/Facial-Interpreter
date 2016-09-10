package FireBaseCalls;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by bptrent on 9/10/2016.
 */
public class InitSDK {


    public InitSDK() throws FileNotFoundException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream("FaceOff-b91129674a0b.json"))
                .setDatabaseUrl("https://faceoff-42676.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
    }


}
