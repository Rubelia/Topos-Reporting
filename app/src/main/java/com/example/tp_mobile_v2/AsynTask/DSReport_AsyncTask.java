package com.example.tp_mobile_v2.AsynTask;

import java.io.StringReader;
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
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.tp_mobile_v2.ArrayAdapter.DSReport_ArrayAdapter;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.R;

public class DSReport_AsyncTask extends AsyncTask<String, Report, ArrayList<Report>> {
	//khai bÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡o Activity ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€ Ã¢â‚¬â„¢ lÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°u trÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â¯ ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¹a chÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â° cÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â§a MainActivity
	
	
	ArrayList<Exception> list_excep = new ArrayList<>();
	public String IP ;
//	public Report report;
	
	
	public ArrayList<Report> arrReport = new ArrayList<>();
	DSReport_ArrayAdapter adapter = null;
//	Item emp = new Item();
	
	Report emp = new Report();
	String id_group = "";
	Activity contextCha;
	//double result = 0.0;
	XmlPullParserFactory fc;
	XmlPullParser parser;
	int eventType;
	int i = 0;

	private static ProgressDialog pDialog;
	//constructor nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y ÃƒÆ’Ã¢â‚¬Å¾ÃƒÂ¢Ã¢â€šÂ¬Ã‹Å“ÃƒÆ’Ã¢â‚¬Â Ãƒâ€šÃ‚Â°ÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â£c truyÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¯Ã‚Â¿Ã‚Â½n vÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â o lÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â  MainActivity
	public DSReport_AsyncTask(Activity ctx, String inputIP, String input_id_group)
	{		
		contextCha=ctx;		
		adapter = new DSReport_ArrayAdapter(contextCha, R.layout.reports_list_layout, arrReport);
//		this.arrReport = arrReport;
		IP = inputIP;
		id_group = input_id_group;
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
	protected ArrayList<Report> doInBackground(String...params) {
	

		String diachiWebService = "/tungSqlServerProxy.asmx?WSDL";
		String URL = "http://" + IP + diachiWebService;
//		String URL = "http://localhost" + diachiWebService;
//		String URL = "http://localhost/MyService/tungSqlServerProxy.asmx";
		final String NAMESPACE = "http://tempuri.org/";
		final String METHOD_NAME = "fSelectAndFillDataSet";
		final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
		
		String script = "Select * from [Report] where Id_Group = '" + id_group + "'";
//		String script = "Select * from [Parameter] where MaBC = 'BC01BH01'" ;

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("query", script);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		
		MarshalFloat marshal = new MarshalFloat();
		marshal.register(envelope);
		
		HttpTransportSE transport = new HttpTransportSE(URL);
		System.out.print(URL);
		try {
			transport.debug = true;
			transport.call(SOAP_ACTION, envelope);
			
			String xml = transport.responseDump;
			System.out.println(xml);

			try{
				fc = XmlPullParserFactory.newInstance();
				parser = fc.newPullParser();
				parser.setInput(new StringReader(xml));
				eventType = parser.getEventType();
			} catch (Exception e)
			{
				list_excep.add(e);
				e.printStackTrace();
			}
		
			String nodeName;
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				Report tmp = new Report();
				eventType = parser.next();
				switch(eventType)
				{
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					nodeName = parser.getName();
					if( nodeName.equalsIgnoreCase("Id")) {
						emp.setId(parser.nextText());
					} else if(nodeName.equalsIgnoreCase("Ten")) {
							emp.setName(parser.nextText());
					} else if (nodeName.equalsIgnoreCase("SQL")) {
						emp.setSql(parser.nextText());
					} else if (nodeName.equalsIgnoreCase("DienGiai")) {
						emp.setTab_Des(parser.nextText());
					} else if (nodeName.equalsIgnoreCase("GiaTri")) {
						emp.setTab_Value(parser.nextText());
					} else if (nodeName.equalsIgnoreCase("MaBC")) {
						emp.setMaBC(parser.nextText());
					} else if (nodeName.equalsIgnoreCase("Type_Sum")) {
						emp.setType_sum(parser.nextText());
						publishProgress(emp);
						emp = tmp;
					} else break;
				case XmlPullParser.END_TAG:
					
					break;
				}
			}

		}
		catch (Exception e)
		{
			list_excep.add(e);
			e.printStackTrace();
		}
		
		
		return null;
	}
	/**
	* ta cÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â­p nhÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â­p giao diÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡n trong hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y
	*/
	@Override
	protected void onProgressUpdate(Report... values) {
		// TODO Auto-generated method stub
		
		arrReport.add(values[0]);		
				
		ListView lv1 = (ListView) contextCha.findViewById(R.id.list_report);
		lv1.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		super.onProgressUpdate(values);
		
		
	}
	/**
	* sau khi tiÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â¿n trÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¬nh thÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»Ãƒâ€šÃ‚Â±c hiÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚Â»ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¡n xong thÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¬ hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â m nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â y sÃƒÆ’Ã‚Â¡Ãƒâ€šÃ‚ÂºÃƒâ€šÃ‚Â£y ra
	*/
	@Override
	protected void onPostExecute(ArrayList<Report> result) {
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
	
}