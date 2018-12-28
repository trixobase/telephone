package cm.trixobase.telephone.systeme.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cm.trixobase.telephone.Telephone;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public class ImageProcess {

    public static Drawable byteArrayToDrawable(Context context, byte[] bytes) {
        return bitmapToDrawable(context, byteArrayToBitmap(bytes));
    }

    public static byte[] drawableToByteArray(Drawable drawable) {
        return bitmapToByteArray(drawableToBitmap(drawable));
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private static Bitmap byteArrayToBitmap(byte[] bytes) {
        InputStream is = new ByteArrayInputStream(bytes);
        return BitmapFactory.decodeStream(is);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static void saveGroupPicture(Context context, String groupName, Bitmap groupPicture) {
        try {
            FileOutputStream out = context.openFileOutput(computePictureGroup(groupName), MODE_PRIVATE);
            out.write(ImageProcess.bitmapToByteArray(groupPicture));
            if(out != null) {
                out.close();
            }
            Log.e(Telephone.Log, groupName + ": Picture saved");
        } catch (IOException e) {
            Log.e(Telephone.Log, groupName + ": Fail to save picture group: " + e);
        }
    }

    public static void updateGroupPicture(Context context, String oldGroupName, String newGroupName, Bitmap groupPicture) {
        try {
            context.deleteFile(computePictureGroup(oldGroupName));
            FileOutputStream out = context.openFileOutput(computePictureGroup(newGroupName), MODE_PRIVATE);
            out.write(ImageProcess.bitmapToByteArray(groupPicture));
            if(out != null) {
                out.close();
            }
            Log.e(Telephone.Log, oldGroupName + ": Picture updated to: " + newGroupName);
        } catch (IOException e) {
            Log.e(Telephone.Log, oldGroupName + ": Fail to update picture group: " + e);
        }
    }

    public static Bitmap getGroupPicture(Context context, String groupName) {
        FileInputStream in = null;
        Bitmap bm = null;
        try {
            in = context.openFileInput(computePictureGroup(groupName));
            bm = BitmapFactory.decodeStream(in);
            Log.w(Telephone.Log, groupName + ": Picture group found");
        } catch (FileNotFoundException e) {
            Log.w(Telephone.Log, groupName + ": Picture group not found");
        }
        return bm;
    }

    public static void saveContactPicture(Context context, String contactName, Bitmap contactPicture) {
        try {
            FileOutputStream out = context.openFileOutput(computePictureContact(contactName.trim()), MODE_PRIVATE);
            out.write(ImageProcess.bitmapToByteArray(contactPicture));
            if(out != null) {
                out.close();
            }
            Log.e(Telephone.Log, contactName + ": Picture contact saved");
        } catch (IOException e) {
            Log.e(Telephone.Log, contactName + ": Fail to save picture contact: " + e);
        }
    }

    public static Bitmap getContactPicture(Context context, String contactName) {
        Bitmap bm = null;
        try {
            FileInputStream in = context.openFileInput(computePictureContact(contactName));
            bm = BitmapFactory.decodeStream(in);
            Log.w(Telephone.Log, contactName + ": Picture contact found");
        } catch (FileNotFoundException e) {
            Log.w(Telephone.Log, contactName + ": Picture contact not found");
        }
        return bm;
    }

    private static String computePictureGroup(String groupName) {
        return "picture_group_" + groupName + ".jpeg";
    }

    private static String computePictureContact(String contactName) {
        return "picture_contact_" + contactName + ".jpeg";
    }

}