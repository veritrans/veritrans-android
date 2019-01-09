package com.midtrans.sdk.corekit.utilities;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import static com.midtrans.sdk.corekit.utilities.ValidationHelper.isNotEmpty;

public class InstallationHelper {

    private static final String TAG = "InstallationHelper";
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String generatedRandomID(Context context) {
        String generatedRandomId = null;
        if (isNotEmpty(context)) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                generatedRandomId = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return generatedRandomId;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}