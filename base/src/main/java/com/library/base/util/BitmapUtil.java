package com.library.base.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.library.base.GlobalContext;
import com.library.base.LogConsole;
import com.library.base.widget.toast.ToastSimple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {


    /**
     * 调用系统分享
     *
     * @param context
     * @param picFilePath
     * @return
     */
    public static boolean sharePic(Context context, String picFilePath, String desc) {
        File shareFile = new File(picFilePath);
        if (!shareFile.exists())
            return false;
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", shareFile);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
        }
        intent.setType("image/*");
        Intent chooser = Intent.createChooser(intent, desc);
        context.startActivity(chooser);
        return true;
    }


    /**
     * 调用系统分享bitmap
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static boolean shareBitmap(Context context, Bitmap bitmap, String desc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("image/*");
        Intent chooser = Intent.createChooser(intent, desc);
        context.startActivity(chooser);
        return true;
    }


    /**
     * 保存图片到缓存文件
     */
    public static String saveBitmapCache(Bitmap bitmap, String name, int quality) {
        if (bitmap == null) {
            return null;
        }
        // 首先保存图片
        File appDir = GlobalContext.app().getCacheDir();
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        if (!TextUtils.isEmpty(name)) {
            fileName = name + ".jpg";
        }
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LogConsole.e("saveBitmapCache>> 2.file=" + file.length());
        return file.toString();
    }


    /**
     * 转圆角图片
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();
        return output;
    }


    /**
     * 保存图片到相册
     */
    public static String saveImageToGallery(Bitmap bitmap, String name) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastSimple.show("can not find sdcard", 1);
            return null;
        }
        // 首先保存图片
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsoluteFile();
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        if (!TextUtils.isEmpty(name)) {
            fileName = name + ".jpg";
        }
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(GlobalContext.app().getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
        } // 最后通知图库更新

        GlobalContext.app().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "")));

        return file.toString();
    }
}
