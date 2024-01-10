

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import hw4.Restaurant; 
import hw4.User;

/**
 * Servlet implementation class DetailsServelt
 */
@WebServlet("/DetailsServelt")
public class DetailsServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DetailsServelt() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String name = request.getParameter("name");
		 response.setContentType("application/json");
		 PrintWriter out = response.getWriter();
		 Gson gson= new Gson();
	      HttpSession session = request.getSession();
	        
	        	Connection connection = null;
	    	     PreparedStatement preparedStatement = null;
	    	     ResultSet resultSet = null;
	    	     
	        	try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					String dburl = "jdbc:mysql://localhost:3306/hw4";
	 	            String dbUser = "root";
	 	            String dbPassword = "root";
	 	       
	 	           connection = DriverManager.getConnection(dburl, dbUser, dbPassword);
	 	        String sql = "SELECT * FROM restaurants where name = ? LIMIT 1";
	 	       preparedStatement = connection.prepareStatement(sql);
	           preparedStatement.setString(1, name); 
	           resultSet = preparedStatement.executeQuery();
	 	      

	 	        if(!resultSet.next()) {
	 	        	this.error("restaurant not found", out);
	 	        	return;
	 	        }
	           Restaurant temp= new Restaurant();
	        temp.id = resultSet.getInt("id");
	           temp.name = resultSet.getString("name");
	           temp.address = resultSet.getString("address");
	           temp.phone = resultSet.getString("phone");
	           temp.url = resultSet.getString("url");
	           temp.image_url = resultSet.getString("pic");
	           temp.cuisine = resultSet.getString("cuisine");
	           temp.price = resultSet.getString("price");
	           temp.rating = resultSet.getFloat("rating");
               
          if(session.getAttribute("loginInfo") == null) {
          // haven't login
        	  	temp.isfav = 3;
          }
          else {
           // login
           String userInfo= (String) session.getAttribute("loginInfo");
           User user2 = gson.fromJson(userInfo, User.class);
           String sql2 = "SELECT * FROM favorites WHERE name = ? AND user_id = ? LIMIT 1";
           preparedStatement = connection.prepareStatement(sql2);
           preparedStatement.setString(1, name); 
           preparedStatement.setInt(2, user2.id); 
           resultSet = preparedStatement.executeQuery();

                 if(!resultSet.next()){
                  temp.isfav = 1;
              }
                 else {
                  temp.isfav = 2;
                 }
          }
	 	       success(gson.toJson(temp),out);
	           
	           return;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
	}
	private void error(String errmsg, PrintWriter out) {
		Apierror err= new Apierror();
		err.errmsg = errmsg;
		Gson gson= new Gson();
		String jsonString= gson.toJson(err);
        out.print(jsonString);
        out.flush();
        // system exit here
	}
	
	private void success(String data, PrintWriter out) {
		Apierror err= new Apierror();
		err.data = data;
		err.code = 0;
		Gson gson= new Gson();
		String jsonString= gson.toJson(err);
        out.print(jsonString);
        out.flush();
        // system exit here
	}

}
