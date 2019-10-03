package webCrawlere;
 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*; 


public class Workday  { 
	private String companyName ;
	private String companyUrl ;	
	private static final int noOfItemInThePage=50; //each page only loads 50 jobs
	
	public Workday(String name,String url) {
		companyName=name;
		companyUrl=url;
	}
	
	public Workday(JSONObject o) {
		companyName=  o.getString("companyName");
		companyUrl=  o.getString("companyUrl");
	}
	
	public JSONObject toJSONObject() {
		JSONObject o =new JSONObject(this);
		return o;
	}
	public  String getCompanyUrl() {
		return companyUrl;
	}
	public String getCompanyName() {
		return companyName;
	}


	public void updateJobInfo() {
 
		try {  
			 URL url =new URL(companyUrl); 
			 JSONObject all =getJSONDataFromRequest(url);
			 JSONArray endPoints=(JSONArray) getNestedKeyValueFromJSON(all,"endPoints");
			 String pagenation = getPagenation(endPoints);
			 String toReplace = "/"+companyUrl.split("/")[companyUrl.split("/").length-1];
			 pagenation=pagenation.replace(toReplace, "");			
			 int size=findTheMaxSize(url,pagenation,all); //max number of jobs(multiple of 50)
			 for(int i=noOfItemInThePage;i<=size;i=i+noOfItemInThePage) {
				 //crawl the webpage using multi thread
				 MultiUpdater m1=new MultiUpdater(this,url,all,pagenation,i); 
				 m1.start(); //will call the update() method
			  }
			  
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	private int findTheMaxSize(URL url, String pagenation, JSONObject all) {
		// TODO Auto-generated method stub
		int size=0;
		while(true) {
			try {
				all =this.getJSONDataFromRequest(url);
				JSONArray listItems=(JSONArray)this.getNestedKeyValueFromJSON(all,"listItems");
				if (listItems!=null) {
					size=size+noOfItemInThePage;
				}
				else {
					break;
				}
				url =new URL(this.getCompanyUrl()+pagenation.toString()+"/"+String.valueOf(size));				
			} catch ( IOException e) { 
				e.printStackTrace();
			}
		} 
		return size;
	}

	void update(Workday w,URL url, JSONObject all, String pagenation, int size){
		int index=size-noOfItemInThePage;
		while(index<size) {
			try {
				url =new URL(w.getCompanyUrl()+pagenation.toString()+"/"+String.valueOf(index));
				System.out.println(url);
				all =w.getJSONDataFromRequest(url);
				JSONArray listItems=(JSONArray)w.getNestedKeyValueFromJSON(all,"listItems");
				//listItems->title->commandLink
				if (listItems!=null) {
					 for(Object j:listItems) {
						 if(j instanceof JSONObject) {
							 JSONObject title=(JSONObject) ((JSONObject) j).get("title");
							 String link=(String) title.get("commandLink"); 
							 String date=j.toString().split("Posted")[1];
							 date="Posted"+date.split("\"")[0]; 
							 index++; 
							 Job job= w.getJobInfo(link,date);
							 job.write(w.getCompanyName());
						 }
					 }
				 }
				 if(listItems==null) {
					 break;
				 }
				 } catch (IOException e) {
					 e.printStackTrace();
					 }
			}
		}
	private Job getJobInfo(String link,String date) {
	// parse from url /en-US/Careers/job/NTU-Main-Campus-Singapore/Lead-Counsellor_R00000002-1
		String  []jobInfo=link.split("/");
		//name, websiteId, description,requiste, source,procedure, url
		String location= jobInfo[jobInfo.length-2];
		String name=jobInfo[jobInfo.length-1].split("_")[0].replace("-", " ");
		String websiteId=jobInfo[jobInfo.length-1].split("_")[1];
		link=companyUrl+"/job"+link.split("job")[1];
		String des=""; 
		try {
			URL url  = new URL(link);
			JSONObject jobJS =getJSONDataFromRequest(url);	 	 
		 	des=(String) getNestedKeyValueFromJSON( jobJS,  "description");
		} catch (IOException e) {
			e.printStackTrace();
			
		} 
		return new Job(name,websiteId,des,"",location,date,link);
	}

	private String getPagenation(JSONArray endPoints) { 
		String pagenation="";
		 if (endPoints!=null) {
			 for(Object j:endPoints) {
				 if(j instanceof JSONObject) {
					 String type=   (String) ((JSONObject) j).get("type");
					 if (type.equalsIgnoreCase("Pagination")) {
						 pagenation=(String) ((JSONObject) j).get("uri");
						 break;
					 }
				 }
			 }
		 }
		return pagenation;
	}
	private JSONObject getJSONDataFromRequest(URL url) throws IOException {
		 HttpURLConnection con  = (HttpURLConnection) url.openConnection(); 
		 con.setRequestProperty("Accept", "application/json,application/xml,charset=utf-8"); 	 
		 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		 String inputLine;
		 String out="";
		 while ((inputLine = in.readLine()) != null)
		        out=out+inputLine;
		 in.close();
		 con.disconnect();
		 if(out.endsWith("}")&&out.startsWith("{")) {
			 return new JSONObject(out);
		 }
		 else {
			 return new JSONObject();
		 }
	}

	private Object getNestedKeyValueFromJSON(JSONObject all,String target) {		
		for(Object k : all.keySet()) { 
			//System.out.println(k);
			if(k.equals(target)) {
				return   all.get((String) k);
			} 			
			if(all.get((String) k) instanceof JSONObject || all.get((String) k) instanceof JSONArray) {
				if(all.get((String) k).toString().contains('"'+target+'"'+':')) {
					if(all.get((String) k) instanceof JSONObject) { 
						return getNestedKeyValueFromJSON((JSONObject) all.get((String) k),target);
					}
					else {
						JSONArray ja= (JSONArray)all.get((String) k);
						JSONArray result=null;
						for(Object j: ja){
							result=(JSONArray) getNestedKeyValueFromJSON((JSONObject) j,target);
							if (result!=null) {
								return result;
							}
						}
					}
				}		
			}
		}
		return null;
	}
}
