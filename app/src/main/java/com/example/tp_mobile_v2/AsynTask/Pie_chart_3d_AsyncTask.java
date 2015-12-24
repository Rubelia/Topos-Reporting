package com.example.tp_mobile_v2.AsynTask;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.tp_mobile_v2.Object.Item;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.ViewHolder;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

public class Pie_chart_3d_AsyncTask extends AsyncTask<String, Item, ArrayList<Item>> {
	//khai bÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡o Activity ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€ Ã¢â‚¬â„¢ lÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°u trÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â¯ ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¹a chÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â° cÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â§a MainActivity
//
	ArrayList<Exception> list_excep = new ArrayList<>();
	public String IP ;
	public Report report;
//	public final static int SHOW_IMG_PIE_CHART = 0;
	
	public static String urlGoogleChart 
	  = "http://chart.apis.google.com/chart";
	 public static String urlp3Api 
	  = "?cht=p3&chs=775x387";
	 public static String name_pie = "&chtt=";
	 public static String name_label = "&chl=";
	 public static String colors = "&chco=";
	 public static String values = "&chd=t:";
	 public static String name_legend = "&chdl=";
//	 public static String chart_style = "&chf=c,lg,90,FFE7C6,0,76A4FB,0.5";
			 
	 public String url ;
	 
	 
	ArrayList<Item> arrItem = new ArrayList<>();
	Item emp = new Item();
	
	long tam;
//	long total=0;
	Activity contextCha;
	//double result = 0.0;
	XmlPullParserFactory fc;
	XmlPullParser parser;
	public ViewHolder holder = new ViewHolder();
	int eventType;
	int i = 0;

	private static ProgressDialog pDialog;
	
