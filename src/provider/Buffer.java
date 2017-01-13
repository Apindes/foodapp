package provider;

import android.graphics.Bitmap;

import java.util.HashMap;

public class Buffer{

    static HashMap<String,Bitmap> pictures_map = new HashMap();

    public Buffer(String pictName, Bitmap bitmap){
       if(!pictures_map.containsKey(pictName)){
           pictures_map.put(pictName, bitmap);
       }
    }


    public static HashMap<String, Bitmap> getPictures_map() {
        return pictures_map;
    }
}
