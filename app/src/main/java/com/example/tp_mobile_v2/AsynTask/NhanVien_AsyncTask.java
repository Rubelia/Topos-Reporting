package com.example.tp_mobile_v2.AsynTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tp_mobile_v2.DashBoard;
import com.example.tp_mobile_v2.Object.NhanVien;
import com.example.tp_mobile_v2.Other.Rfc2898DeriveBytes;
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
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by LapTrinhMobile on 11/13/2015.
 */
public class NhanVien_AsyncTask extends AsyncTask<String, String, String> {

    private static String LOCATION = "NhanVien_AsyncTask";

    private static final byte[] salt = new byte[]
            {
                    0x26, (byte) 0xdc, (byte) 0xff, 0x00, (byte) 0xad, (byte) 0xed, 0x7a, (byte) 0xee, (byte) 0xc5,
                    (byte) 0xfe, 0x07, (byte) 0xaf, 0x4d, 0x08, 0x22, 0x3c
            };

    private static final int PIN_LENGTH = 6;
    private static ProgressDialog pDialog;
    private static Dialog enterPassword;
    private static AlertDialog err_dialog;
    ArrayList<Exception> list_excep = new ArrayList<Exception>();
    SQLController sqlController;
    Activity contextCha;
    ArrayList<String> arr_password ;
    public NhanVien nhanVien;
    String TenDangNhap ;
    String IP;
    int count;
    int isSave;
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDelete, btnExit;
    TextView pinBox0, pinBox1, pinBox2, pinBox3, pinBox4, pinBox5, statusMessage;

    XmlPullParserFactory fc;
    XmlPullParser parser;
    int eventType = 0;
    public NhanVien_AsyncTask(Activity ctx, String in_TenDangNhap, String in_IP, int in_isSave) {
        contextCha = ctx;
        TenDangNhap = in_TenDangNhap;
        nhanVien = new NhanVien();
        IP = in_IP;
        isSave = in_isSave;
        sqlController = new SQLController(ctx);
        pDialog = new ProgressDialog(ctx);
        enterPassword = new Dialog(ctx);
        arr_password = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        // TODO
        super.onPreExecute();

//        sqlController = new SQLController(contextCha);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("Đang kết nối với Webservice...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        enterPassword.setTitle("Nhập mật khẩu");
//        enterPassword.setContentView(R.layout.enter_password_view);

    }
    @Override
    protected String doInBackground(String... params) {
        final String NAMESPACE = "http://tempuri.org/";
        final String METHOD_NAME = "fSelectAndFillDataSet";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        String URL = "http://" + IP +"/tungSqlServerProxy.asmx";
        String script = "SELECT MaNV, TenNV, TenDangNhap, MatKhau, MaCH, MaNhomNV FROM NhanVien WHERE TenDangNhap = '" + TenDangNhap + "';";
//        Log.d(LOCATION, script);
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
//            Log.d(LOCATION, xml);
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
                            nhanVien.setMaNV(parser.nextText());
                        } else if( nodeName.equalsIgnoreCase("TenNV")) {
                            nhanVien.setTenNV(parser.nextText());
                        } else if( nodeName.equalsIgnoreCase("MatKhau")) {
                            nhanVien.setMatKhau(parser.nextText());
                            byte[] decode = Base64.decode(nhanVien.getMatKhau(), 1);
                            Log.d(LOCATION,"From server");
                            Log.d(LOCATION, Arrays.toString(decode));
                            SecretKeyFactory factory;
                            SecretKeyFactory factory1;
                            try {
                                factory = SecretKeyFactory.getInstance("PBKDF2WithHMACSHA1");
//                                factory1 = SecretKeyFactory.getInstance("PBKDF2WithMD5");
                                PBEKeySpec pbeKeySpec = new PBEKeySpec("thinhphat@jsc".toCharArray(), salt, 1000, 384);
//                                PBEKeySpec pbeKeySpec1 = new PBEKeySpec()
                                Key secretKey = factory.generateSecret(pbeKeySpec);
//                                Key test = factory.
                                byte[] key = new byte[32];
                                byte[] iv = new byte[16];

                                System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
                                System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);
                                Log.d(LOCATION, "From pass with iv");
                                Log.d(LOCATION, Arrays.toString(iv));
                                Log.d(LOCATION,"String with MD5");
                                String a = md5("0A-38-C8-38-E6-0C-C1-1E-C4-C9-C9-1A-C8-8A-31-2D");
                                Log.d(LOCATION,a);
                            } catch (NoSuchAlgorithmException err) {
                                Log.d(LOCATION,"NoSuchAlgorithmException: " + err.getMessage());
                            }
                        } else if( nodeName.equalsIgnoreCase("MaCH")) {
                            nhanVien.setMaCH(parser.nextText());
                            publishProgress("100");
//                        } else if( nodeName.equalsIgnoreCase("MaNhomNV")) {
//                            nhanVien.setMaNhomNV(parser.nextText());
//                            publishProgress("100");
                        } else if( nodeName.equalsIgnoreCase("faultstring")) {
                            throw new Exception(parser.nextText());
                        } else {
//                            publishProgress("104");
                            break;
                        }
                    case XmlPullParser.END_TAG:
                        break;
                }
            }
        } catch (Exception e) {
            list_excep.add(e);
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        // TODO Auto-generated method stub
        String condition = values[0];
        if(condition.equalsIgnoreCase("100")) {
            showEnterPasswordDialog(R.layout.enter_password_view);
//            enterPassword.show();
        }
        super.onProgressUpdate(values);
    }

    private void showEnterPasswordDialog(int layoutID) {
        count = 1;
        LayoutInflater inflater = LayoutInflater.from(contextCha);
        View v = inflater.inflate(layoutID, null, false);
        enterPassword.setContentView(v);
        enterPassword.setCancelable(false);
        btn0 = (Button) v.findViewById(R.id.btn0);btn1 = (Button) v.findViewById(R.id.btn1);btn2 = (Button) v.findViewById(R.id.btn2);
        btn3 = (Button) v.findViewById(R.id.btn3);btn4 = (Button) v.findViewById(R.id.btn4);btn5 = (Button) v.findViewById(R.id.btn5);
        btn6 = (Button) v.findViewById(R.id.btn6);btn7 = (Button) v.findViewById(R.id.btn7);btn8 = (Button) v.findViewById(R.id.btn8);
        btn9 = (Button) v.findViewById(R.id.btn9);btnDelete = (Button) v.findViewById(R.id.btnDelete);
        btnExit = (Button) v.findViewById(R.id.btnExit);
        pinBox0 = (TextView) v.findViewById(R.id.pinBox0);pinBox1 = (TextView) v.findViewById(R.id.pinBox1);
        pinBox2 = (TextView) v.findViewById(R.id.pinBox2);pinBox3 = (TextView) v.findViewById(R.id.pinBox3);
        pinBox4 = (TextView) v.findViewById(R.id.pinBox4);pinBox5 = (TextView) v.findViewById(R.id.pinBox5);
        statusMessage = (TextView) v.findViewById(R.id.statusMessage);
        nhanVien.setMatKhau("123456");
        View.OnClickListener clickBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId() /*to get clicked view id**/) {
                    case R.id.btn0:
//                        Log.d(LOCATION,"0");
                        if(arr_password.isEmpty()) {
                            arr_password.add("0");
                        } else {
                            if(arr_password.size() == 6) {
                                checkPassword(arr_password,nhanVien.getMatKhau(),v);
                            } else {
                                arr_password.add("0");
                                setText(count);
                                count += 1;
                                if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);

                            }
                        }
                        break;
                    case R.id.btn1:
