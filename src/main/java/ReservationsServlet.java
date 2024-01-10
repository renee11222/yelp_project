

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet implementation class ReservationsServlet
 */
@WebServlet("/ReservationsServlet")
public class ReservationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReservationsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		 Gson gson= new Gson();
		HttpSession session = request.getSession();
		if (session.getAttribute("loginInfo") == null) {
		    // User not logged in
		    this.error("User not logged in", out);
		    return;
		}

		User user = gson.fromJson((String) session.getAttribute("loginInfo"), User.class);
		int userId = user.id;
		String orderby = request.getParameter("orderby");
	

		try {
		    Connection connection = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet resultSet = null;

		    Class.forName("com.mysql.cj.jdbc.Driver");
		    String dburl = "jdbc:mysql://localhost:3306/hw4";
		    String dbUser = "root";
		    String dbPassword = "root";

		    connection = DriverManager.getConnection(dburl, dbUser, dbPassword);
		    
			String sql = "SELECT * FROM `reservations` t1, `restaurants` t2 WHERE t1.name = t2.name AND t1.user_id = ? ";

			if (orderby.equals("1")) {
			    // Sort by alphabetical order (A to Z)
			    sql += "ORDER BY t2.name ASC";
			} else if (orderby.equals("2")) {
			    // Sort by alphabetical order (Z to A)
			    sql += "ORDER BY t2.name DESC";
			} else if (orderby.equals("3")) {
			    // Sort by rating (highest to lowest)
			    sql += "ORDER BY t2.rating DESC";
			} else if (orderby.equals("4")) {
			    // Sort by rating (lowest to highest)
			    sql += "ORDER BY t2.rating ASC";
			} else if (orderby.equals("5")) {
			    // Sort by recent (most recent first)
			    sql += "ORDER BY t1.createtime DESC";
			} else if (orderby.equals("6")) {
			    // Sort by recent (oldest first)
			    sql += "ORDER BY t1.createtime ASC";
			} 

		    preparedStatement = connection.prepareStatement(sql);
		    preparedStatement.setInt(1, userId);
		    resultSet = preparedStatement.executeQuery();
		    
		    System.out.println(sql);
		    List<Restaurant> favoriteRestaurants = new ArrayList<>();

		    while (resultSet.next()) {
		        Restaurant restaurant = new Restaurant();
		        restaurant.name = resultSet.getString("t2.name");
		        restaurant.address = resultSet.getString("t2.address");
		        restaurant.rating = resultSet.getFloat("t2.rating");
		        restaurant.image_url = resultSet.getString("t2.pic");
		        restaurant.url = resultSet.getString("t2.url");
		        restaurant.display_phone = resultSet.getString("t2.phone");
		        restaurant.cuisine = resultSet.getString("t2.cuisine");
		        restaurant.price = resultSet.getString("t2.price");
		        restaurant.date =resultSet.getString("t1.date");
		        restaurant.time =resultSet.getString("t1.time");
		        restaurant.notes =resultSet.getString("t1.notes");
		        favoriteRestaurants.add(restaurant);
		    }
		    String jsonResult = gson.toJson(favoriteRestaurants);

		    success(jsonResult,out);
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		} catch (SQLException e) {
		    e.printStackTrace();
		}    
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String name = request.getParameter("name");
		 String date= request.getParameter("date");
		 String time= request.getParameter("time");
		 String notes= request.getParameter("notes");
		 PrintWriter out = response.getWriter();
		
		 response.setContentType("application/json");
		 Gson gson= new Gson();
		 HttpSession session = request.getSession();
	        
	        if(session.getAttribute("loginInfo") == null) {
	        // haven't login
	         this.error("User not login",out);
	         return;
	        }
	        if ( date == null|| date.isBlank( ) ||
		    		time == null || time.isBlank( ) ) {
		    	 this.error("missing reservation date or time", out);
		    	 return;
		    	 
		     }
	        else {
	 	        	Connection connection = null;
		    	     PreparedStatement preparedStatement = null;
		    	     ResultSet resultSet = null;
		    	     
	 	        	try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						String dburl = "jdbc:mysql://localhost:3306/hw4";
		 	            String dbUser = "root";
		 	            String dbPassword = "root";

		 	         connection = DriverManager.getConnection(dburl, dbUser, dbPassword);
		 	            
		 	        String userInfo= (String) session.getAttribute("loginInfo");
		 	        User user1 = gson.fromJson(userInfo, User.class);
		 	        int user_id= user1.id;
		 	   
		         
		           String insertSql = "INSERT INTO reservations (`name`, `user_id`, `createtime`, `date`, `time`, `notes`) VALUES (?, ?, NOW(), ?, ?, ?)";

		           preparedStatement = connection.prepareStatement(insertSql);
		           preparedStatement.setString(1, name);
		           preparedStatement.setInt(2, user_id);
		           preparedStatement.setString(3, date);
		           preparedStatement.setString(4, time);
		           preparedStatement.setString(5, notes);

		           int rowsAffected = preparedStatement.executeUpdate();
		           
		           this.success("success reservation", out);
		           return;
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 	        }
		
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
