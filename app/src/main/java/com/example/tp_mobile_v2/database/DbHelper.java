package com.example.tp_mobile_v2.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by LapTrinhMobile on 9/28/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.tp_mobile_v2/databases/";
//
    private static String DB_NAME = "topos_reporting_mobile.db";
    private static String LOCATION = "DbHelper";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static DbHelper instance;

    public static synchronized DbHelper getHelper(Context context) {
        if (instance == null)
            instance = new DbHelper(context);
        return instance;
    }

    public DbHelper (Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void onCreate(SQLiteDatabase db){
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist) {
            //do nothing - database already exist
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        try {
            final String mPath = DB_PATH + DB_NAME;
            final File file = new File(mPath);
            return file.exists();
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        try {
            InputStream myInput = myContext.getAssets().open("topos_reporting_mobile.db");
            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0) {
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
            Log.d(LOCATION, "Copy database successful");
        } catch (Exception ex) {
//            Toast.makeText(myContext, "Exception in copydatabase: " + ex.toString(), Toast.LENGTH_LONG).show();
            Log.d(LOCATION, "Exception in copydatabase: " + ex.toString());
        }

    }
    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.d(LOCATION, "Open database successful");
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
        Log.d(LOCATION, "Close database successful");
    }
}