//                        Log.d(LOCATION,"1");
                        if(arr_password.size() == 6) {
                            checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        } else {
                            arr_password.add("1");
                            setText(count);
                            count += 1;
                            if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        }
                        break;
                    case R.id.btn2:
//                        Log.d(LOCATION,"2");
                        if(arr_password.size() == 6) {
                            checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        } else {
                            arr_password.add("2");
                            setText(count);
                            count += 1;
                            if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        }
                        break;
                    case R.id.btn3:
//                        Log.d(LOCATION,"3");
                        if(arr_password.size() == 6) {
                            checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        } else {
                            arr_password.add("3");
                            setText(count);
                            count += 1;
                            if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        }
                        break;
                    case R.id.btn4:
//                        Log.d(LOCATION,"4");
                        if(arr_password.size() == 6) {
                            checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        } else {
                            arr_password.add("4");
                            setText(count);
                            count += 1;
                            if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        }
                        break;
                    case R.id.btn5:
//                        Log.d(LOCATION,"5");
                        if(arr_password.size() == 6) {
                            checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        } else {
                            arr_password.add("5");
                            setText(count);
                            count += 1;
                            if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        }
                        break;
                    case R.id.btn6:
//                        Log.d(LOCATION,"6");
                        if(arr_password.size() == 6) {
                            checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        } else {
                            arr_password.add("6");
                            setText(count);
                            count += 1;
                            if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        }
                        break;
                    case R.id.btn7:
//                        Log.d(LOCATION,"7");
                        if(arr_password.size() == 6) {
                            checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        } else {
                            arr_password.add("7");
                            setText(count);
                            count += 1;
                            if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        }
                        break;
                    case R.id.btn8:
//                        Log.d(LOCATION,"8");
                        if(arr_password.size() == 6) {
                            checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        } else {
                            arr_password.add("8");
                            setText(count);
                            count += 1;
                            if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        }
                        break;
                    case R.id.btn9:
