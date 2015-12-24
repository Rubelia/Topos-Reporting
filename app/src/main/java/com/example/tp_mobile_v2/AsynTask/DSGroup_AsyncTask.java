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

import com.example.tp_mobile_v2.ArrayAdapter.DSGroup_ArrayAdapter;
import com.example.tp_mobile_v2.Object.Group;
import com.example.tp_mobile_v2.R;

public class DSGroup_AsyncTask extends AsyncTask<String, Group, ArrayList<Group>> {
	ArrayList<Exception> list_excep = new ArrayList<>();
	public String IP ;
	public ArrayList<Group> arrGroup = new ArrayList<>();
	DSGroup_ArrayAdapter adapter = null;
	Group emp = new Group();
	Activity contextCha;
	XmlPullParserFactory fc;
	XmlPullParser parser;
	int eventType;
	int i = 0;
	private static ProgressDialog pDialog;
	//constructor nÃƒÆ’Ã‚Â y Ãƒâ€žÃ¢â‚¬ËœÃƒâ€ Ã‚Â°ÃƒÂ¡Ã‚Â»Ã‚Â£c truyÃƒÂ¡Ã‚Â»Ã¯Â¿Â½n vÃƒÆ’Ã‚Â o lÃƒÆ’Ã‚Â  MainActivity
	public DSGroup_AsyncTask(Activity ctx, String inputIP) {
		contextCha=ctx;		
		adapter = new DSGroup_ArrayAdapter(contextCha, R.layout.list_groups_layout, arrGroup);
		pDialog = new ProgressDialog(ctx);
		IP = inputIP;
	}

	//hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y sÃƒÂ¡Ã‚ÂºÃ‚Â½ Ãƒâ€žÃ¢â‚¬ËœÃƒâ€ Ã‚Â°ÃƒÂ¡Ã‚Â»Ã‚Â£c thÃƒÂ¡Ã‚Â»Ã‚Â±c hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚ÂºÃ‚Â§u tiÃƒÆ’Ã‚Âªn
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
	//sau Ãƒâ€žÃ¢â‚¬ËœÃƒÆ’Ã‚Â³ tÃƒÂ¡Ã‚Â»Ã¢â‚¬Âºi hÃƒÆ’Ã‚Â m doInBackground
	//tuyÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡t Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Ëœi khÃƒÆ’Ã‚Â´ng Ãƒâ€žÃ¢â‚¬ËœÃƒâ€ Ã‚Â°ÃƒÂ¡Ã‚Â»Ã‚Â£c cÃƒÂ¡Ã‚ÂºÃ‚Â­p nhÃƒÂ¡Ã‚ÂºÃ‚Â­t giao diÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n trong hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y
	@Override
	protected ArrayList<Group> doInBackground(String...params) {
	

		String diachiWebService = "/tungSqlServerProxy.asmx?WSDL";
		String URL = "http://" + IP + diachiWebService;
//		String URL = "http://localhost" + diachiWebService;
//		String URL = "http://localhost/MyService/tungSqlServerProxy.asmx";
		final String NAMESPACE = "http://tempuri.org/";
		final String METHOD_NAME = "fSelectAndFillDataSet";
		final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
		
		String script = "Select * from [Group]";
		

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
//			System.out.println(xml);

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
				Group tmp = new Group();
				eventType = parser.next();
				switch(eventType) {
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
						publishProgress(emp);
						emp = tmp;
					} else if( nodeName.equalsIgnoreCase("faultstring")) {
						throw new Exception(parser.nextText());
					} else break;
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
	* ta cÃƒÂ¡Ã‚ÂºÃ‚Â­p nhÃƒÂ¡Ã‚ÂºÃ‚Â­p giao diÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n trong hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y
	*/
	@Override
	protected void onProgressUpdate(Group... values) {
		// TODO Auto-generated method stub
		
		arrGroup.add(values[0]);
		System.out.println(arrGroup.size());
				
		ListView lv1 = (ListView) contextCha.findViewById(R.id.ds_group);
		lv1.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		super.onProgressUpdate(values);
	}
	/**
	* sau khi tiÃƒÂ¡Ã‚ÂºÃ‚Â¿n trÃƒÆ’Ã‚Â¬nh thÃƒÂ¡Ã‚Â»Ã‚Â±c hiÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡n xong thÃƒÆ’Ã‚Â¬ hÃƒÆ’Ã‚Â m nÃƒÆ’Ã‚Â y sÃƒÂ¡Ã‚ÂºÃ‚Â£y ra
	*/
	@Override
	protected void onPostExecute(ArrayList<Group> result) {
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