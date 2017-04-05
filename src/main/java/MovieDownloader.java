import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * A class for downloading movie data from the internet.
 * Code adapted from Google.
 *
 * YOUR TASK: Add comments explaining how this code works!
 * 
 * @author Joel Ross & Kyungmin Lee
 */
public class MovieDownloader {

	public static String[] downloadMovieData(String movie) {

		//construct the url for the omdbapi API
		String urlString = "";
		try { // if an exception occurs within this try block, the exception is handled by the catch block (exception handler)
			urlString = "http://www.omdbapi.com/?s=" + URLEncoder.encode(movie, "UTF-8") + "&type=movie";
		}catch(UnsupportedEncodingException uee){ // an exception handler that handles the type of exception indicated by its argument
			return null;
		}

		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;

		String[] movies = null;

		try {

			URL url = new URL(urlString);

			urlConnection = (HttpURLConnection) url.openConnection(); //a connection to the remote object referred to by the URL
			urlConnection.setRequestMethod("GET"); //Set the method for URL request
			urlConnection.connect(); //Opens a communications link to the resource reference by the URL

			InputStream inputStream = urlConnection.getInputStream(); // an input stream of bytes
			StringBuffer buffer = new StringBuffer();
			if (inputStream == null) {
				return null;
			}
			//Buffers characters for efficient reading of characters, arrays and lines.
			reader = new BufferedReader(new InputStreamReader(inputStream));

			String line = reader.readLine(); //Reads a line of text
			while (line != null) {
				buffer.append(line + "\n");
				line = reader.readLine();
			}

			if (buffer.length() == 0) {
				return null;
			}
			String results = buffer.toString();
			results = results.replace("{\"Search\":[","");
			results = results.replace("]}","");
			results = results.replace("},", "},\n");

			movies = results.split("\n"); //returns an array of movies
		} 
		catch (IOException e) {
			return null;
		} 
		finally { // contains cleanup code and closes the connection
			if (urlConnection != null) {
				urlConnection.disconnect(); //indicates that other requests to the server are unlikely in the near future
			}
			if (reader != null) {
				try {
					reader.close(); //close input stream
				} 
				catch (IOException e) {
				}
			}
		}

		return movies;
	}


	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);

		boolean searching = true;

		while(searching) {					
			System.out.print("Enter a movie name to search for or type 'q' to quit: ");
			String searchTerm = sc.nextLine().trim();
			if(searchTerm.toLowerCase().startsWith("q")){
				searching = false;
			}
			else {
				String[] movies = downloadMovieData(searchTerm);
				for(String movie : movies) {
					System.out.println(movie);
				}
			}
		}
		sc.close();
	}
}
