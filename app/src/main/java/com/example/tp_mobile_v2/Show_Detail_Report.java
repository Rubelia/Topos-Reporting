package com.example.tp_mobile_v2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.tp_mobile_v2.AsynTask.Detail_Report_AsynTask;
import com.example.tp_mobile_v2.Object.Item;
import com.example.tp_mobile_v2.Object.Report;
import com.example.tp_mobile_v2.database.SQLController;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Show_Detail_Report extends Activity {

	private final static String LOCATION = "Show_Detail_Report";

	static final int SETTING_REPORT_DASHBROAD = 0;
	static final int INT_ALERTDIALOG = 11;
	public final static int INT_RADIOG1 = 1;
	public final static int INT_RADIOG2 = 2;
//	public final static int INT_G1RADIO0 = 3;
//	public final static int INT_G1RADIO1 = 4;
//	public final static int INT_G2RADIO0 = 5;
//	public final static int INT_G2RADIO1 = 6;
//	public final static int INT_G2RADIO2 = 7;
//	public final static int INT_G2RADIO3 = 8;
	
	public final static int TEXT_NAME_REPORT_INT = 10;

	ArrayList<Exception> list_excep = new ArrayList<>();
	ArrayList<Item> arrItem = new ArrayList<>();
	ArrayList<String> arr_err = new ArrayList<>();
	
	String IP = "";
	String namePDF ;
	String pathPDF ;
	String isshow ;
	String in_alertdialog;
	int location = 0;
//	int pos;
	boolean isSavePDF = false;
	boolean sure = false;
	Report report;
	Report old_report;
	Detail_Report_AsynTask myAsynTask;
	Calendar c;
	
	BaseFont urName;
	Font times;
	
	SQLController sqlController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show__detail__report);
		sqlController = new SQLController(getApplicationContext());
		isshow = "2";
		try {
			urName = BaseFont.createFont("assets/VTIMESN.TTF", "UTF-8",BaseFont.CACHED);
			times = new Font(urName, 12);
		} catch (DocumentException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		report = new Report();
		Bundle extras = getIntent().getExtras();
		if(extras !=null) {
			IP = extras.getString("IP");
			report = (Report) extras.getSerializable("info_report");
			old_report = (Report) extras.getSerializable("old_report");
		}
//		Window w = getWindow();
//		setTitle(report.getName());
		show_detail_report();
//		ActionBar tabsActionBar = get
//		tabsActionBar.setDisplayShowTitleEnabled(false);
//		tabsActionBar.setDisplayShowHomeEnabled(false);
//	
//		LayoutInflater mInflater = LayoutInflater.from(this);
//
//		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		TextView mTitleTextView = (TextView) findViewById(R.id.txt_title);
		mTitleTextView.setText(report.getName());
		mTitleTextView.setTextSize(14);

		final ImageButton imageButton = (ImageButton) findViewById(R.id.btnSetting);
		imageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
//				showDialog(SETTING_REPORT_DASHBROAD);
				PopupMenu popup = new PopupMenu(Show_Detail_Report.this,imageButton);
				//Inflating the Popup using xml file
				popup.getMenuInflater()
						.inflate(R.menu.menu, popup.getMenu());

				//registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
//                        arr_err = new ArrayList<>();
						switch (item.getItemId()) {
							case R.id.menu_save:
								SaveListViewToPdf();
								return true;
							case R.id.menu_share:
								try {
									CreatNewFileInSdCard();
								} catch (DocumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Toast.makeText(getApplicationContext(), "Đang tạo...", Toast.LENGTH_LONG).show();
								if(isSavePDF) {
									sharePDF();
									isSavePDF = false;
								} else {
									AlertDialog.Builder builder = new AlertDialog.Builder(Show_Detail_Report.this);
									builder.setMessage("Không tạo được file PDF để share");
									AlertDialog alert = builder.create();
									alert.show();
								} return true;
							case R.id.menu_like:
								showDialog(SETTING_REPORT_DASHBROAD);
							case R.id.menu_auto:
//								createSetAlarmDialog();

								Intent view_setAlarm = new Intent(getApplicationContext(),SetAlarmToRunReport.class);
								Bundle b = new Bundle();
								b.putString("IP",IP);
								b.putSerializable("Report",report);
								b.putSerializable("old_report", old_report);
								view_setAlarm.putExtras(b);
								startActivity(view_setAlarm);
								return true;
							default:
								return true;
//								return super.onOptionsItemSelected(item);
						}
					}
				});
				popup.show();
			}
		});