//                        Log.d(LOCATION,"9");
                        if(arr_password.size() == 6) {
                            checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        } else {
                            arr_password.add("9");
                            setText(count);
                            count += 1;
                            if(arr_password.size() == 6) checkPassword(arr_password,nhanVien.getMatKhau(),v);
                        }
                        break;
                    case R.id.btnDelete:
//                        Log.d(LOCATION,"Delete");
                        if(arr_password.isEmpty()) {
                            //don't do anything
                        } else {
                            count --;
                            delText(count);
                            arr_password.remove(arr_password.size()-1);
                        }
                        statusMessage.setText("");

                        break;
                    case R.id.btnExit:
//                        Log.d(LOCATION,"Exit");
                        enterPassword.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };
        btn0.setOnClickListener(clickBtn);btn1.setOnClickListener(clickBtn);btn2.setOnClickListener(clickBtn);
        btn3.setOnClickListener(clickBtn);btn4.setOnClickListener(clickBtn);btn5.setOnClickListener(clickBtn);
        btn6.setOnClickListener(clickBtn);btn7.setOnClickListener(clickBtn);btn8.setOnClickListener(clickBtn);
        btn9.setOnClickListener(clickBtn);btnDelete.setOnClickListener(clickBtn);btnExit.setOnClickListener(clickBtn);

        enterPassword.show();
    }

    private void setText(int count) {
        switch (count) {
            case 1:
                pinBox0.setText("*");
                break;
            case 2:
                pinBox1.setText("*");
                break;
            case 3:
                pinBox2.setText("*");
                break;
            case 4:
                pinBox3.setText("*");
                break;
            case 5:
                pinBox4.setText("*");
                break;
            case 6:
                pinBox5.setText("*");
                break;
            default:
                break;
        }
    }
    private void delText(int count) {
        switch (count) {
            case 1:
                pinBox0.setText("");
                break;
            case 2:
                pinBox1.setText("");
                break;
            case 3:
                pinBox2.setText("");
                break;
            case 4:
                pinBox3.setText("");
                break;
            case 5:
                pinBox4.setText("");
                break;
            case 6:
                pinBox5.setText("");
                break;
            default:
                break;
        }
    }

    private void checkPassword(ArrayList<String> arr_password, String password, View v)  {
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] salt = new byte[]
                {
                        0x26, (byte) 0xdc, (byte) 0xff, 0x00, (byte) 0xad, (byte) 0xed, 0x7a, (byte) 0xee, (byte) 0xc5, (byte) 0xfe, 0x07,
                        (byte) 0xaf, 0x4d, 0x08, 0x22, 0x3c
                };
        PBEKeySpec pbeKeySpec;
//        char[] temp = password.toCharArray();
        pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, 1000, 384);
        Key secretKey = null;
        try {
            secretKey = factory.generateSecret(pbeKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        byte[] key = new byte[32];
        byte[] iv = new byte[16];
        System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
        System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);
        Log.d(LOCATION, "Run here");
        Log.d(LOCATION, String.valueOf(key));
        Log.d(LOCATION, String.valueOf(iv));
        String arrPass = "";
        for(int i=0;i<arr_password.size();i++) {
            arrPass += arr_password.get(i);
        }
        if(arrPass.equalsIgnoreCase(password)) {
            if(isSave == 1) {
                sqlController.open();
//                sqlController.BeginTransaction();
                if(sqlController.checkUser(TenDangNhap)) sqlController.updateUser(TenDangNhap);
                else sqlController.insertUser(TenDangNhap);
                Log.d(LOCATION, "Here");
//                sqlController.endTransaction();
                sqlController.close();
            }
            enterPassword.dismiss();
            Intent dsgroup = new Intent (contextCha,DashBoard.class);
            dsgroup.putExtra("inputUserName", nhanVien.getTenNV());
            dsgroup.putExtra("IP",IP);
            contextCha.startActivity(dsgroup);
//            contextCha.finish();
        }
        else {
            statusMessage.setText("Mật khẩu không chính xác");
        }
    }

    /**
     * sau khi tiÃƒÂ¡Ã‚ÂºÃ‚Â¿n trÃƒÆ’Ã‚Â¬nh thÃƒÂ¡Ã‚Â»Ã‚Â±c hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n xong thÃƒÆ’Ã‚Â¬ hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y sÃƒÂ¡Ã‚ÂºÃ‚Â£y ra
     */
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if(nhanVien.getMaNV().isEmpty()) {
            String outStr = "Tên đăng nhập không tồn tại";
            AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
            builder.setMessage(outStr);
            AlertDialog alert = builder.create();
            alert.show();
        }
        for (Exception e : list_excep) {
            // Do whatever you want for the exception here
            AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
            builder.setMessage(e.getMessage());
            AlertDialog alert = builder.create();
            alert.show();
        }
        pDialog.dismiss();
    }

    private static final String md5(final String password) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
//            Log.d(LOCATION,Arrays.toString(messageDigest));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
