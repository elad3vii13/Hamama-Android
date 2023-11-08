package communication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.Context;

/**
 * Servlet implementation class BaseServlet
 */
@WebServlet("/BaseServlet")
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Context ctx;
		
		try {
			ctx = new Context(request, response);
		} catch (Exception e) {
			System.out.println("Fatal error: the server is not functioning yet.");
			e.printStackTrace();
			return;
		}
		
		String command = request.getParameter("cmd");
		
		// Validating command
		if (command == null) {
			response.getWriter().print("<p style='font-size: 24px'>Commend is not valid.</p>");
		} else {
			handleRequest(ctx, command);
		}
	}
	
	protected void handleRequest(Context ctx, String cmd) {
		switch(cmd) {
			case "login":
				ctx.handleLogin(false);
				
				break;
			case "logout":
				ctx.handleLogout(false);
				
				break;
			case "measure":
				ctx.getMeasures();
			  
			  break;
			case "sensors":
				ctx.getAllSensors();
			  
			  break;
			case "log":
				ctx.getLogEntries();
			  
			  break;
			default:
				ctx.handleUnknownRequest();
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
