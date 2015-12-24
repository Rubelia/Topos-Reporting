package com.example.tp_mobile_v2.Object;
import java.util.ArrayList;

public class Item {
	private String name;
	private String value;
	private float cost = 0;
	private ArrayList<Float> listcost ;
	private Group group;
//	private int color;
	public Item(){
		name = "";
		value = "";
		group = new Group();
		listcost = new ArrayList<>();
	}
	public float getValues(){
		return cost;
	}
	public ArrayList<Float> getList(){
		return listcost;
	}
//	public void addCostToList(Float cost){
//		listcost.add(cost);
//	}
//	public Group getGroup(){
//		return group;
//	}
	public String getName(){
		return name;
	}
	public String getValue(){
		return value;
	}
//	public String getCost (){
//		String result ="";
//		result = String.valueOf(cost);
//		return result;
//	}
	public void setName(String name){
		this.name = name;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public void setCost(float  cost){
		this.cost = cost;
	}
	
	public String toName(){
		return this.name ;
	}
//	public String toValue(){
//		return this.value;
//	}
	public float toCost()
	{
		return this.cost;
	}
	
	public class Group{
		String name_group;
		String id;
		Object report;
		Group(){
			name_group = "";
			id = "";
			report = new Object();
		}
//		public void addReport(Object temp ){
//			this.report = temp;
//		}
		public Object getReport(){
			return report;
		}
		public void setName(String name){
			this.name_group = name;
		}
		public String getName(){
			return name_group;
		}
		public void setId(String id){
			this.id = id;
		}
		public String getId(){
			return id;
		}
	}
	
}