//		tabsActionBar.setCustomView(mCustomView);
//		tabsActionBar.setDisplayShowCustomEnabled(true);
		
		Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnRefresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				show_detail_report();
			}
		});

		
		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		for (Exception e : list_excep) {
		      // Do whatever you want for the exception here
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(e.getMessage());
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	public void show_detail_report() {
		myAsynTask = new Detail_Report_AsynTask(this, report, IP);
		myAsynTask.execute();
		arrItem = myAsynTask.arrItem;
	}
	public String CreateFolder() {
    	String path = "";
    	File folder = new File(Environment.getExternalStorageDirectory() + "/PDF");
    	System.out.println(Environment.getExternalStorageDirectory());
    	boolean success = true;
    	if (!folder.exists()) {
    	    success = folder.mkdir();
    	}
    	if (success) {
    	    // Do something on success
    		path = Environment.getExternalStorageDirectory() + "/PDF" ;
//    		Toast.makeText(getApplicationContext(), "Created folder success", Toast.LENGTH_LONG).show();
    	} else {
    	    // Do something else on failure 
    		System.out.println("Tạo thư mục mới false");
    	}
		return path;
    }
    public void SaveListViewToPdf() {
		try {
			CreatNewFileInSdCard();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void CreatNewFileInSdCard() throws DocumentException {
//		File folder = new File(Environment.getExternalStorageDirectory()
//			    + File.separator + "com.example.topos_mobile");
//			folder.mkdirs();
		namePDF = "";
		pathPDF = "";
		c = Calendar.getInstance();
		int day  = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		if(c.get(Calendar.MONTH) < 12) {
			month += 1;
		} else {
			month = 1;
			year += 1;
		}
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minutes = c.get(Calendar.MINUTE);
		int seconds = c.get(Calendar.SECOND);
		String date = "" + day+"-" + month +"-" + year +":"+ hour +"h" + minutes+"m" + seconds +"s";
		String nameFile = "BC"  + date;
		namePDF = nameFile;
		System.out.println(nameFile);
		String path = CreateFolder();
//		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
		pathPDF = path;
		writeDataToPDF(path, nameFile);
	}
//	public  void openPdf() {
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
//
//		File file = new File(path, namePDF +".pdf");
//		intent.setDataAndType(Uri.fromFile(file),"application/pdf");
//		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//		Intent show = Intent.createChooser(intent, "Mở file PDF");
//		try {
//		    startActivity(show);
//		} catch (ActivityNotFoundException e) {
//		    // Instruct the user to install a PDF reader here, or something
//			e.printStackTrace();
//		}
//	}
	public void writeDataToPDF(String path, String nameFile) {
		File file;
		Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter docWriter = null;
		
		BaseFont bf;
		Font font12 ;
		Font fontBold12 ;
//		Font fontBold16 = new Font();
		Font fontBold20 ;
		
//		Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
//		Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12); 
		try {
//			String link = getAssets()+"VTIMESN.TFF";
//			BaseFont bf = BaseFont.createFont("VTIMESN.TFF",BaseFont.CP1250,BaseFont.EMBEDDED);
//			Font a = new Font
			
//			font = new Font(bf,12);
			bf = BaseFont.createFont("/assets/TIMES.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		    font12 = new Font(bf,12);
		    fontBold12 = new Font(bf,12,Font.BOLD);
//		    fontBold16 = new Font(bf,14,Font.BOLD);
		    fontBold20 = new Font(bf,20,Font.BOLD);
		    
			File dir = new File(path);
			if(!dir.exists())
				dir.mkdirs();
//			Log.d("PDFCreator", "PDF Path: " + path);
		    file = new File(dir, nameFile+ ".pdf");
		    FileOutputStream fOut = new FileOutputStream(file);

		    docWriter = PdfWriter.getInstance(doc, fOut);

		    doc.open();
		    
		    //tao mới paragraph p1
		    Paragraph p1 = new Paragraph(report.getName(),fontBold20);
//		    System.out.println(report.getName());
            p1.setAlignment(Paragraph.ALIGN_CENTER);
//            p1.setFont(fontBold16);
            
//            doc.add(p1);
            
            //specify column widths
            
            float[] columnWidths = {2f,0.5f};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(90f);
            
            //insert column heading
            
        	insertCell(table,report.getTab_Des(),com.itextpdf.text.Element.ALIGN_CENTER, 1, fontBold12);
        	insertCell(table,report.getTab_Value(),com.itextpdf.text.Element.ALIGN_CENTER, 1, fontBold12);
        	table.setHeaderRows(1);
        	
        	//insert empty row
        	
        	insertCell(table,"",com.itextpdf.text.Element.ALIGN_LEFT,2,font12);
        
        	for(int i=0;i<arrItem.size();i++)
        	{
        		Item temp = arrItem.get(i);
        		String name_temp = temp.getName();
//        		String values_temp = new String'
//        		values_temp = temp.getValue();
        		String number = temp.getValue();
    		    double amount = Double.parseDouble(number);
    			DecimalFormat formatter = new DecimalFormat("#,###");
    		    String formatted = formatter.format(amount);
    		    
        		insertCell(table,name_temp,com.itextpdf.text.Element.ALIGN_LEFT,1,font12);
        		insertCell(table,formatted,com.itextpdf.text.Element.ALIGN_RIGHT,1,font12);
//        		System.out.println(name_temp + values_temp);
        	}
//            }
        	p1.add(table);
        	doc.add(p1);
           isSavePDF = true;
    
            //set footer
//            Phrase footerText = new Phrase("This is an example of a footer");
//            HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
//            doc.setFooter(pdfFooter);
//            Header pdfHeader = new Header(footerText, false);	s
       
//            Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
		} catch (DocumentException de){
			Log.e("PDFCreater","Document Exception:" + de);
		} catch (IOException e) {
		    // handle exception
			e.printStackTrace();
		} finally {
			//close the document
			doc.close();
			if (docWriter != null){
			    //close the writer
			    docWriter.close();
			}
		}
	}
	private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){
		   
		  //create a new cell with the specified Text and Font
		  
		  Phrase temp = new Phrase(text.trim(),font);
		  PdfPCell cell = new PdfPCell(temp);
		  
//		  System.out.println("Test write into PDF: " + temp.toString());
		  //set the cell alignment
		  cell.setHorizontalAlignment(align);
		  //set the cell column span in case you want to merge two or more cells
		  cell.setColspan(colspan);
		  //in case there is no text and you wan to create an empty row
		  if(text.trim().equalsIgnoreCase("")) {
		   cell.setMinimumHeight(10f);
		  }
		  //add the call to the table
		  table.addCell(cell);
		 }
	public void sharePDF() {
		try {
			File fileUri = new File(Environment.getExternalStorageDirectory() +"/PDF" ,namePDF +".pdf");
//			Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() +"/PDF" ,namePDF));
			System.out.println("Path fileUri: "+fileUri.getAbsolutePath());
			
			Uri uri = Uri.fromFile(fileUri);
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
	        sharingIntent.setType("application/pdf*");
	        
//	        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	        sharingIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"xuan.thang0512@gmail.com"} );
	        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Gửi file Báo cáo PDF");
	        Log.v(getClass().getSimpleName(), "Uri=" + uri.toString());
	        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
	//        System.out.println("Tên file: " + namePDF);
	//        System.out.println("tên path: " + pathPDF);
	        startActivity(Intent.createChooser(sharingIntent, "Share via"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void onPrepareDialog(int id, Dialog dialog) {
		 
        switch (id) {
	        case SETTING_REPORT_DASHBROAD:
	        	EditText name_report = (EditText) dialog.findViewById(TEXT_NAME_REPORT_INT);
	        	name_report.setHint(report.getName());
//	        	RadioGroup radio1 = (RadioGroup) dialog.findViewById(INT_RADIOG1);
//	        	RadioGroup radio2 = (RadioGroup) dialog.findViewById(INT_RADIOG2);
//	        	RadioButton test1 = (RadioButton) radio1.findViewById(INT_G1RADIO1);
////	        	test1.setChecked(true);
//	        	RadioButton test = (RadioButton) radio1.findViewById(INT_G1RADIO0);
////	        	test.setChecked(false);
//    			radio2.findViewById(INT_G2RADIO0).setEnabled(false);
//    			radio2.findViewById(INT_G2RADIO1).setEnabled(false);
//    			radio2.findViewById(INT_G2RADIO2).setEnabled(false);
            	break;
            default:
            	break;
        }
    }

	@Override
    protected Dialog onCreateDialog(int id){
    	switch(id){
    	case SETTING_REPORT_DASHBROAD:
    		return createSettingReportDashbroad(report);
    	case INT_ALERTDIALOG:
    		return createAlertDialog(in_alertdialog);
		default:
			return null;
    	}
    }
	private Dialog createAlertDialog(String in_string) {
		sure = false;
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.alertdialog_view);
		dialog.setCancelable(false);
		final Button ok = (Button) dialog.findViewById(R.id.bt_ok);
//        final Button cancel = (Button) dialog.findViewById(R.id.bt_cancel);
        final TextView txt_arr_notice = (TextView) dialog.findViewById(R.id.txt_arr_notice);
        txt_arr_notice.setText(in_string);
        txt_arr_notice.setTextSize(14);
        ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sure = true;
				dialog.dismiss();
			}
		});
//        cancel.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//			}
//		});
		return dialog;
	}
	private Dialog createSettingReportDashbroad(Report in_report){
    	final Dialog dialog = new Dialog(this);
//    	final Report tmp = in_report;
    	dialog.setContentView(R.layout.setting_report_dashbroad_view);
    	dialog.setTitle("Tùy chỉnh báo cáo:");
    	dialog.setCancelable(false);
    	
    	
    	final RadioGroup radioG1 = (RadioGroup) dialog.findViewById(R.id.radioGroup1);
    	final RadioGroup radioG2 = (RadioGroup) dialog.findViewById(R.id.radioGroup2);
    	final EditText name_report = (EditText) dialog.findViewById(R.id.edit_name_report);
    	name_report.setId(TEXT_NAME_REPORT_INT);
    	radioG1.setId(INT_RADIOG1);
    	radioG2.setId(INT_RADIOG2);
    	
//    	radioG1.findViewById(R.id.G1radio0).setId(INT_G1RADIO0);
//    	radioG1.findViewById(R.id.G1radio1).setId(INT_G1RADIO1);
//    	radioG2.findViewById(R.id.G2radio0).setId(INT_G2RADIO0);
//    	radioG2.findViewById(R.id.G2radio1).setId(INT_G2RADIO1);
    	radioG2.findViewById(R.id.G2radio0).setEnabled(false);
    	radioG2.findViewById(R.id.G2radio1).setEnabled(false);
    	radioG2.findViewById(R.id.G2radio2).setEnabled(false);
    	radioG2.findViewById(R.id.G2radio3).setEnabled(false);
    	
    	final Button ok = (Button) dialog.findViewById(R.id.btnSave);
        final Button cancel = (Button) dialog.findViewById(R.id.btnExit);
        radioG1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
//				if(checkedId == INT_G1RADIO0)
				if(checkedId == R.id.G1radio0) {
					isshow = "1";
//					radioG2.findViewById(INT_G2RADIO0).setEnabled(true);
//					radioG2.findViewById(INT_G2RADIO1).setEnabled(true);
//					radioG2.findViewById(INT_G2RADIO2).setEnabled(true);
					if( report.getTab_Des().isEmpty()) {
						radioG2.findViewById(R.id.G2radio1).setEnabled(true);
						radioG2.findViewById(R.id.G2radio2).setEnabled(true);
						radioG2.findViewById(R.id.G2radio3).setEnabled(true);
					} else {
						radioG2.findViewById(R.id.G2radio0).setEnabled(true);
						radioG2.findViewById(R.id.G2radio1).setEnabled(true);
					}
				} else if(checkedId == R.id.G1radio1) {
					isshow = "0";
//					radioG2.findViewById(INT_G2RADIO0).setEnabled(false);
//					radioG2.findViewById(INT_G2RADIO1).setEnabled(false);
//					radioG2.findViewById(INT_G2RADIO2).setEnabled(false);
					
					radioG2.findViewById(R.id.G2radio0).setEnabled(false);
					radioG2.findViewById(R.id.G2radio1).setEnabled(false);
					radioG2.findViewById(R.id.G2radio2).setEnabled(false);
					radioG2.findViewById(R.id.G2radio3).setEnabled(false);
				} else {
					isshow = "2";
				}
			}
		});
        radioG2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
//				case INT_G2RADIO0:
				case R.id.G2radio0:
					location = 1;
					break;
//				case INT_G2RADIO1:
				case R.id.G2radio1:
					location = 2;
					break;
//				case INT_G2RADIO2:
				case R.id.G2radio2:
					location = 3;
					break;
				case R.id.G2radio3:
					location = 4;
					break;
				default:
					break;
				}
			}
		} );	
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { 
            	arr_err = new ArrayList<>();
//            	Log.d("IsShow: ",isshow);
//            	radioGroup1 = (RadioGroup) dialog.findViewById(R.id.radioGroup1);
//            	RadioGroup radioGroup2 = (RadioGroup) dialog.findViewById(R.id.radioGroup2);
            	if(isshow.equalsIgnoreCase("2")) {
//            		Log.d("Here: ","554");
//            		String err = new String();
            		in_alertdialog = "Bạn chưa thực hiện thao tác nào hết.Vui lòng chọn giá trị!";
//            		createAlertDialog(in_alertdialog);
            		showDialog(INT_ALERTDIALOG);
            	} else if (isshow.equalsIgnoreCase("0")) {
            		dialog.dismiss();
            	} else if (name_report.getText().toString().isEmpty()) {
            		String err = "Chưa điền tên báo cáo";
            		arr_err.add(err);
            		showNotice(arr_err);
            	} else if (location == 0 ) {
            		String err = "Chưa chọn vị trí hiển thị";
            		arr_err.add(err);
            		showNotice(arr_err);
            	} else {
//                	db = openOrCreateDatabase("DBConfig", Show_Detail_Report.MODE_PRIVATE, null);
					sqlController.open();
					sqlController.BeginTransaction();
					if (location == 3) {
//                		db.execSQL("DELETE FROM DASHBOARD WHERE Location ='3'");
						try {
							sqlController.deleteDashBoardWithLocation(3);
							sqlController.setTransactionSuccessful();
						} catch (SQLException ex) {
							arr_err = new ArrayList<>();
							arr_err.add(ex.getMessage());
							showNotice(arr_err);
						}
                	} else if (location == 4) {
//                		db.execSQL("DELETE FROM DASHBOARD WHERE Location ='4'");
						try {
							sqlController.deleteDashBoardWithLocation(4);
							sqlController.setTransactionSuccessful();
						} catch (SQLException ex) {
							arr_err = new ArrayList<>();
							arr_err.add(ex.getMessage());
							showNotice(arr_err);
						}
                	}

//            		Cursor check = db.rawQuery("SELECT * FROM DashBoard",null);
					Cursor check = sqlController.getAllInDashBoard();
            		String 	report_Sql = report.getSql();
            		report_Sql = report_Sql.replace("'", "''");
            		
            		if(!check.moveToFirst()) {
						try {
							sqlController.deleteDashBoardWithLocation(4);
							sqlController.insertIntoDashBoard(1,location,isshow,name_report.getText().toString(),report_Sql,report);
							sqlController.setTransactionSuccessful();
						} catch (SQLException ex) {
							arr_err = new ArrayList<>();
							arr_err.add(ex.getMessage());
							showNotice(arr_err);
						}
//            			String sql1 = "INSERT INTO DashBoard VALUES('1','"+location+"','"+isshow+"','"+name_report.getText().toString()+
//                        			"','"+report_Sql+"','"+report.getTab_Value()+"','"+ report.getTab_Des()+"')";
//                    	db.execSQL(sql1);
                    	Toast.makeText(getApplicationContext(), "Đã lưu thành công", Toast.LENGTH_SHORT).show();
                    } else {
            			int index = check.getCount() +1;
//            			String sql ;
//            			sql = "INSERT INTO DashBoard VALUES('" +index +"','"+location+"','"+isshow+"','"+name_report.getText().toString()+
//                    			"','"+report_Sql+"','"+report.getTab_Value()+"','"+ report.getTab_Des()+"')";
//            			db.execSQL(sql);
						try {
							sqlController.insertIntoDashBoard(index,location,isshow,name_report.getText().toString(),report_Sql,report);
							sqlController.setTransactionSuccessful();
						} catch (SQLException err) {
							arr_err = new ArrayList<>();
							arr_err.add(err.getMessage());
							showNotice(arr_err);
						}
						sqlController.endTransaction();
						sqlController.close();
            			Toast.makeText(getApplicationContext(), "Đã lưu thành công", Toast.LENGTH_SHORT).show();
            		}
            		isshow = "2";
//            		db.close();
            		dialog.dismiss();
            	}
              }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    	return dialog;
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
//	    	MenuInflater menuInflater = getMenuInflater();
//	        menuInflater.inflate(R.menu.menu, menu);
	        getMenuInflater().inflate(R.menu.menu, menu);
