package com.esp30.smartMirror.bdd.stepdefs;
import com.esp30.smartMirror.data.Emotion;
import cucumber.api.java.en.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

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
            String json = testRestTemplate.getForObject(DEFAULT_ENDPOINT + "/emotions", String.class);
            JSONArray jsonArr = new JSONArray(json);

            apiResponse = jsonArr.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the medical professional should be able to obtain all emotions registered without any user identification")
    public void theMedicalProfessionalShouldBeAbleToObtainAllEmotionsRegisteredWithoutAnyUserIdentification() {
        System.out.println("/emotions: "+apiResponse);
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
            String json = testRestTemplate.getForObject(DEFAULT_ENDPOINT + "/useremotions?id=" + patientName, String.class);
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
        System.out.println("/useremotions{"+patientID+"}: "+apiResponse);
    }

    @Then("the medical professional should be able to access the latest reports of the patient using the mobile app")
    public void theMedicalProfessionalShouldBeAbleToAccessTheLatestReportsOfThePatientUsingTheMobileApp() {
    }
}
