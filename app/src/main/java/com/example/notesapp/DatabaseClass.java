package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
public class DatabaseClass extends SQLiteOpenHelper {
    Context context;
    public static final String DB_NAME ="MyNotes";
    public static final int DB_VERSION = 1;
    public static final String NOTE_TB_NAME ="notes";
    public static final String NOTE_CLN_ID ="id";
    public static final String NOTE_CLN_TITLE ="title";
    public static final String NOTE_CLN_DESCRIPTION ="description";


    public DatabaseClass(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query= "CREATE TABLE "+NOTE_TB_NAME+" ("+NOTE_CLN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                ""+NOTE_CLN_TITLE+" TEXT,"+NOTE_CLN_DESCRIPTION+" TEXT);";

        db.execSQL(query); //execute this query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+NOTE_TB_NAME);
        onCreate(db);
    }

    //دالة الاضافة
    void addNotes(String title,String description){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_CLN_TITLE,title);
        values.put(NOTE_CLN_DESCRIPTION,description);

        long result = db.insert(NOTE_TB_NAME,null,values);// if data not added >> return -1

        if(result == -1){
            Toast.makeText(context,"Data Not Added",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context,"Data Added Successfully",Toast.LENGTH_LONG).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + NOTE_TB_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;//Cursor = مؤشر
        if(db !=null ){
            cursor =  db.rawQuery(query,null);
        }
        return cursor;
    }

    void deleteAllNotes(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + NOTE_TB_NAME;
        db.execSQL(query);
    }

    void updateNotes(String title,String description,String id){
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values=new ContentValues();
       values.put(NOTE_CLN_TITLE,title);
       values.put(NOTE_CLN_DESCRIPTION,description);

       long result =db.update(NOTE_TB_NAME,values,"id=?",new String[]{id});
       if(result==-1)
       {
           Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
       }
       else
       {
           Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
       }

    }

    public void deleteSingleItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(NOTE_TB_NAME,"id=?",new String[]{id});
        if(result==-1)
        {
            Toast.makeText(context, "Item Not Deleted", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
