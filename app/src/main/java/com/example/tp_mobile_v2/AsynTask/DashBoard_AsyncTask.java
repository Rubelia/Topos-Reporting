package com.example.tp_mobile_v2.AsynTask;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.tp_mobile_v2.Object.Item;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.R;


public class DashBoard_AsyncTask extends AsyncTask<String, Item, Item> {
	//khai bÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡o Activity ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€ Ã¢â‚¬â„¢ lÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°u trÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â¯ ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¹a chÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â° cÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â§a MainActivity
	
	
	ArrayList<Exception> list_excep = new ArrayList<>();
	public String IP ;
	public Report report;
//	public String name_report = new String();
//	public String script = new String();
//	public static String urlGoogleChart
//	  = "http://chart.apis.google.com/chart";
//	 public static String urlp3Api 
//	  = "?cht=p3&chs=775x387";
//	 public static String name_pie = "&chtt=";
//	 public static String name_label = "&chl=";
//	 public static String colors = "&chco=";
//	 public static String values = "&chd=t:";
//	 public static String name_legend = "&chdl=";
//	 public static String chart_style = "&chf=c,lg,90,FFE7C6,0,76A4FB,0.5";
//	 public String url ;
//	ArrayList<Item> arrItem = new ArrayList<>();
//	MyArrayAdapter adapter = null;
	Item emp = new Item();
	
	long tam;
//	long total=0;
	Activity contextCha;
	//double result = 0.0;
	XmlPullParserFactory fc;
	XmlPullParser parser;
	int eventType;
	int i ;
	
	//constructor nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â£c truyÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¯Ã‚Â¿Ã‚Â½n vÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â o lÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â  MainActivity
	public DashBoard_AsyncTask(Activity ctx, String inputIP, Report report)
	{		
		this.contextCha=ctx;
//		adapter = new MyArrayAdapter(contextCha,R.layout.my_item_layout, arrItem);	
		this.report = report;
//		this.name_report = name_report;
//		this.q
		this.IP = inputIP;
//		this.arrKho = arrKho;
	}

	
	//hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y sÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â½ ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â£c thÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â±c hiÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡n ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â§u tiÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âªn
	@Override
	protected void onPreExecute() {
	// TODO 
		super.onPreExecute();
//		Toast.makeText(contextCha, "Đang lấy dữ liệu. Vui lòng đợi giây lát!...",
//		Toast.LENGTH_LONG).show();
	}
	//sau ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³ tÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Âºi hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m doInBackground
	//tuyÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡t ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“i khÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â´ng ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â£c cÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â­p nhÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â­t giao diÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡n trong hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y
	@Override
	protected Item doInBackground(String...params) {
	

//		name_pie += report.getName();
		String diachiWebService = "/tungSqlServerProxy.asmx?WSDL";
		String URL = "http://" + IP + diachiWebService;
//		String URL = "http://localhost" + diachiWebService;
//		String URL = "http://localhost/MyService/tungSqlServerProxy.asmx";
		final String NAMESPACE = "http://tempuri.org/";
		final String METHOD_NAME = "fSelectAndFillDataSet";
		final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
		final String script = report.getSql();
//		final String table_des = report.getTab_Des();
		final String table_value = report.getTab_Value();		
//		final String script = "";

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
//			Log.d("XML",xml);
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
//				Item tmp = new Item();
				eventType = parser.next();
				switch(eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					nodeName = parser.getName();
					if(nodeName.equalsIgnoreCase(table_value)) {
						String temp = parser.nextText();
						emp.setValue(temp);
						float a = Float.parseFloat(temp);
						int b = (int) a;
						String temp1 = String.valueOf(b);
//						System.out.println(temp1);
						 try {
					         tam = Long.parseLong(temp1.trim());
					         System.out.println("long l = " + tam);
					      } catch (NumberFormatException nfe) {
					         System.out.println("NumberFormatException: " + nfe.getMessage());
					         list_excep.add(nfe);
					      }
						emp.setCost(tam);
						publishProgress(emp);
					} else if( nodeName.equalsIgnoreCase("faultstring")) {
							{
								throw new Exception("Câu SQL: " +script + "\n" + "Error: "+parser.nextText());
							}
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
//		TextView txtResult = (TextView) contextCha.findViewById(R.id.txtTimeUpdate);
//		txtResult.setText(values[0]);
//		ListView txtList = (ListView) contextCha.findViewById(R.id.lvItem);
//		
//		arrItem.add(values[0]);		
		super.onProgressUpdate(values);
		if(report.getLocation() == 3) {
			TextView txtName = (TextView) contextCha.findViewById(R.id.txtTenReport);
			txtName.setText(report.getName());
			TextView txtDoanhThu = (TextView) contextCha.findViewById(R.id.txtDoanhThu);
			
			long amount = 0;
			amount += values[0].toCost();
			DecimalFormat formatter = new DecimalFormat("#,###");
		    String formatted = formatter.format(amount);
		    txtDoanhThu.setText(formatted);
		} else {
			TextView txtName = (TextView) contextCha.findViewById(R.id.txt_Report);
			txtName.setText(report.getName());
			TextView txtSLKH = (TextView) contextCha.findViewById(R.id.txtSLKH);
			txtSLKH.setText(values[0].getValue());
		}
	}
	/**
	* sau khi tiÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â¿n trÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¬nh thÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â±c hiÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡n xong thÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¬ hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y sÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â£y ra
	*/
	@Override
	protected void onPostExecute(Item result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		for (Exception e : list_excep) {
		      // Do whatever you want for the exception here
			AlertDialog.Builder builder = new AlertDialog.Builder(contextCha);
			builder.setMessage(e.getMessage());
			AlertDialog alert = builder.create();
			alert.show();
		}
//		Log.d("ASDASDAD","ASDADSADADA");
//		TextView txt_textsif(
		
		
//		Toast.makeText(contextCha, "Đã lấy dữ liệu xong!",
//		Toast.LENGTH_SHORT).show();
	}
	
}