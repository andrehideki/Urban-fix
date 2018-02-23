package com.mobile.urbanfix.urban_fix;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SystemUtils {

    public static void showMessage(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    public static boolean askPermission(Activity activity, String permission, int requestCode) {
        if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
            return false;
        }
        return true;
    }

    public static File createTempFile(Context context) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()));
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File f = File.createTempFile(sb.toString(), ".jpg", storageDir);
        Log.i("Script", "Arquivo temporário criado: " + f.getAbsolutePath());
        return f;
    }

    public static Bitmap getResizedBitmap(int width, int heigh, String currentPath) {

        Bitmap photoBitmap = BitmapFactory.decodeFile(currentPath);
        photoBitmap = Bitmap.createScaledBitmap(photoBitmap, width, heigh, false);

        return photoBitmap;
    }

    public static boolean saveBitmap(String filePath, Bitmap bitmap) {
        File f = new File(filePath);
        try {
            OutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            Log.e("Script", e.getMessage());
        }
        return false;
    }


}
