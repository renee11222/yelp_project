import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import hw4.Datum;
import hw4.User;
import hw4.tt;
import hw4.Restaurant;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
/**
 * Servlet implementation class RestSearch
 */
@WebServlet("/RestSearch")
public class RestSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			response.setContentType("application/json");
			 PrintWriter out = response.getWriter();
			 Gson gson= new Gson();
			 Restaurant rest1= new Restaurant();
		     
		     try {
		    	 String name = request.getParameter("name");
		    	 String latitude = request.getParameter("latitude");
		    	 String longitude = request.getParameter("longitude");
		    	 String orderby = request.getParameter("orderby");
		    	 
		    	 
		    	 	// what parameter is missing here
		    	     if (name == null || name.isBlank( ))
		    	    		  {
		    	    	 this.error("Missing Restaurant Name", out);
		    	    	 return; 
		    	     }
		    	     if (latitude == null|| latitude.isBlank( ) ||
			    	    		longitude == null || longitude.isBlank( ) ) {
		    	    	 this.error("Latitude or Longitude is missing", out);
		    	    	 return;  
		    	     }
		    	     
		    	     Connection connection = null;
		    	     PreparedStatement preparedStatement = null;
		    	     ResultSet resultSet = null;
		             String urlEncodedRestName = name.replace(" ", "%20");
		    	     
		    	     String key="Bearer 12HpdS4QHV9ldZCkGBAhYWin4A9imMjoZq5RUaZbqLhjj7lZztlJL96YcVjSLIl8QnCRt8h4Ti3nNNThEGSm5g0GCEDeKHoZciNNgod2DIns7uTKSHhgx2Zug804ZXYx";
		    	     String urlStr = "https://api.yelp.com/v3/businesses/search?latitude=" + latitude + "&longitude=" + longitude + "&term="
		    	     + urlEncodedRestName;
		    	
		    	     // sort by best match 
		    	     if (orderby.equals("best match")) {
		    	     	 urlStr+= "&sort_by=best_match&limit=10";
		    	     }

		    	     // sort by rating  
		    	     else if (orderby.equals("rating")) {
		    	     	 urlStr+= "&sort_by=rating&limit=10";
		    	     }

		    	     // sort by review count
		    	     else if (orderby.equals("review count")) {
		    	     	 urlStr+= "&sort_by=review_count&limit=10";
		    	     }

		    	     // sort by distance
		    	     else if(orderby.equals("distance")) {
		    	     	 urlStr+= "&sort_by=distance&limit=10";
		    	     }
		    	     
		    	     HttpRequest request_ = HttpRequest.newBuilder()
		 				    .uri(URI.create(urlStr))
		 				    .header("accept", "application/json")
		 				    .header("Authorization", key)
		 				    .method("GET", HttpRequest.BodyPublishers.noBody())
		 				    .build();
		    	     
		    	     HttpResponse<String> response_ = HttpClient.newHttpClient().send(request_, HttpResponse.BodyHandlers.ofString());
						//System.out.println(response.body());
						String responseBody = response_.body();
						Datum temp = gson.fromJson(responseBody, Datum.class);
						
		    	     
		    	     try {
		    	    	 
		    	            Class.forName("com.mysql.cj.jdbc.Driver");

		    	            String dburl = "jdbc:mysql://localhost:3306/hw4";
		    	            String dbUser = "root";
		    	            String dbPassword = "root";

		    	            connection = DriverManager.getConnection(dburl, dbUser, dbPassword);
		    	            
		    	            for (int i = 0; i < temp.businesses.size(); i++) {
		    	                String rest_name = temp.businesses.get(i).name;

		    	                // Check if the restaurant already exists in the database
		    	                String selectSql = "SELECT * FROM restaurants WHERE name = ?";
		    	                preparedStatement = connection.prepareStatement(selectSql);
		    	                preparedStatement.setString(1, rest_name);
		    	                resultSet = preparedStatement.executeQuery();
		    	                String location = "";
			    	             
		    	                for(int j=0; j<temp.businesses.get(i).location.display_address.size();j++) {
			    	            	   location+= " "+temp.businesses.get(i).location.display_address.get(j);
			    	               } 
			    	               
			    	               String cuisine="";
			    	               if(temp.businesses.get(i).categories != null) {
			    	            	   for(int j=0; j<temp.businesses.get(i).categories.size();j++) {
				    	            	   if(j==temp.businesses.get(i).categories.size()-1) {
				    	            		   cuisine+= temp.businesses.get(i).categories.get(j).title;
				    	            	   }
				    	            	   else {
				    	            		   cuisine+= temp.businesses.get(i).categories.get(j).title+ ", ";
				    	            	   }
				    	               } 
			    	            	   
			    	               }
			    	               System.out.println(cuisine);
			    	               
		    	                tt temp_tt = new tt();
			    	               temp_tt = temp.businesses.get(i);
			    	               temp_tt.address = location;
			    	               temp_tt.cuisine= cuisine;
			    	               temp.businesses.set(i, temp_tt);
			    	               
		    	                if (resultSet.next()) {
		    	                    continue;
		    	                }

		    	                // Restaurant details
		    	          
		    	                String image_url = temp.businesses.get(i).image_url;
		    	                String url = temp.businesses.get(i).url;
		    	                float rating = temp.businesses.get(i).rating;
		    	                String display_phone = temp.businesses.get(i).display_phone;
		    	                String price = temp.businesses.get(i).price;
		    	                String cuisine1 = temp.businesses.get(i).cuisine;
		    	             
		    	               
		    	               //temp.businesses.get(i).address= location;
		    	               
		    	                String insertSql = "INSERT INTO restaurants (`name`, `address`, `phone`, `url`, `pic`, `cuisine`, `price`, `rating`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		    	                preparedStatement = connection.prepareStatement(insertSql);
		    	                preparedStatement.setString(1, rest_name);
		    	                preparedStatement.setString(2, location);
		    	                preparedStatement.setString(3, display_phone);
		    	                preparedStatement.setString(4, url);
		    	                preparedStatement.setString(5, image_url);
		    	                preparedStatement.setString(6, cuisine1);
		    	                preparedStatement.setString(7, price);
		    	                preparedStatement.setFloat(8, rating);

		    	                // Execute the INSERT operation
		    	                int rowsAffected = preparedStatement.executeUpdate();
		    	            }

		    	            success(gson.toJson(temp.businesses),out);
	    	                return;
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
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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


