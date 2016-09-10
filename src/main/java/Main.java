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
        Kairos.enroll("http://i.imgur.com/ffVXNwd.jpg", "Kyle", "TestingGallery");
        Kairos.recognize("http://i.imgur.com/sDdcDwd.jpg", "TestingGallery");
        DesktopUI.main(args);
    }
}
