package util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

import java.io.InputStream;

/**
 * Author: brianfroschauer
 * Date: 2019-01-25
 */
public class ImageWriter {

    private static final String bucketName = "nemesis-8be1b.appspot.com";

    /**
     * Upload image to Google Cloud Storage for Nemesis.
     *
     * @param fileName image name.
     * @param inputStream image input stream.
     *
     * @return the media link of the new image.
     */
    public static String uploadImage(String fileName, InputStream inputStream) throws Exception {
        final FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setStorageBucket(bucketName)
                .build();

        FirebaseApp.initializeApp(options);
        final Bucket bucket = StorageClient.getInstance().bucket();
        return bucket.create(fileName, inputStream).getMediaLink();
    }
}
