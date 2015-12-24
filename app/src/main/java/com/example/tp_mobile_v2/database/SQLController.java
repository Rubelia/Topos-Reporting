package com.example.tp_mobile_v2.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tp_mobile_v2.Object.Alarm;
import com.example.tp_mobile_v2.Object.Report;

import java.io.IOException;

public class SQLController {

//    private static String DB_PATH = "/data/data/com.example.laptrinhmobile.toposmobile/databases/";

//    private static String DB_NAME = "topos_reporting_mobile.db";
    private static String LOCATION = "SQLController";

    private DbHelper dbHelper;
    private Context myContext;
    private SQLiteDatabase database;

    public SQLController(Context c) {
        myContext = c;
    }

    public void instance() {
        dbHelper = new DbHelper(myContext);
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
            Log.d(LOCATION, "IOException: " + ioe.getMessage());
        }
    }
    public boolean isTableExist(String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void BeginTransaction() {
        database.beginTransaction();
    }
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
//        Log.d(LOCATION,"TransactionSuccessful");
    }

    public void endTransaction() {
        database.endTransaction();
    }
    public SQLController open() throws SQLException {
        dbHelper = new DbHelper(myContext);
        dbHelper.openDataBase();
        database  = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }

    //table user
    public int getIsSave(String user_name) {
        String sql = "SELECT * FROM User WHERE user_name = '"+ user_name +"'";
        Cursor get = database.rawQuery(sql,null);
        if(get != null) {
            if(get.moveToFirst()) return get.getInt(2);
            else return 0;
        } else return 0;
    }
    public String getUserNameIsSave() {
        String sql = "SELECT * FROM User WHERE isSave = '1'";
        Cursor get = database.rawQuery(sql,null);
        if(get != null)
        {
            if(get.moveToFirst()) return get.getString(1);
            else return "";
        } else return "";
    }
    public boolean checkUser(String user_name) {
        String sql = "SELECT * FROM User WHERE user_name = '" + user_name +"';";
        Cursor check = database.rawQuery(sql,null);
        if(check != null) {
            if(check.moveToFirst()) return true;
            else return false;
        } else return false;
    }
    public void insertUser(String user_name) {
        String sql = "INSERT INTO User (user_name, isSave) VALUES ('"+ user_name + "','1')";
        database.execSQL(sql);
    }
    public void updateUser(String user_name) {
        database.execSQL("UPDATE User SET isSave = '0'");
        database.execSQL("UPDATE User SET isSave = '1' WHERE user_name = '" + user_name + "';");
    }
    //table config
    public boolean checkIP() {
        Cursor check = database.rawQuery("SELECT * FROM Config",null);
        if(check != null) {
            if(check.moveToFirst())    return true;
            else return false;
        } else return false;
    }
    public void insertIP() {
//        database.delete()
        String query = "INSERT INTO Config(IP, KV, CH) VALUES ('192.168.200.16/MyService','KV001','AA');";
        database.execSQL(query);
    }
    public Cursor getIP() {
        Cursor get = database.rawQuery("SELECT * FROM Config",null);
        get.moveToFirst();
        return  get;
    }
    public String getStrIP() {
        Cursor get = database.rawQuery("SELECT IP FROM Config", null);
        get.moveToFirst();
        return get.getString(0);
    }
    public void updateIP(String IP) {
        String query = "UPDATE Config SET IP = '" + IP +"';";
        database.execSQL(query);
    }
    //table Dashboard
    public Cursor getAllInDashBoard() {
        String sql = "SELECT * FROM DashBoard ;";
        Cursor result = database.rawQuery(sql,null);
        return result;
    }
    public Cursor getReportIsShowInDashBoard() {
        String sql = "SELECT * FROM DashBoard WHERE isShow = '1';";
        Cursor result = database.rawQuery(sql,null);
        if(result != null) {
            result.moveToFirst();
            return  result;
        } else {
            Log.d(LOCATION,"GetDataInDashBoard: Không có dữ liệu");
            return null;
        }
    }
    public void insertIntoDashBoard(int index,int location, String isshow, String name_report, String report_Sql, Report report) {
        String sql1 = "INSERT INTO DashBoard VALUES('"+index+"','"+location+"','"+isshow+"','"+ name_report +
                "','"+report_Sql+"','"+report.getTab_Value()+"','"+ report.getTab_Des()+"')";
        database.execSQL(sql1);
    }
    public void updateDashBoard(String name,String id) {
        database.execSQL("UPDATE DashBoard SET name_report ='" + name + "' WHERE ID = '" + id + "' ");
    }
    public void deleteDashBoardWithLocation(int location) {
        database.execSQL("DELETE FROM DashBoard WHERE Location = '"+ location +"'");
    }
    public void deleteDashboard(String in_id) {
        database.execSQL("DELETE FROM DashBoard WHERE ID = '" + in_id + "';");
    }
    //table Alarm
    public Alarm getFromDatabase(String id) {
        Cursor get = database.rawQuery("SELECT * FROM Alarm WHERE Report_ID = '" + id + "';", null);
        if(get != null && get.moveToFirst()) return new Alarm(get);
        else return null;
    }
    public String getDates(String id) {
        Cursor get = database.rawQuery("SELECT Dates FROM Alarm WHERE Report_ID = '" + id + "';", null );
        if(get!= null &&get.moveToFirst()) return get.getString(0);
            else return "";
    }
    public void insertAlarm(Alarm alarm) {
        String sql = "INSERT INTO Alarm (Report_ID, Dates, Hours, Minutes, PhoneNumber) VALUES " +
                "('" + alarm.getReport_ID() + "', '" + alarm.getDates() +"', '" + alarm.getHours() + "', " +
                "'" + alarm.getMinutes()+ "', '" + alarm.getListPhoneToInsertIntoDatabase() +"');";
        Log.d(LOCATION,sql);
        database.execSQL(sql);
    }

}
