package com.proyecto.libsemantica.ProyectoLibreria;

import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class SNOMEDCTLibrary {
	private final static String baseUrl = "http://browser.ihtsdotools.org/api/v1/snomed/";

	  /** The edition. */
	  private final static String edition = "en-edition";

	  /** The version. */
	  private final static String version = "20190131";
	  
	  private static String getUrl() {
		    return baseUrl + "/" + edition + "/v" + version;
	  }

	  
	  public static String findSynonyms(String query) throws Exception {
		  

		    Client client = ClientBuilder.newClient();

		    final WebTarget target =client.target("https://browser.ihtsdotools.org/snowstorm/snomed-ct/browser/MAIN/2020-03-09/concepts/22298006?descendantCountForm=inferred&languages=es");
		
		    final Response response = target.request(MediaType.APPLICATION_JSON).get();
		    
		    final String resultString = response.readEntity(String.class);
		    JSONObject json = new JSONObject(resultString);
		    JSONArray jsonarray = json.getJSONArray("descriptions");
		    System.out.print("Descriptions:"+jsonarray.toString());
		    ArrayList<String> synonyms = new ArrayList<String>();
		    for(int i =0 ;i<jsonarray.length(); i++){
		    	JSONObject jsonaux = jsonarray.getJSONObject(i);
		    //	jsonaux.get("term");
		    	System.out.print("SYNONYMNS->"+jsonaux.getString("term")+"\n");
		    	//synonyms.add(jsonarray.getInt(i));
		    }
		    return resultString; 
	  
	  } 
	  
	  
	  public static String getHypernyms(String query) throws Exception {  
		    Client client = ClientBuilder.newClient(); 
		    final WebTarget target =client.target("https://browser.ihtsdotools.org/snowstorm/snomed-ct/browser/MAIN/SNOMEDCT-ES/2020-04-30/concepts/22298006/parents?form=inferred");
		    final Response response = target.request(MediaType.APPLICATION_JSON).get();
		    final String resultString = response.readEntity(String.class);
		    System.out.print(resultString);
		    return resultString; 
	  } 
	  
	  
	  public static String getHyponyms(String query) throws Exception {  
		    Client client = ClientBuilder.newClient(); 
		    final WebTarget target =client.target("https://browser.ihtsdotools.org/snowstorm/snomed-ct/browser/MAIN/SNOMEDCT-ES/2020-04-30/concepts/64572001/children?form=inferred");
		    final Response response = target.request(MediaType.APPLICATION_JSON).get();
		    final String resultString = response.readEntity(String.class);
		    System.out.print(resultString);
		    return resultString; 
	  } 
	  
	  
	  public static void main(String[] arg) throws Exception{
		  System.out.print("SNOMED CONNECTION");
		//  findSynonyms("heart attack");
		  getHypernyms("heart attack");
		  //  System.out.print("RESULTADO"+findByQuery("heart attack"));
	  }
}
