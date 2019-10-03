package webCrawlere;
 
import java.net.URL;
 
import org.json.JSONObject;

public class MultiUpdater extends Thread{
	
	private URL url; 
	private JSONObject all;
	private String pagenation;
	private int size;
	private Workday w;
	
	public MultiUpdater(Workday w,URL url, JSONObject all, String pagenation, int size) {		
		this.w=w;
		this.url= url; 
		this.all=all;
		this.pagenation=pagenation;
		this.size= size;
	}

	public void run() {
		w.update(w,url,all,pagenation,size);
	}
	

}
