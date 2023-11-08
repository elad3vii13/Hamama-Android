package model;

public class Measure {
	private long time;
	private int sid;
	private double value;
	
	public Measure(long time, double value, int sid) {
		super();
		this.time = time;
		this.value = value;
		this.sid = sid;
	}
	
	public int getSid() {
		return this.sid;
	}
	
	public void setSid(int sid) {
		this.sid = sid;
	}

	public Measure() {};
	
	public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
	}


	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Measure [time=" + time + ", value=" + value + "]";
	}
}
