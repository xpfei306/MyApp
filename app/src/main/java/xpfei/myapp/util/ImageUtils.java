package xpfei.myapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @Description: 图片的质量压缩
 * @author 许鹏飞
 * @date 2016-4-1
 * 
 */
public class ImageUtils {

	/**
	 * 图片的质量压缩
	 * 
	 * @param bmp
	 *            Bitmap对象
	 * @param file
	 *            存储路径
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
	 * @param pathName
	 *            图片路径
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFromLocal(String pathName) {
		Bitmap bitmap = BitmapFactory.decodeFile(pathName);
		return bitmap;
	}

}
