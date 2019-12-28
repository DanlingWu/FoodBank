package com.findfoodbank.rest.foodbank;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;

@Service
public class FoodBankGeocodeService {

    /**
     * External API call is asynchronous so not to block the app or user.
     * @param foodbank
     * @param repo
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */
    @Async("processExecutor")
    public void setGeoCode(FoodBank foodbank, FoodBankRepository repo) throws UnsupportedEncodingException {
        /** setup API call to Google geocode **/
        String apiKey = "AIzaSyCeQirfQpcZQpfwuOy8EMgq4uVKl1tU90s";
        String addressEncoded = URLEncoder.encode(foodbank.getAddress(), "UTF-8");
        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?";
        apiUrl = apiUrl + "address=" + addressEncoded;
        apiUrl = apiUrl + "&key=" + apiKey;
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        //String jsonString = rt.getForObject(apiUrl, String.class);

        /** call the API **/
        ResponseEntity<String> jsonString = rt.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        JsonObject jsonResult = new JsonParser().parse(jsonString.getBody()).getAsJsonObject();

        /** update Foodbank entity lat lng **/
        JsonObject latLngLocation = jsonResult.get("results").getAsJsonArray().get(0)
                .getAsJsonObject().get("geometry")
                .getAsJsonObject().get("location").getAsJsonObject();
        BigDecimal lat = latLngLocation.get("lat").getAsBigDecimal();
        BigDecimal lng = latLngLocation.get("lng").getAsBigDecimal();
        foodbank.setLatitude( lat );
        foodbank.setLongitude( lng );

        repo.save(foodbank);
    }
}
