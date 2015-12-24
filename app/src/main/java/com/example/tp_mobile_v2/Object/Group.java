package com.example.tp_mobile_v2.Object;

public class Group { 
//	implements Parcelable  {
		private String name;
		private String id;
//		private ArrayList<Report> arrReport ;
		
		public Group() {
			name = "";
			id = "";
//			arrReport = new ArrayList<Report>();
		}
		/*
		// get arraylist 
		public ArrayList<Report> getArrayReport() {
			return arrReport;
		}
		// add Report to arrReport
		public void addReportToGroup (Report temp) {
			arrReport.add(temp);
		}
		*/
		public void setName (String name){
			this.name = name;
		}
		public void setId (String id) {
			this.id = id;
		}
		public String getName(){
			return name;
		}
		public String getId(){
			return id;
		}

//		@Override
//		public int describeContents() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public void writeToParcel(Parcel dest, int flags) {
//			// TODO Auto-generated method stub
//			dest.writeString(name);
//			Bundle b = new Bundle();
//			b.putParcelableArrayList("Report", arrReport);
//			dest.writeBundle(b);
//			
//		}
//		
//		public static final Parcelable.Creator<Group> CREATOR = 
//				new Parcelable.Creator<Group>() { 
//			public Group createFromParcel(Parcel in) { 
//				Group group = new Group();
//				group.name = in.readString();
//				Bundle b = in.readBundle(Report.class.getClassLoader());        
//				group.arrReport = b.getParcelableArrayList("Report");
//
//				return group;
//		}
//		
//		@Override
//		public Category[] newArray(int size) {
//		return new Category[size];
//		}
		
}


