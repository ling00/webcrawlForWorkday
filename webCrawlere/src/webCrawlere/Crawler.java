package webCrawlere;  
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
 

 

public class Crawler {

	
	public void crawl(Workday w) { 
		w.updateJobInfo();
	};

 	
	public static void main(String[] args) {		
		Crawler c= new Crawler();
    	String data;
		try {
			String workDaypath=".\\data\\workday.json";
			data = new String(Files.readAllBytes(Paths.get(workDaypath)));
			JSONArray array =new JSONArray(data); 
			for(Object j: array) {
				JSONObject w=(JSONObject)j; 
				c.crawl(new Workday(w)); 
			}  
		} catch (IOException e) { 
			e.printStackTrace();
		} 	
	}

}
