package allezia.vote.app.android.alleziavote;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Abdullah on 20.11.2016.
 */
public class Database extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "vote";//database adı

    private static final String TABLE_NAME = "product";
    private static String ID = "id";
    public static String PROD_ID = "db_id";
    public static String IMAGE = "image";
    public static String OWNER = "owner";
    public static String DESCRIPTION = "description";
    public static String COIN = "coin";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PROD_ID + " INTEGER,"
                + IMAGE + " TEXT,"
                + OWNER + " TEXT,"
                + DESCRIPTION + " TEXT ," +
                COIN + " INTEGER" +")";
        db.execSQL(CREATE_TABLE);
    }

    public void productDelete(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PROD_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
}

    public void productAdd(String prodId, String imageUrl,String ownerName,String description,String coin) {
        //kitapEkle methodu ise adı üstünde Databese veri eklemek için
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROD_ID, prodId);
        values.put(IMAGE, imageUrl);
        values.put(OWNER, ownerName);
        values.put(DESCRIPTION, description);
        values.put(COIN, coin);

        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }

    public HashMap<String, String> getProduct(int id){
        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.
        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String,String> productSingle = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+" where "+PROD_ID + " = "+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();

        if(cursor.getCount() > 0){

            productSingle.put(IMAGE, cursor.getString(2));
            productSingle.put(OWNER, cursor.getString(3));
            productSingle.put(DESCRIPTION, cursor.getString(4));
            productSingle.put(COIN, cursor.getString(5));
        }
        cursor.close();
        db.close();

        return productSingle;
    }

    public ArrayList products(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+PROD_ID +" FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList productList = new ArrayList<>();


        if (cursor.moveToFirst()) {
            do {
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    productList.add(i, cursor.getInt(i));

                }

            } while (cursor.moveToNext());
        }
        db.close();
        // return kitap liste
        return productList;
    }
//
//    public void kitapDuzenle(String kitap_adi, String kitap_yazari,String kitap_basim_yili,String kitap_fiyat,int id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        //Bu methodda ise var olan veriyi güncelliyoruz(update)
//        ContentValues values = new ContentValues();
//        values.put(KITAP_ADI, kitap_adi);
//        values.put(KITAP_YAZARI, kitap_yazari);
//        values.put(KITAP_BASIM_YILI, kitap_basim_yili);
//        values.put(KITAP_FIYATI, kitap_fiyat);
//
//        // updating row
//        db.update(TABLE_NAME, values, KITAP_ID + " = ?",
//                new String[] { String.valueOf(id) });
//    }

    public int getRowCount() {
        // Bu method bu uygulamada kullanılmıyor ama her zaman lazım olabilir.Tablodaki row sayısını geri döner.
        //Login uygulamasında kullanacağız
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
    public void setCurrentCoin(Context context,int coin){
        SharedPreferences pref = context.getSharedPreferences("currentPref",0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.putInt("currentCoin",coin);
        editor.commit();

    }

    public int getCurrent(Context context){
        SharedPreferences pref = context.getSharedPreferences("currentPref",0);
        return pref.getInt("currentCoing",0);
    }

}