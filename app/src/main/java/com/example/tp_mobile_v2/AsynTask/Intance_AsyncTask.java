package com.example.tp_mobile_v2.AsynTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_mobile_v2.Login;
import com.example.tp_mobile_v2.Object.NhanVien;
import com.example.tp_mobile_v2.R;
import com.example.tp_mobile_v2.database.SQLController;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by LapTrinhMobile on 11/13/2015.
 */
public class Intance_AsyncTask extends AsyncTask<String, String, String> {

//    private static final int SETTING_DIALOG_ID = 0;
//    private static final int IP_OLD_ID = 1;
//    private static final int IP_NEW_CLEAR = 2;

    private static String LOCATION = "Initialization_AsyncTask";
    private static ProgressDialog pDialog;
    private static Dialog changePassDialog;
    ArrayList<Exception> list_excep = new ArrayList<Exception>();
//    public String IP ;
    AlertDialog alertDialog;
    SQLController sqlController;
    Activity contextCha;
    NhanVien nhanVien;
    int type;
    String IP;

    XmlPullParserFactory fc;
    XmlPullParser parser;
    int eventType = 0;
    public Intance_AsyncTask(Activity ctx, NhanVien nv, int in_type) {
        contextCha = ctx;
        nhanVien = nv;
        type = in_type;
        pDialog = new ProgressDialog(ctx);
//        dialog = new Dialog(ctx);
        changePassDialog = new Dialog(ctx);
//        err_dialog = new Dialog(ctx);
    }

    @Override
    protected void onPreExecute() {
        // TODO
        super.onPreExecute();

        sqlController = new SQLController(contextCha);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("Đang kiểm tra kết nối với Webservice...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        alertDialog = new AlertDialog.Builder(contextCha).create();

    }
    @Override
    protected String doInBackground(String... params) {

        sqlController.open();
//        if(sqlController.checkIP()) {
            IP = sqlController.getStrIP();
//            IP = getIP.getString(0);
//        } else sqlController.insertIP();
        sqlController.close();

        final String NAMESPACE = "http://tempuri.org/";
        final String METHOD_NAME = "fSelectAndFillDataSet";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        String URL = "http://" + IP +"/tungSqlServerProxy.asmx";
//        Log.d(LOCATION, URL);
        String script = "SELECT TOP 1 MaNV, TenNV, TenDangNhap, MatKhau, MaCH, MaNhomNV FROM NhanVien ";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("query", script);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        MarshalFloat marshal = new MarshalFloat();
        marshal.register(envelope);

        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.debug = true;
            transport.call(SOAP_ACTION, envelope);
            String xml = transport.responseDump;
            Log.d(LOCATION, xml);
            try {
                fc = XmlPullParserFactory.newInstance();
                parser = fc.newPullParser();
                parser.setInput(new StringReader(xml));
                eventType = parser.getEventType();
            } catch (Exception e) {
                list_excep.add(e);
                e.printStackTrace();
            }
            String nodeName;
            while(eventType != XmlPullParser.END_DOCUMENT) {
                eventType = parser.next();
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        nodeName = parser.getName();
                        if( nodeName.equalsIgnoreCase("MaNV")) {
                            publishProgress("100");
////                            nhanVien.setMaNV(parser.nextText());
//                        } else if( nodeName.equalsIgnoreCase("TenNV")) {
//                            nhanVien.setTenNV(parser.nextText());
//                        } else if( nodeName.equalsIgnoreCase("MatKhau")) {
//                            nhanVien.setMatKhau(parser.nextText());
//                        } else if( nodeName.equalsIgnoreCase("MaCH")) {
//                            nhanVien.setMaCH(parser.nextText());
////                            publishProgress("100");
//                        } else if( nodeName.equalsIgnoreCase("MaNhomNV")) {
//                            nhanVien.setMaNhomNV(parser.nextText());
////                            publishProgress("100");
//                        } else if( nodeName.equalsIgnoreCase("faultstring")) {
//                            throw new Exception(parser.nextText());
                        } else {
                            break;
                        }
                    case XmlPullParser.END_TAG:
                        break;
                }
            }
        } catch (Exception e) {
//            if(type == 0) {
                publishProgress("101");
//            } else {
//                list_excep.add(e);
//                e.printStackTrace();
//            }
        }

