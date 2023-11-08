	package model;

public class Sensor {
	long id;
	String displayName;
	String name;
	String units;
	
	public Sensor(long id, String name, String units, String displayName) {
		super();
		this.displayName = displayName;
		this.id = id;
		this.name = name;
		this.units = units;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public Sensor() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "Sensor [id=" + id + ", name=" + name + ", units=" + units + "]";
	}
}
