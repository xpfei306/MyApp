package xpfei.myapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author 许鹏飞
 * @Description: 图片的质量压缩
 * @date 2016-4-1
 */
public class ImageUtils {

    /**
     * 图片的质量压缩
     *
     * @param bmp  Bitmap对象
     * @param file 存储路径
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Write(File file, String path) {
        compressBmpToFile(getBitmapFromLocal(path), file);
    }

    /**
     * 通过图片路径获得bitmap（无压缩形式）
     *
     * @param pathName 图片路径
     * @return Bitmap
     */
    public static Bitmap getBitmapFromLocal(String pathName) {
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        return bitmap;
    }


    public static Drawable getForegroundDrawable(Context context, Bitmap bitmap) {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        final float widthHeightSize = (float) (DisplayUtil.getScreenWidth(context)
                * 1.0 / DisplayUtil.getScreenHeight(context) * 1.0);
        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
                bitmap.getHeight());
        /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 50, bitmap
                .getHeight() / 50, false);
        /*模糊化*/
        Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true);
        Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }

}
