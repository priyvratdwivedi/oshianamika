package com.khansa.thedictionary;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class Databasehelper extends SQLiteOpenHelper {

    private String DB_PATH =null;
    private static String DB_NAME  ="eng_dictionary.db";
    private SQLiteDatabase mDatabase;
    private final Context mycontext;


    public Databasehelper(Context context){
        super(context, DB_NAME, null, 1);
        this.mycontext = context;

        this.DB_PATH ="/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("Path 1", DB_PATH);



    }

    public  void createDatabase()throws IOException{
        boolean dbExist = checkDatabase();
        if(!dbExist)
        {
            this.getReadableDatabase();
            try{
                mycontext.deleteDatabase(DB_NAME);

                copyDatabase();

            }
            catch(IOException e)
            {
                throw  new Error("Error copying database");
            }
        }
    }
    public boolean checkDatabase(){

        SQLiteDatabase checkdb = null;
        try {
            String mypath = DB_PATH+DB_NAME;
            checkdb = SQLiteDatabase.openDatabase(mypath,null,SQLiteDatabase.OPEN_READONLY);

        }
        catch(SQLiteException e)
        {


        }
        if(checkdb!=null)
        {
            checkdb.close();

        }
        return checkdb != null?true : false ;
    }

    private void copyDatabase() throws IOException{

        InputStream myInput = mycontext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH+DB_NAME;
        OutputStream myoutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length ;
        while ((length = myInput.read(buffer))>0)
        {
            myoutput.write(buffer,0,length);
        }
        myoutput.flush();
        myoutput.close();
        myInput.close();


    }

    public void openDatabase() throws SQLException{
        String myPath = DB_PATH+DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if(mDatabase!=null)
            mDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try
        {
            this.getReadableDatabase();
            mycontext.deleteDatabase(DB_NAME);
            copyDatabase();

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Cursor getmeaning(String text)

    {
        text =text.replaceAll("\\s+","");
        Cursor c= mDatabase.rawQuery("SELECT en_definition,example,synonyms,antonyms FROM words WHERE en_word==UPPER('"+text+"')"
                ,null);
        return c;
    }
    public Cursor getSuggestions(String text){
        text = text.replaceAll("\\s+","");
        Log.e("mm",text+"n");
        Cursor c = mDatabase.rawQuery("SELECT _id ,en_word FROM words WHERE en_word LIKE'"+text+"%' LIMIT 40",
                null);
        return c;
    }

    public void insertHistory(String text)
    {   text =text.replaceAll("\\s+","");
        mDatabase.execSQL("INSERT INTO history(word) values('"+text+"')");
    }
    public Cursor getHistory()
    {
        Cursor c= mDatabase.rawQuery("select distinct  word, en_definition from history h join words w on h.word==w.en_word order by h._id desc",null);
        return c;
    }
    public void deletehistory()
    {
        mDatabase.execSQL("DELETE FROM history");
    }

}
