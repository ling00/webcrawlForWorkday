package webCrawlere;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;

public class Job {
	private String name;
	private String websiteId;
	private String description;
	private String requiste;
	private String source;
	private String procedure;
	private String url;
	public Job() {
		
	}
	
	public Job(String name, String websiteId,String  description,
			String requiste, String source,String procedure,String url) {
		//name, websiteId, description, requiste, source
		this.name=name;
		this.websiteId=websiteId;
		this.description=description;
		this.requiste=requiste;
		this.source=source;
		this.procedure=procedure;
		this.url=url;
	}
	public JSONObject toJSON() {
		return new JSONObject(this);
	}
	
	public String getName() {
		return name;
	}
	
	public String getWebsiteId() {
		return websiteId;
	}
	public String getDescription() {
		return description;
	}

	public String getRequiste() {
		return requiste;
	}
	public String getSource() {
		return source;
	}
	public String getProcedure() {
		return procedure;
	}
	public String getUrl() {
		return url;
	}
	
	public void setName(String name) {
		this.name= name;
	}
	
	public void setWebsiteId(String websiteId) {
		this.websiteId=websiteId;
	}
	public void setDescription(String description) {
		this.description=description;
	}

	public void setRequiste(String requiste) {
		this.requiste=requiste;
	}
	public void setSource(String source) {
		this.source=source;
	}
	public void setProcedure(String procedure) {
		this.procedure=procedure;
	}
	
	public void SetUrl(String url) {
		this.url=url;
	}
	public void write(String companyName) {

		try {
			String jobpath=".\\data\\"+companyName+this.getWebsiteId()+this.getName()+".json";
			FileOutputStream outputStream  = new FileOutputStream(jobpath);
			outputStream.write((this.toJSON().toString()).getBytes());
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

 
}