//	        menu.add(0,MENU_SETTING,0,)
	        return true;
	    }
	 
    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
//	        int id = item.getItemId();
//	        if (id == R.id.action_settings) {
//	            return true;
//	        }
	        
	        switch (item.getItemId()) {
	        case R.id.menu_save:
	        	SaveListViewToPdf();
	        	return true;
	        case R.id.menu_share:
	        	try {
					CreatNewFileInSdCard();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//	        	System.out.println("")
	        	Toast.makeText(getApplicationContext(), "Đang tạo...", Toast.LENGTH_LONG).show();
	        	if(isSavePDF) {
	        		sharePDF();
	        		isSavePDF = false;
	        	} else {
	        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    			builder.setMessage("Không tạo được file PDF để share");
	    			AlertDialog alert = builder.create();
	    			alert.show();
	        	} return true;
	        case R.id.menu_like:
	        	showDialog(SETTING_REPORT_DASHBROAD);
				case R.id.menu_auto:
					createSetAlarmDialog();
					return true;
	        default:
	        	return super.onOptionsItemSelected(item);
	        }
	        
	    }
    public void showNotice (ArrayList<String> str) {
		String outStr = "";
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		for(int i=0;i<str.size();i++) {
			outStr += str.get(i);
		}
		builder.setMessage(outStr);
		AlertDialog alert = builder.create();
		alert.show();
	}

    private void openChart(){
    	int[] x = new int[arrItem.size()];
    	// Creating a dataset to hold each series
    	  	
    	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
    	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    	for(int i=0;i<arrItem.size();i++)
    	{
    		x[i] = i;
    		XYSeries temp = new XYSeries(arrItem.get(i).getName());
    		temp.add(i, arrItem.get(i).getValues());
    		dataset.addSeries(temp);
    		XYSeriesRenderer tempRenderer = new XYSeriesRenderer();
    		Random random = new Random();
    		int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    		tempRenderer.setColor(color);
    		tempRenderer.setFillPoints(true);
//    		tempRenderer.setLineWidth(200);
    		tempRenderer.setDisplayChartValues(true);
    		tempRenderer.setChartValuesTextAlign(Align.CENTER);
        	
    		multiRenderer.addSeriesRenderer(tempRenderer);
    	}
//    	Random random = new Random();
//		int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
		
		multiRenderer.setXLabelsColor(Color.rgb(222	, 203, 145));
		multiRenderer.setYLabelsColor(0, Color.rgb(222	, 203, 145));
		multiRenderer.setAxesColor(Color.rgb(222	, 203, 145));
    	multiRenderer.setMarginsColor(Color.rgb(52, 110, 153));
    	multiRenderer.setBackgroundColor(Color.LTGRAY);
    	multiRenderer.setXLabels(0);
    	multiRenderer.setChartTitle(report.getName());
    	multiRenderer.setXTitle(report.getTab_Des());
    	multiRenderer.setYTitle(report.getTab_Value());
    	multiRenderer.setZoomButtonsVisible(true);    	    	
    	for(int i=0; i< x.length;i++){
    		multiRenderer.addXTextLabel(i, ""+x[i]);    		
    	}    	
    	
//    	multiRenderer.addSeriesRenderer(incomeRenderer);
//    	multiRenderer.addSeriesRenderer(expenseRenderer);
    		
		// Creating an intent to plot bar chart using dataset and multipleRenderer    	
    	Intent intent = ChartFactory.getBarChartIntent(getBaseContext(), dataset, multiRenderer, Type.DEFAULT);
    	
    	// Start Activity
    	startActivity(intent);
    }

	private void createSetAlarmDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.set_alarm);
		dialog.setTitle("Cài giờ chạy báo cáo");
		dialog.setCancelable(true);

		final TextView txtTime = (TextView) dialog.findViewById(R.id.txtTime);
//		final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
//		txtTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());

		Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				txtTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
			}
		});
		dialog.show();
	}
}
