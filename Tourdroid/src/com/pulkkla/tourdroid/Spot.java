package com.pulkkla.tourdroid;

public abstract class Spot {
	
	private String datasource = "";
	private int id;
	private String name;
	private Location loc;
	
	public Spot() {
		
	}
	
	public void setDataSource(String datasource) {
		this.datasource = datasource;
	}
	
	public String getDataSource() {
		return this.datasource;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	@Override
	public String toString() {
		//return "id: " + Integer.toString(id) + ", name: " + name + ", source: " + datasource + ", location: " + loc.toString();
		return "id: " + Integer.toString(id) + ", name: " + name + ", source: " + datasource;
	}
	
}
