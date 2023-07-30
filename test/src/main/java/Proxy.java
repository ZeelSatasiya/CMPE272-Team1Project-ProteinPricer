import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.json.JSONArray;


public class Proxy{

	private String apiurl = "https://api.nal.usda.gov/fdc";
	private String apikey = "UkaojsuvAJCo3lryhbiiUg0eodTWSnmQyCIYr76d";


	/* this code is based on Unirest, which has different dependencies than imported in this program*/
	public void search(String term)
	{
		String urlMod = "?query=" + term;
		try { 
			HttpResponse<JsonNode> response = Unirest.get( apiurl + "/foods/search/" + urlMod )
                .header( "apikey", apikey )
                .asJson() ;
            JSONObject json = response.getBody().getObject() ;
            System.out.println(json.toString());
		} catch (Exception e) {
			System.out.println( e.toString() ) ;
		}

	}

}

