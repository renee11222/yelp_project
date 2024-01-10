

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import hw4.User;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
	      //response.getWriter().append("Served at: ").append(request.getContextPath());
			response.setContentType("application/json");
			 PrintWriter out = response.getWriter();
			 Gson gson= new Gson();
			 User user1= new User();
		     
		     try {
		    	 String username = request.getParameter("username");
		    	 String email = request.getParameter("email");
		    	 String password = request.getParameter("password");
		    	 String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		    	 Pattern pattern = Pattern.compile(emailRegex);
		    	 Matcher matcher = pattern.matcher(email);

		    	     if (username == null || username.isBlank( ) ||
		    	    		 password == null|| password.isBlank( ) ||
		    	    		email == null || email.isBlank( ) ) {
		    	    	 this.error("username or password or email is missing", out);
		    	    	 return;
		    	    	 
		    	     }
		    	     if (!matcher.matches()) {
		    	    	    this.error("email is not valid", out);
		    	    	    return;
		    	    	}
		    	     
		    	     Connection connection = null;
		    	     PreparedStatement preparedStatement = null;
		    	     ResultSet resultSet = null;
		    	     
		    	     try {
		    	            Class.forName("com.mysql.cj.jdbc.Driver");

		    	            String dburl = "jdbc:mysql://localhost:3306/hw4";
		    	            String dbUser = "root";
		    	            String dbPassword = "root";

		    	            connection = DriverManager.getConnection(dburl, dbUser, dbPassword);
		    	            
		    	            
		    	            //adjust in the type of error the user sign up has
		    	            String sql1 = "select * from users where email ='"+email+"' limit 1";
		    	            preparedStatement = connection.prepareStatement(sql1);
		    	            resultSet = preparedStatement.executeQuery();
		    	            
		    	            if(resultSet.next()){
		    	            	this.error("Email is already in use", out);
		    	            	return;
		    	            }
		    	           
		    	            
		    	            String sql2 = "select * from users where username='"+username+"' limit 1";
		    	            preparedStatement = connection.prepareStatement(sql2);
		    	            resultSet = preparedStatement.executeQuery();
		    	            
		    	            if(resultSet.next()){
		    	            	this.error("Username is already in use", out);
		    	            	return;
		    	            }
		    	            

		    	            String sql3 = "INSERT INTO users (`username`,`password`,`email`) VALUES ('" + username + "','" + password + "','" + email + "')";
		    	            preparedStatement = connection.prepareStatement(sql3);
		    	            int rowsAffected = preparedStatement.executeUpdate();

		    	            if (rowsAffected > 0) {
		    	                // The insertion was successful
		    	                this.success("success", out);
		    	                return;
		    	            } else {
		    	                // The insertion failed
		    	                this.error("Failed to insert user", out);
		    	                return;
		    	            }
		    	     
		    	        } catch (ClassNotFoundException e) {
		    	        	this.error("An error occurred: " + e.getMessage(),out);
		    	        } catch (Exception e ){
		    	        	this.error("An error occurred: " + e.getMessage(),out);
		    	      }
		     }
		     catch (Exception e) {
		    	 this.error("An error occurred: " + e.getMessage(),out);
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
