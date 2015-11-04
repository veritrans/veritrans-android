package id.co.veritrans.sdk.core;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by chetan on 19/10/15.
 */
public class StorageDataHandler {

    private static final String TAG = "StorageData";

    public StorageDataHandler() {
    }


    /**
     * This method writes given object in file, it looks for file on internal storage.
     * <p> To read data use {@link #readObject(android.content.Context, String)} <p>
     *
     * @param context application context.
     * @param key     file name.
     * @param object  object to store.
     * @throws java.io.FileNotFoundException Thrown when a file specified by a file cannot be found.
     * @throws java.io.IOException           Thrown when a failed to write into file.
     */
    public synchronized static void writeObject(final Context context, final String key,
                                                final Object object) throws IOException {

        if (context == null || key == null || object == null) {
            Log.e(TAG + " Error ", "invalid input to Storage handler.");
        } else {
            final FileOutputStream fileOutputStream = context.openFileOutput(key,
                    Context.MODE_PRIVATE);
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            fileOutputStream.close();
        }
    }

    /**
     * This method reads object from given file, it looks for file on internal storage.
     * <p> To write data use {@link #writeObject(android.content.Context, String, Object)} .<p>
     *
     * @param context application context.
     * @param key     file name.
     * @return return the object read from the file.
     * @throws java.io.FileNotFoundException Thrown when a file specified by a file cannot be found.
     * @throws ClassNotFoundException        Thrown when a class loader is unable to find a class.
     * @throws java.io.IOException           Thrown when a failed to read from file.
     */
    public synchronized static Object readObject(final Context context, final String key) throws
            ClassNotFoundException, IOException {

        if (context == null || key == null) {
            Log.e(TAG +" Error ", "invalid input to Storage handler.");
            return null;
        } else {
            final FileInputStream fileInputStream = context.openFileInput(key);
            final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            final Object object = objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return object;
        }
    }
}
