package provider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.*;

/**
 * Created by максим on 02.01.2017.
 */
public class ImagesProvider  {

    private String directoryName = "images";
    private String fileName = "image.jpg";
    private Context context;
    private boolean external;
    public  static String filePath;

    public ImagesProvider(Context context){
        this.context=context;
    }

    public ImagesProvider setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
        return this;
    }

    public ImagesProvider setExternal(boolean external) {
        this.external = external;
        return this;
    }

    public ImagesProvider setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void saveBitmap(Bitmap bitmap){
        FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(createAFile());
            if(fileName.endsWith("png"))
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            if(fileName.endsWith("jpeg")||fileName.endsWith("jpg"))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos!=null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private File createAFile(){
        File dir =null;
        if(external) {

            dir = getAlbumStorageDir(directoryName);

        }else{
            dir  = context.getDir(directoryName,Context.MODE_PRIVATE);
            filePath=dir.getAbsolutePath();
        }
        return new File(dir,fileName);
    }

    private File getAlbumStorageDir(String albumName) {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),albumName);
        if(!file.mkdir()){
            Log.e("ImageProvider", "Directory not created");
        }
        return file;
    }

    public Bitmap load(){
        Bitmap bitmap =null;
        FileInputStream fis=null;
        try {
            fis = new FileInputStream(createAFile());      //???????????????????
            bitmap = BitmapFactory.decodeStream(fis);

            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
           if(fis!=null){
               try {
                   fis.close();
               } catch (IOException e) {}
           }
        }
        return null;
    }


}