        return null;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        // TODO Auto-generated method stub
        String condition = values[0];
//        if(type == 0) {
            if (condition.equalsIgnoreCase("101")) {
                pDialog.dismiss();
                CreateAlertDialog(alertDialog);
            } else if(condition.equalsIgnoreCase("100")) {
                Intent login_view = new Intent(contextCha,Login.class);
                Bundle b = new Bundle();
//                b.putSerializable("NhanVien",nhanVien);
                b.putString("IP",IP);
                login_view.putExtras(b);
                contextCha.startActivity(login_view);
                contextCha.finish();
            }
//        } else {
//
//        }
        super.onProgressUpdate(values);
    }
    /**
     * sau khi tiÃƒÂ¡Ã‚ÂºÃ‚Â¿n trÃƒÆ’Ã‚Â¬nh thÃƒÂ¡Ã‚Â»Ã‚Â±c hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n xong thÃƒÆ’Ã‚Â¬ hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y sÃƒÂ¡Ã‚ÂºÃ‚Â£y ra
     */
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        for (Exception e : list_excep) {
            // Do whatever you want for the exception here
            AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
            builder.setMessage(e.getMessage());
            AlertDialog alert = builder.create();
            alert.show();
        }
        pDialog.dismiss();

    }
    public void CreateAlertDialog(final AlertDialog in) {
        in.setTitle("Kết nối không thành công");
        in.setCanceledOnTouchOutside(false);
        in.setMessage("Vui lòng kiểm tra lại đường truyền hoặc kiểm tra IP");
        in.setButton(AlertDialog.BUTTON_NEUTRAL, "Thử lại", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intance_AsyncTask intance_asyncTask = new Intance_AsyncTask(contextCha,nhanVien,0);
                intance_asyncTask.execute();
                in.dismiss();
            }
        });
        in.setButton(AlertDialog.BUTTON_NEGATIVE, "Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                in.dismiss();
                contextCha.finish();
            }
        });
        in.setButton(AlertDialog.BUTTON_POSITIVE, "Thiết lập IP", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                createSettingDialog();
                changePassDialog.show();
            }
        });
        in.show();
    }

    private void createSettingDialog(){

        changePassDialog.setContentView(R.layout.setting_view);
        changePassDialog.setTitle("Thiết lập IP");
        changePassDialog.setCancelable(false);

        final Button ok = (Button) changePassDialog.findViewById(R.id.bt_ok);
        final Button cancel = (Button) changePassDialog.findViewById(R.id.bt_cancel);
        final EditText IPNew = (EditText) changePassDialog.findViewById(R.id.IPNew);

        final EditText ip_old = (EditText) changePassDialog.findViewById(R.id.IP_Old);

//        sqlController.open();
//        Cursor getIP = sqlController.getIP();
//        sqlController.close();
        ip_old.setText(IP);
//        ip_old.setId(IP_OLD_ID);
//        IPNew.setId(IP_NEW_CLEAR);
//
//        KV_New.setVisibility(true);
        ip_old.setFocusable(false);
//       kv_old.setFocusable(false);
//       ch_old.setFocusable(false);

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //xu ly su kien khi nhap dữ liệu vào :3
                String ip_new = IPNew.getText().toString();
//            	String kv_new = KV_New.getText().toString();
//            	String ch_new = CH_New.getText().toString();
                if (ip_new.length() != 0) {
//                    db.execSQL("UPDATE config SET IP ='"+ ip_new +"'");
                    sqlController.open();
                    sqlController.BeginTransaction();
                    try {
                        sqlController.updateIP(ip_new);
                        sqlController.setTransactionSuccessful();
                    } catch (SQLException err) {
                        err.getMessage();
                    }
                    sqlController.endTransaction();
                    sqlController.close();
                    Toast.makeText(contextCha, "Đã cập nhật xong IP", Toast.LENGTH_LONG).show();
                    changePassDialog.dismiss();
                    Intance_AsyncTask intance_asyncTask = new Intance_AsyncTask(contextCha,nhanVien,0);
                    intance_asyncTask.execute();
                } else {
                    String err = "Chưa điền IP mới";
                    ArrayList<String> arr_err = new ArrayList<String>();
                    arr_err.add(err);
                    createNoticeDialog(arr_err);
                }
//            	if (kv_new.length() != 0)
//            	{
//            		db.execSQL("UPDATE config SET KV ='"+ kv_new +"'");
//            		Toast.makeText(getApplicationContext(), "Đã cập nhật xong KV",Toast.LENGTH_LONG).show();
//                }
//            	if (ch_new.length() != 0)
//            	{
//            		db.execSQL("UPDATE config SET CH ='"+ ch_new +"'");
//            		Toast.makeText(getApplicationContext(), "Đã cập nhật xong CH",Toast.LENGTH_LONG).show();
//                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changePassDialog.dismiss();
            }
        });
//        return changePassDialog;
    }

    public void createNoticeDialog(ArrayList<String> str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
        String outStr = "";
        for(int i=0;i<str.size();i++) {
            outStr += str.get(i);
        }
        builder.setMessage(outStr);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
