package com.example.DiabetApp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.DiabetApp.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by максим on 16.09.2016.
 */
public class ProductDataBase extends SQLiteOpenHelper {

    public Context fContext;
    private static final String DATABASE_NAME = "product_database.db";
    public static final String TABLE_NAME = "products_list";
    public static String TITLE="TITLE"; public static String AMOUNT="AMOUNT";

    public ProductDataBase(Context context){
        super(context,DATABASE_NAME,null,1);
        fContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + "_id INTEGER PRIMARY KEY, " + TITLE+" TEXT, " + AMOUNT+" TEXT"
                + ");");


        ContentValues values = new ContentValues();
        XmlResourceParser _xml = fContext.getResources().getXml(R.xml.product_records);
        try {
            // Ищем конец документа
            int eventType = _xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Ищем теги record
                if ((eventType == XmlPullParser.START_TAG)
                        && (_xml.getName().equals("record"))) {
                    // Тег Record найден, теперь получим его атрибуты и
                    // вставляем в таблицу
                    String title = _xml.getAttributeValue(0);
                    String amount = _xml.getAttributeValue(1);
                    values.put(TITLE, title);
                    values.put(AMOUNT, amount);
                    sqLiteDatabase.insert(TABLE_NAME, null, values);
                }
                eventType = _xml.next();
            }
        }catch (XmlPullParserException e) {
            Log.e("Test1", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("Test1", e.getMessage(), e);

        } finally {
            // Close the xml file
            _xml.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.w("TestBase", "Upgrading database from version " + i
                + " to " + i1 + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
