package logic;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import model.Log;
import model.Measure;
import model.MySQLDB;
import model.Sensor;
import model.User;

	//	This class is a middle layer class between the "communication" layers 
	//	and the back-end/model/db layers. it provides mainly business logic to
	//	your web site.
	//	it has two constructors, one usually used from servlets/listeners modules 
	//	and the other used from jsp pages

public class Context {
	final static boolean isProduction = SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
	final static String SESSION_KEY_USER = "currentUser";
	final static String SESSION_KEY_MANAGER = "isManager";
	
	MySQLDB dbc;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext application;
	PrintWriter out;
	
	// Used mainly from JSP	
	public Context(PageContext pContext) throws Exception {
		this((HttpServletRequest)pContext.getRequest(), 
				(HttpServletResponse)pContext.getResponse());
	}
	
	// Used mainly from servlets
	public Context(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.dbc = new MySQLDB(isProduction, true);
		this.request = request;
		this.response = response;
		this.session = request.getSession();
		this.application = this.session.getServletContext();
		
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			this.out = response.getWriter();
			
			if (dbc.IsDbConnected() == false) {
				throw(new Exception("no db connection"));
			}
		} catch (IOException e) {};
	}
	
	public void insertAlertDlg(String msg, String forwardToPage){
		out.write("<script charset=\"UTF-8\">");
		out.write("alert('" + msg + "');");
		
		if (forwardToPage!= null) {
			out.write("window.location.href='"+ forwardToPage + "';");
		}
		
		out.write("</script>");
	}
	
	public List<User> getAllUseList() {
		return dbc.getAllUsers();
	}
	
	public void deleteUserByName() {
		String username=  request.getParameter("nickname");
		dbc.DeleteUser(username);
		
		try {
			response.sendRedirect("management.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleLogout(boolean fromMobile){
		this.session.removeAttribute(SESSION_KEY_USER);
		
		try {
			if(!fromMobile) {
				response.sendRedirect("home.jsp");
			} else {
				this.session.invalidate();
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isLoggedIn(){
		return (this.session.getAttribute(SESSION_KEY_USER) != null);
	}
	
	public boolean isManager(){
		User u = (User) this.session.getAttribute(SESSION_KEY_USER);
		
		return (u != null) && u.getRole().equals(User.MGR_ROLE);
	}
	
	public String getCurrentUserName() {
		User u = (User) this.session.getAttribute(SESSION_KEY_USER);
		
		return (u == null) ? "אורח" : u.getNickName();
	}
	
	public void handleLogin(boolean fromMobile) {
		long lastLogin = System.currentTimeMillis();
		String nickname= request.getParameter("nickname");
		String password= request.getParameter("password");
		
		try {
			User u = dbc.UserAuthenticate(nickname, password);
			
			if(u!=null) {
				this.session.setAttribute(SESSION_KEY_USER, u);
				dbc.updateLastLogin(lastLogin, nickname);
				
				if(fromMobile) {
					out.print(u.getId());
				} else {
					String url = "home.jsp?name=" + URLEncoder.encode(nickname, "UTF-8");
					response.sendRedirect(url);
				}
			} else {
				if (fromMobile) {
					out.print(-1);
				} else {
					request.setAttribute("error", "פרטי משתמש לא נכונים");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			}
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		} 
	}
	
	public void handleRegistration() {
		long lastLogin = System.currentTimeMillis();
		String nickname= request.getParameter("nickname");
		
		if (userCanBeRegistered(nickname)) {
			dbc.AddNewUser(userFromRequest());
			handleLogin(false);
		} else {
			request.setAttribute("error", "שם המשתמש קיים, אנא נסה שנית");
			
			try {
				request.getRequestDispatcher("registration.jsp").forward(request, response);
				dbc.updateLastLogin(lastLogin, nickname);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getFieldFromRequest(String key) {
		String x = request.getParameter(key);
		
		return (request.getParameter(key) != null? request.getParameter(key): "");
	}
	
	private User userFromRequest() {
		User u = new User();
		u.setNickName(getFieldFromRequest("nickname"));
		u.setPassword(getFieldFromRequest("password"));
		u.setRole(getFieldFromRequest("role"));
		
		return u;
	}
	
	private boolean userCanBeRegistered(String nickname){
		return !dbc.UserExists(nickname);
	}
	
	public void handleUnknownRequest() {
		try {
			response.sendRedirect("home.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void AddMeasure() {
		  String value = request.getParameter("value");
		  String time = request.getParameter("time");
		  String sid = request.getParameter("sid");
		  
		  if (time == null || value == null || sid == null) {
			  addInternalLogEntry();
			  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			  return;
		  }
		  
		  Measure m = new Measure(Long.parseLong(time), 
				  Double.parseDouble(value), Integer.parseInt(sid));
		  dbc.addMeasure(m);
	}
	
	public void AddLogEntry() {
		 try {
			 String time = request.getParameter("time");
			 String priority = request.getParameter("priority");
			 String message = request.getParameter("message");
			 String sid= request.getParameter("sid");
			 
			 if (time == null || priority == null || message == null || sid == null) {
				 addInternalLogEntry();
				 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				 
				 return;
			 }
			 
			 Log log = new Log(Long.parseLong(time), priority, message, Integer.parseInt(sid));
			 dbc.addLogEntry(log);
			 response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	public void addInternalLogEntry() { 
	  String time = Long.toString(System.currentTimeMillis());
	  String priority = "error";
	  String message = "Bad formated request from the board. URI=" + request.getRequestURI() + ", Parameters= " + request.getParameterMap();
	  String sid = "-1";
	  Log log = new Log(Long.parseLong(time), priority, message, Integer.parseInt(sid));
	  dbc.addLogEntry(log);
	}
	
	public void getLogEntries() {
		try {
			String sid = request.getParameter("sid");
			String from = request.getParameter("from");
			String to = request.getParameter("to");
			String priority = request.getParameter("priority");
			
			 if (from == null || to == null)
				  throw new Exception("bad get log command");
			 
			HashMap<Integer, String> sensors = dbc.getAllSensorsNames();
			ArrayList<Log> list = dbc.getLogEntries(sid, Long.parseLong(from), 
					Long.parseLong(to), priority);
			Type listType = new TypeToken<ArrayList<Log>>() {}.getType();
			Gson gson = new Gson(); 
			String jsonResult = gson.toJson(list, listType);
			response.setContentType("application/json");
			response.getWriter().print(jsonResult.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	public void getMeasures() {
		String sid = request.getParameter("sid");
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		ArrayList<Measure> list = dbc.getMeasures(Integer.parseInt(sid), Long.parseLong(from), Long.parseLong(to));
		Type listType = new TypeToken<ArrayList<Measure>>() {}.getType();
		Gson gson = new Gson(); 
		Sensor sensor = dbc.getSensorProperties(Integer.parseInt(sid));
		JsonObject jsonResult = (JsonObject) gson.toJsonTree(new Sensor(Integer.parseInt(sid),
				sensor.getName(), sensor.getUnits(), sensor.getDisplayName()));
		String jsonMeasures = gson.toJson(list, listType);
		jsonResult.addProperty("measures", jsonMeasures);
		response.setContentType("application/json");
		out.print(jsonResult);
		
		return;
	}
	
	public void getAllSensors() {
		ArrayList<Sensor> sensors = dbc.getAllSensors();
		Type listType = new TypeToken<ArrayList<Sensor>>() {}.getType();
		Gson gson = new Gson(); 
		String jsonAllSensors = gson.toJson(sensors, listType);
		response.setContentType("application/json");
		out.print(jsonAllSensors);
		
		return;
	}
	
	public void getCurrentUser() {
		User u = (User) this.session.getAttribute(SESSION_KEY_USER);
		
		if(u != null) {
			out.print(u.getId());
		} else {
			out.print("-1");
		}
	}
	
	public String getCurrentTime() {
		long time = System.currentTimeMillis();
		return Long.toString(time);
	}
}
