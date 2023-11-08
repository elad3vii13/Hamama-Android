package communication;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.Context;

@WebServlet(name = "HelloAppEngine", urlPatterns = { "/board" })
public class BoardServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int reqID = -1;
		
		try {
			PrintWriter out = response.getWriter();
			Context ctx = new Context(request, response);
			reqID = request.getIntHeader("REQID");
			String command = request.getParameter("cmd");
			
			if (command == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				switch (command) {
					case "measure":
						ctx.AddMeasure();
						
						break;
					case "log":
						ctx.AddLogEntry();
						
						break;
					case "time":
						out.write(ctx.getCurrentTime());
						
						break;
					default:
						ctx.addInternalLogEntry();
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						break;
				}
			}
		} catch (Exception e) {
			System.out.println("Fatal error: the server is not functioning yet.");
			e.printStackTrace();
		} finally {
			response.addIntHeader("REQID", reqID);
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}