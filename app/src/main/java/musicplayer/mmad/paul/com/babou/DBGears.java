package musicplayer.mmad.paul.com.babou;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

import java.lang.reflect.Array;


public class DBGears extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SongRestrictions.db";
    public static final String TABLE_NAME = "xsongpair";
    public static final String ID_COLUMN = "id";
    public static final String PRE_SONG_TITLE_COLUMN = "songa";
    //public static final String PRE_SONG_ARTIST_COLUMN = "artista";
    public static final String PRO_SONG_TITLE_COLUMN = "songb";
    //public static final String PRO_SONG_ARTIST_COLUMN = "artistb";
//"++"
    private String createTableString = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +
            "("+ID_COLUMN+" integer primary key AUTOINCREMENT," +
             PRE_SONG_TITLE_COLUMN+" text,"+
             PRO_SONG_TITLE_COLUMN+" text);";

    public DBGears(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBGears.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean banPairing(String songA, String songB){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PRE_SONG_TITLE_COLUMN, songA);
        //contentValues.put(PRE_SONG_ARTIST_COLUMN, artistA);
        contentValues.put(PRO_SONG_TITLE_COLUMN, songB);
       // contentValues.put(PRO_SONG_ARTIST_COLUMN, artistB);

        db.insert(TABLE_NAME, null, contentValues);
        /*Cursor c = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        c.moveToFirst();
        do {
            System.out.println(c.getString(c.getColumnIndex(ID_COLUMN))+" "+c.getString(c.getColumnIndex(PRE_SONG_TITLE_COLUMN))+ " " + c.getString(c.getColumnIndex(PRO_SONG_TITLE_COLUMN)));
            c.moveToNext();
        }
        while (c.isLast()==false);*/
        return true;

    }

    public boolean isBanned(String songA, String songB){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM "+ TABLE_NAME+" WHERE "+
                PRE_SONG_TITLE_COLUMN+"='"+songA+"' AND "+
                PRO_SONG_TITLE_COLUMN+"='"+songB+"'", null);
        if(c.getCount()!=0){
            db.close();
            return true;
        }else { db.close();return false;}
    }

    public String[][] bannedPairs(){
        SQLiteDatabase db = this.getReadableDatabase();
        String s = "SELECT * FROM "+ TABLE_NAME+"";

        Cursor c =  db.rawQuery(s, null);
        c.moveToFirst();
        String[][] newArr = new String[c.getCount()][3];
        for (int i = 0; i<c.getCount();i++){
            System.out.println(i);
            String ii = (String)c.getString(c.getColumnIndex(ID_COLUMN));
            String ss = (String)c.getString(c.getColumnIndex(PRE_SONG_TITLE_COLUMN));
            System.out.println(ss);
            String sss = (String)c.getString(c.getColumnIndex(PRO_SONG_TITLE_COLUMN));
            System.out.println(sss);
            newArr[i][0] = ii;
            newArr[i][1] = ss.toString();
            newArr[i][2] = sss.toString();
            c.moveToNext();
        }


        db.close();
        return newArr;
    }
}
