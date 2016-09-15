package API;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by krohlfing on 9/10/2016.
 */
public class ImageUploader {
    private Cloudinary cloudinary;
    public ImageUploader() {
        Map config = new HashMap();
        config.put("cloud_name", "di1dnijpw");
        config.put("api_key", "126987751573914");
        config.put("api_secret", "PU3pzVV2R3pDjfk5Nxge1tk919I");
        cloudinary = new Cloudinary(config);
    }

    public String upload(String imagePath) throws IOException {
        Map text = cloudinary.uploader().upload(imagePath, ObjectUtils.emptyMap());
        return (String) text.get("url");
    }
}
