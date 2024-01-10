import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;
import hw4.User;
import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		 User user1= new User();
	
	      String type = request.getParameter("type");

	    try {
	    	  System.out.println("0");
	    	 if("logstatus".equals(type)) {
	    	        HttpSession session = request.getSession();
	    	        
	    	        if(session.getAttribute("loginInfo") == null) {
	    	        // haven't login
	    	         success("nologin",out);
	    	         return;
	    	        }
	    	        else {
	    	         // login
	    	         String userInfo= (String) session.getAttribute("loginInfo");
	    	         success(userInfo,out);
	    	          return;
	    	        }
	    	       }
	    	 else if("login".equals(type)) {
	    		 String username = request.getParameter("username");
	    	     String password = request.getParameter("password");
	    	     
	    	     
	    	     if (username == null || username.isBlank( ) ||
	    	    		 password == null|| password.isBlank( )) {
	    	    	 this.error("username or password is missing", out);
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
	    	            
	    	           // System.out.println("login success");
	    	            
	    	            String sql = "select * from users where username='"+username+"' limit 1";
	    	            preparedStatement = connection.prepareStatement(sql);
	    	            resultSet = preparedStatement.executeQuery();
	    	            
	    	            if(!resultSet.next()){
	    	            	//user not found
	    	            	this.error("Username not found", out);
	    	            	 return;
	    	            }
	    	             user1.id = resultSet.getInt("id");
	    	             user1.username = resultSet.getString("username");
	    	             user1.password = resultSet.getString("password");
	    	             user1.email = resultSet.getString("email");
	    	             
	    	             if(!user1.password.equals(password)) {
	    	            	 this.error("Password not correct", out);
	    	            	 return;
	    	             }
	    	          
	    	         
	    	            HttpSession session = request.getSession();
	    	    	    session.setAttribute("loginInfo", gson.toJson(user1));
	    	    	    success(gson.toJson(user1),out);
	    	    	    return;
	    	       // } catch (ClassNotFoundException e) {
	    	           // e.printStackTrace();
	    	        } catch (Exception e ){
	    	        	this.error("An error occurred: " + e.getMessage(),out);
	    	        } /*finally {
	    	            if (resultSet != null) {
	    	                try {
	    	                    resultSet.close();
	    	                } catch (SQLException throwables) {
	    	                    throwables.printStackTrace();
	    	                }
	    	            }

	    	            if (preparedStatement != null) {
	    	                try {
	    	                    preparedStatement.close();
	    	                } catch (SQLException throwables) {
	    	                    throwables.printStackTrace();
	    	                }
	    	            }

	    	            if (connection != null) {
	    	                try {
	    	                    connection.close();
	    	                } catch (SQLException throwables) {
	    	                    throwables.printStackTrace();
	    	                }
	    	            }
	    	        } */
	    	    
	    	 }
	    	 
	    	 else if("logout".equals(type)) {
	    		 HttpSession session = request.getSession();
		    	 session.removeAttribute("loginInfo");
		    	 success("success",out);
		    	 return;
	    	 }
	    	 else {
	    		 this.error("type is missing", out);
	    		 return;
	    	 }
	     }
	     catch (Exception e) {
	    	 this.error("An error occurred: " + e.getMessage(),out);
	    	 return;
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
        //System.exit(0);
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
        //System.exit(0);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	

}

class Apierror{
	 public int code=9999;
	 public String errmsg;
	 public String data;
	}
