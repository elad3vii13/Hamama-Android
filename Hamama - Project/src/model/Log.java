package model;

public class Log {
	private long time;
	private String message;
	private int sid;
	private String priority;
	
	public Log() {};
	
	public Log(long time, String priority, String message, int sid) {
		super();
		this.sid = sid;
		this.time = time;
		this.priority = priority;
		this.message = message;
	}

	public long getTime() {
		return time;
	}

	public int getSid() {
		return this.sid;
	}
	
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	public void setTime(long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getpriority() {
		return priority;
	}

	public void setpriority(String priority) {
		this.priority = priority;
	}
}
