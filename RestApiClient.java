import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
                            //THIS IS THE SECOND TASK(REST API CLIENT)
public class RestApiClient {

    public static void main(String[] args) {
        String name = "Ali";
        String apiUrl = "https://api.agify.io/?name=" + name;

        try {
            URI uri = URI.create(apiUrl);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder responseContent = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine);
                }
                in.close();

                // Example JSON: {"name":"Ali","age":22,"count":20325}
                String json = responseContent.toString();

                // Manual JSON parsing
                String fetchedName = getValue(json, "name");
                String age = getValue(json, "age");
                String count = getValue(json, "count");

                System.out.println("=== Age Prediction API ===");
                System.out.println("name = " + fetchedName);
                System.out.println("age = " + age);
                System.out.println("count = " + count);

            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static String getValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) return "Not found";

        start += searchKey.length();
        if (json.charAt(start) == '"') {
            start++;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } else {
            int end = json.indexOf(",", start);
            if (end == -1) {
                end = json.indexOf("}", start);
            }
            return json.substring(start, end).trim();
        }
    }
}
