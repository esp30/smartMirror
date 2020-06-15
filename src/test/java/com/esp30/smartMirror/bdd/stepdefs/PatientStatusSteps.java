package com.esp30.smartMirror.bdd.stepdefs;

import static org.assertj.core.api.Assertions.assertThat;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PatientStatusSteps {

//    @Autowired
//    Environment environment;
//    String port = environment.getProperty("local.server.port");
    String port = "8443";
    static final String DEFAULT_ENDPOINT = "https://192.168.160.103:30043";

    CloseableHttpClient httpClient = HttpClients.createDefault();

    String patientName = "";
    int patientID = 0;
    String apiResponse;
    String json;

    // Scenario 1 - Emotion Trends
    @Given("a medical professional that needs to know the trends of emotions for all users")
    public void aMedicalProfessionalThatNeedsToKnowTheTrendsOfEmotionsForAllUsers() {
        System.out.println("Doctor John wants to check on his patients.");
    }

    @And("a mobile app that accesses the system's public API")
    public void aMobileAppThatAccessesTheSystemSPublicAPI() {
        try {
            TrustStrategy acceptingTrustStrategy = new TrustSelfSignedStrategy();

            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

            requestFactory.setHttpClient(httpClient);

            RestTemplate testRestTemplate = new RestTemplate(requestFactory);
            json = testRestTemplate.getForObject(DEFAULT_ENDPOINT + "/emotions", String.class);
//            JSONArray jsonArr = new JSONArray(json);
//
//            apiResponse = jsonArr.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the medical professional should be able to obtain all emotions registered without any user identification")
    public void theMedicalProfessionalShouldBeAbleToObtainAllEmotionsRegisteredWithoutAnyUserIdentification() {
        assert(validParseJsonData(json,"/emotions"));
    }

    // Scenario 2 - Latest Reports
    @Given("a medical professional that takes care of {string} {string}")
    public void aMedicalProfessionalThatCaresForSaidUser (String user, String id) {
        patientName = user;
        patientID = Integer.parseInt(id);
    };

    @When("the medical professional wishes to check on their latest reports")
    public void theMedicalProfessionalWishesToCheckOnTheirUserLatestReports() {
        System.out.println("Doctor John wants to check on " + patientName);
        try {
            TrustStrategy acceptingTrustStrategy = new TrustSelfSignedStrategy();

            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

            requestFactory.setHttpClient(httpClient);

            RestTemplate testRestTemplate = new RestTemplate(requestFactory);
            json = testRestTemplate.getForObject(DEFAULT_ENDPOINT + "/useremotions?id=" + patientName, String.class);
            JSONArray jsonArr = new JSONArray(json);
            JSONObject obj = jsonArr.getJSONObject(0);

            assertThat(obj.get("user").toString() == patientName);
            apiResponse = jsonArr.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the medical professional should be able to access the latest reports of the {string} {string} using the mobile app")
    public void theMedicalProfessionalShouldBeAbleToAccessTheLatestReportsOfTheUserEmotionsUsingTheMobileApp() {
        assert(validParseJsonData(json,"/useremotions"));
    }

    @Then("the medical professional should be able to access the latest reports of the patient using the mobile app")
    public void theMedicalProfessionalShouldBeAbleToAccessTheLatestReportsOfThePatientUsingTheMobileApp() {
        assert(validParseJsonData(json,"/useremotions"));
    }

    private boolean validParseJsonData(String jsonResponse, String method){
        if(method.equals("/emotions")) {
            try {
                JSONArray jsonArray = new JSONArray(jsonResponse);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String value1 = jsonObject1.optString("id");
                    String value2 = jsonObject1.optString("timestamp");
                    String value3 = jsonObject1.optString("value");
                    String value4 = jsonObject1.optString("user");
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("/useremotions")) {
            try {
                JSONArray jsonArray = new JSONArray(jsonResponse);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String value1 = jsonObject1.optString("id");
                    String value2 = jsonObject1.optString("timestamp");
                    String value3 = jsonObject1.optString("value");
                    String value4 = jsonObject1.optString("user");
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
