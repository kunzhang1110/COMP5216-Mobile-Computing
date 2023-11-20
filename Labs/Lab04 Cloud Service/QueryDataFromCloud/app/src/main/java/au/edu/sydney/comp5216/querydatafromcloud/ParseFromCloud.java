package au.edu.sydney.comp5216.querydatafromcloud;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ParseFromCloud {

    private final String USER_AGENT = "Mozilla/5.0";

    public String httpResponse;

    public ArrayList<TemperatureData> tds;

    public ParseFromCloud(){
    }

    public void getTemperature() throws Exception {
        this.sendGet();
    }

    // HTTP GET request
    public void sendGet() throws Exception {

        String url = "https://comp5216.azurewebsites.net/rest/temperatures";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Optional default is GET
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Print result string
        System.out.println(response.toString());
        // Convert result
        this.JsonToData(response.toString());
    }

    // Convert from JSON to TemperatureData objects
    private void JsonToData(String json) {
        tds = new ArrayList<TemperatureData>();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                TemperatureData td = new TemperatureData(jsonObject.getDouble("temperature"), jsonObject.getString("createdAt"));
                tds.add(td);
            }
        } catch (Exception e){e.printStackTrace();}
    }

//    // HTTP POST request
//    private void sendPost() throws Exception {
//
//        String url = "http://usydcomp5216.azurewebsites.net/uploadtemp";
//        URL obj = new URL(url);
//        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
//
//        //add reuqest header
//        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);
//        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//
//        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
//
//        // Send post request
//        con.setDoOutput(true);
//        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//        wr.writeBytes(urlParameters);
//        wr.flush();
//        wr.close();
//
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + urlParameters);
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        //print result
//        System.out.println(response.toString());
//
//    }
}
