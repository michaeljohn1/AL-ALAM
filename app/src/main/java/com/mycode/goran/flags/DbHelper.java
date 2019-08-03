package com.mycode.goran.flags;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "MyDB.db";
    private static String DB_PATH = "";
    private SQLiteDatabase mDataBase;
    private Context mContext = null;

    DbHelper(Context context) {
        super(context, DB_NAME, null, 14);

        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        File file = new File(DB_PATH+"MyDB.db");
        if(file.exists())
            Open_DataBase(); // Add this line to fix db.insert can't insert values
        this.mContext = context;
    }

    private void Open_DataBase() {
        String DB_Path = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(DB_Path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void copy_DataBase() throws IOException {
        try {
            InputStream inputStream = mContext.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;

            OutputStream fileOutputStream = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0)
                fileOutputStream.write(buffer, 0, length);

            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean chec_kDataBase() {
        SQLiteDatabase tempDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (tempDB != null)
            tempDB.close();
        return tempDB != null;
    }

    public void createDataBase() throws IOException {
        boolean isDBExists = chec_kDataBase();
        if (isDBExists) {

        } else {
            this.getReadableDatabase();
            try {
                copy_DataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Titles");
        onCreate(db);
    }


    List<Question> Question_Mode(String mode) {
        List<Question> listQuestion = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        int limit = 0;
        if (mode.equals(style.MODE.Easy.toString()))
            limit = 36;
        else if (mode.equals(style.MODE.Medium.toString()))
            limit = 56;
        else if (mode.equals(style.MODE.Hard.toString()))
            limit = 112;
        else if (mode.equals(style.MODE.Hardest.toString()))
            limit = 208;
        try {
            c = db.rawQuery(String.format("SELECT * FROM Question ORDER BY Random() LIMIT %d", limit), null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                int Id = c.getInt(c.getColumnIndex("ID"));
                String Image = c.getString(c.getColumnIndex("Image"));
                String AnswerA = c.getString(c.getColumnIndex("AnswerA"));
                String AnswerB = c.getString(c.getColumnIndex("AnswerB"));
                String AnswerC = c.getString(c.getColumnIndex("AnswerC"));
//                String AnswerD = c.getString(c.getColumnIndex("AnswerD"));
                String CorrectAnswer = c.getString(c.getColumnIndex("CorrectAnswer"));

                Question question = new Question(Id, Image, AnswerA, AnswerB, AnswerC, CorrectAnswer);
                listQuestion.add(question);
            }
            while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return listQuestion;
    }


    void insert_Score(double score) {
        String query = "INSERT INTO Ranking(Score) VALUES("+score+")";
        mDataBase.execSQL(query);
    }


    List<Ranking> Ranking() {
        List<Ranking> listRanking = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM Ranking Order By Score DESC;", null);
            if (c == null) return null;
            c.moveToNext();
            do {
                int Id = c.getInt(c.getColumnIndex("Id"));
                double Score = c.getDouble(c.getColumnIndex("Score"));

                Ranking ranking = new Ranking(Id, Score);
                listRanking.add(ranking);
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listRanking;

    }


    //Update version 2.0
    int Receive_PlayCount(int level)
    {
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        try{
            c = db.rawQuery("SELECT PlayCount FROM UserPlayCount WHERE Level="+level+";",null);
            if(c == null) return 0;
            c.moveToNext();
            do{
                result  = c.getInt(c.getColumnIndex("PlayCount"));
            }while(c.moveToNext());
            c.close();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    void Update_PlayCount(int level, int playCount)
    {
        String query = String.format("UPDATE UserPlayCount Set PlayCount = %d WHERE Level = %d",playCount,level);
        mDataBase.execSQL(query);
    }
}
