package autoupdater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Updater {
	
	static final String FILE_URL = "https://api.github.com/repos/volcanocookies/unturnednpccreator/releases/latest";
	static final String FILE_NAME = "Unturned NPC Creator.exe";
	
	public static void main(String[] args) {
		//Delete old program
		File oldVersion = new File(args[0]);
		oldVersion.delete();
		
		//Get latest version
		try {
			Matcher matcher = Pattern.compile("\"browser_download_url\":\"(https://github.com/VolcanoCookies/UnturnedNPCCreator/releases/download/[^\"]*)\"").matcher(getText(FILE_URL));
			if(matcher.find()) {
				System.out.println(matcher.group(1));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
		} catch (IOException e) {
		    // handle exception
			e.printStackTrace();
		}
		
		//Start the new version
		try {
			new ProcessBuilder(oldVersion.getAbsolutePath().substring(0, oldVersion.getAbsolutePath().lastIndexOf("\\")) + "\\" + FILE_NAME).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	static String getText(String url) throws IOException {
	    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	    //add headers to the connection, or check the status if desired..
	    
	    // handle error response code it occurs
	    int responseCode = connection.getResponseCode();
	    InputStream inputStream;
	    if (200 <= responseCode && responseCode <= 299) {
	        inputStream = connection.getInputStream();
	    } else {
	        inputStream = connection.getErrorStream();
	    }

	    BufferedReader in = new BufferedReader(
	        new InputStreamReader(
	            inputStream));

	    StringBuilder response = new StringBuilder();
	    String currentLine;

	    while ((currentLine = in.readLine()) != null) 
	        response.append(currentLine);

	    in.close();

	    return response.toString();
	}
}