	//constructor nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â£c truyÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¯Ã‚Â¿Ã‚Â½n vÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â o lÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â  MainActivity
	public Pie_chart_3d_AsyncTask (Activity ctx, Report reports, String inputIP, ViewHolder in_holder) {
		contextCha=ctx;		
//		adapter = new PieChartAdapter(contextCha,R.layout.type_pie_chart,arr_arrstr_para,url);	
		report = reports;
		IP = inputIP;
		holder = in_holder;
		pDialog = new ProgressDialog(ctx);
	}

	
	//hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y sÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â½ ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â£c thÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â±c hiÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡n ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â§u tiÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âªn
	@Override
	protected void onPreExecute() {
	// TODO 
		super.onPreExecute();
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setTitle("Đang lấy dữ liệu từ Server");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}
	//sau ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³ tÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Âºi hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m doInBackground
	//tuyÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡t ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“i khÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â´ng ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â£c cÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â­p nhÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â­t giao diÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡n trong hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y
	@Override
	protected ArrayList<Item> doInBackground(String...params) {
		name_pie += report.getName();
		String diachiWebService = "/tungSqlServerProxy.asmx?WSDL";
		String URL = "http://" + IP + diachiWebService;
//		String URL = "http://localhost" + diachiWebService;
//		String URL = "http://localhost/MyService/tungSqlServerProxy.asmx";
		final String NAMESPACE = "http://tempuri.org/";
		final String METHOD_NAME = "fSelectAndFillDataSet";
		final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
		final String script = report.getSql();
		final String table_des = report.getTab_Des();
		final String table_value = report.getTab_Value();		
		
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
			System.out.println(xml);

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
				Item tmp = new Item();
				eventType = parser.next();
				switch(eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					nodeName = parser.getName();
					if( nodeName.equalsIgnoreCase(table_des)) {
						emp.setName(parser.nextText());
					} else if(nodeName.equalsIgnoreCase(table_value)) {
						 String temp = parser.nextText();
						 emp.setValue(temp);
						 float a = Float.parseFloat(temp);
						 int b = (int) a;
						 String temp1 = String.valueOf(b);
						 System.out.println(temp1);
						 try {
							 tam = Long.parseLong(temp1.trim());
							 System.out.println("long l = " + tam);
						 } catch (NumberFormatException nfe) {
							 System.out.println("NumberFormatException: " + nfe.getMessage());
							 list_excep.add(nfe);
						 }
						 emp.setCost(tam);
						 publishProgress(emp);
						 emp = tmp;
					} else if( nodeName.equalsIgnoreCase("faultstring")) {
						throw new Exception("Câu SQL: " +script + "\n" + "Error: "+parser.nextText());
					} else  break;
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
	/**
	* ta cÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â­p nhÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â­p giao diÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡n trong hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y
	*/
	@Override
	protected void onProgressUpdate(Item... values) {
		// TODO Auto-generated method stub
		arrItem.add(values[0]);
				
		super.onProgressUpdate(values);
	}
	/**
	* sau khi tiÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â¿n trÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¬nh thÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â±c hiÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡n xong thÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¬ hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y sÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â£y ra
	*/
	@Override
	protected void onPostExecute(ArrayList<Item> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		for (Exception e : list_excep) {
		      // Do whatever you want for the exception here
			AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
			builder.setMessage(e.getMessage());
			AlertDialog alert = builder.create();
			alert.show();
		}
		name_pie = "&chtt=" + report.getName();
		name_label = "&chl=";
		colors = "&chco=";
		values = "&chd=t:";
		name_legend = "&chdl=";
		for(int i=0;i<arrItem.size();i++) {
			Random random = new Random();
			int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
			String hexColor = String.format("%06X", (0xFFFFFF & color));
			Log.d("hexadecimal: ", hexColor);
			if(i<arrItem.size()-1) {
				String temp = arrItem.get(i).getValue();
				if(temp.length() > 5) {
					temp = temp.substring(0, 2) + "." + temp.substring(2,4);
				}
				name_legend += arrItem.get(i).getName() + "|";
				name_label += arrItem.get(i).getValue() + "|";
				values +=  temp + ",";
				colors += hexColor +"|";
			} else {
				String temp = arrItem.get(i).getValue();
				if(temp.length() > 5) {
					temp = temp.substring(0, 2) + "." + temp.substring(2,4);
				}
				name_legend += arrItem.get(i).getName()  ;
				name_label += arrItem.get(i).getValue() ;
				values +=  temp ;
				colors += hexColor;
			}
		}
		url = urlGoogleChart + urlp3Api +name_label +name_legend +name_pie +"&chts=1E68AD,38,c"+"&chf=bg,s,EFEFEF"+ colors+ values +"&chdls=000000,14" + "&chma=0,0,0,0|80,20";

		url = changeName(url);
//		url = url.replace(" ", "+");
		Log.d("url: ",url );
		Picasso.with(contextCha.getApplicationContext()).load(url).into(holder.img_pie_chart);
//		ImageView img = (ImageView) contextCha.findViewById(R.id.list);
//		((ListView) img).setAdapter(adapter);
//		adapter.notifyDataSetChanged();
//		isFinish = true;

        pDialog.dismiss();
    }
	
	public String changeName(String in) {
		String result;
		result = in.replace("à", "a")    ;result = result.replace("á", "a");result = result.replace("ạ", "a");
		result = result.replace("ả", "a");result = result.replace("ã", "a");result = result.replace("â", "a");
		result = result.replace("ầ", "a");result = result.replace("ấ", "a");result = result.replace("ậ", "a");
		result = result.replace("ẩ", "a");result = result.replace("ẫ", "a");result = result.replace("ă", "a");
		result = result.replace("ằ", "a");result = result.replace("ắ", "a");result = result.replace("ặ", "a");
		result = result.replace("ẳ", "a");result = result.replace("ẵ", "a");
		
		result = result.replace("è", "e");result = result.replace("é", "e");result = result.replace("ẹ", "e");
		result = result.replace("ẻ", "e");result = result.replace("ẽ", "e");result = result.replace("ê", "e");
		result = result.replace("ề", "e");result = result.replace("ế", "e");result = result.replace("ệ", "e");
		result = result.replace("ể", "e");result = result.replace("ễ", "e");
//		result = in(“/(è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ)/”, ‘e’, in);
//		result = in(“/(ì|í|ị|ỉ|ĩ)/”, ‘i’, in);
		result = result.replace("ì", "i");result = result.replace("í", "i");result = result.replace("ị", "i");
		result = result.replace("ỉ", "i");result = result.replace("ĩ", "i");
//		result = in(“/(ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ)/”, ‘o’, in);
		result = result.replace("ò", "o");result = result.replace("ó", "o");result = result.replace("ọ", "o");
		result = result.replace("ỏ", "o");result = result.replace("õ", "o");result = result.replace("ô", "o");
		result = result.replace("ồ", "o");result = result.replace("ố", "o");result = result.replace("ộ", "o");
		result = result.replace("ổ", "o");result = result.replace("ỗ", "o");result = result.replace("ơ", "o");
		result = result.replace("ờ", "o");result = result.replace("ớ", "o");result = result.replace("ợ", "o");
		result = result.replace("ở", "o");result = result.replace("ỡ", "o");
//		result = in(“/(ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ)/”, ‘u’, in);
		result = result.replace("ù", "u");result = result.replace("ú", "u");result = result.replace("ụ", "u");
		result = result.replace("ủ", "u");result = result.replace("ũ", "u");result = result.replace("ư", "u");
		result = result.replace("ừ", "u");result = result.replace("ứ", "u");result = result.replace("ự", "u");
		result = result.replace("ử", "u");result = result.replace("ữ", "u");
//		result = in(“/(ỳ|ý|ỵ|ỷ|ỹ)/”, ‘y’, in);
		result = result.replace("ỳ", "y");result = result.replace("ý", "y");result = result.replace("ỵ", "y");
		result = result.replace("ỷ", "y");result = result.replace("ỹ", "y");result = result.replace("đ", "d");
		result = result.replace("Đ", "D");
		result = result.replace(" ", "+");
//		result = preg_replace(“/(À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ)/”, ‘A’, in);
//		result = preg_replace(“/(È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ)/”, ‘E’, in);
//		result = preg_replace(“/(Ì|Í|Ị|Ỉ|Ĩ)/”, ‘I’, in);
//		result = preg_replace(“/(Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ)/”, ‘O’, in);
//		result = preg_replace(“/(Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ)/”, ‘U’, in);
//		result = preg_replace(“/(Ỳ|Ý|Ỵ|Ỷ|Ỹ)/”, ‘Y’, in);
//		result = preg_replace(“/(Đ)/”, ‘D’, in);

		return result;
	}

	public String changeNumber(String in) {
		String result = in;
		String tmp = result.substring(result.length()-3, result.length());
		if(tmp.equalsIgnoreCase(".00")) {
			if(result.length() >= 7) {
				result = result.substring(0, 2) + "." +result.substring(2,4);
			} else if (result.length() == 6) {
				result = result.substring(0, 2) + "." +result.substring(2,3);
			} else {
				result = result.substring(0, 2);
			}
		} else {
			if(result.length() >= 4) {
				result = result.substring(0, 2) + "." +result.substring(2,4);
			} else if (result.length() == 3) {
				result = result.substring(0, 2) + "." +result.substring(2,3);
			} else {
				result = result.substring(0, 2);
			}
		}
		return result;
	}

}


