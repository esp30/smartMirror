package com.esp30.controllers;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AppController {
    
    static final String DEFAULT_ENDPOINT = "https://192.168.160.103:30043";
    
    @GetMapping("/stats")
    public ModelAndView showStats(Model model) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, JSONException{
        System.out.println("Fetching data frm emotions!");
            
        // Lixo para ignorar certificados =)
        TrustStrategy acceptingTrustStrategy = new TrustSelfSignedStrategy();

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
        .loadTrustMaterial(null, acceptingTrustStrategy)
        .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();   

        requestFactory.setHttpClient(httpClient);
        
        // Call ao endpoint
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String json = restTemplate.getForObject(DEFAULT_ENDPOINT+"/emotions", String.class);
        JSONArray jsonArr = new JSONArray(json);
        
        int happyCount = 0;
        int neutralCount = 0;
        for(int i = 0; i < jsonArr.length(); i++){
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            if(((String)jsonObj.get("value")).equals("Neutral")){
                neutralCount++;
            }else{
                happyCount++;
            }
            System.out.println();   
        }
        model.addAttribute("happy_count", happyCount);
        model.addAttribute("neutral_count", neutralCount);
        
        json = restTemplate.getForObject(DEFAULT_ENDPOINT+"/users", String.class);
        JSONArray jsonArrUsers = new JSONArray(json);
        String[] loggedUsers = new String[jsonArrUsers.length()];
        double avgUserAge = 0;
        for(int i = 0; i < jsonArrUsers.length(); i++){
            JSONObject jsonObj = jsonArrUsers.getJSONObject(i);
            System.out.print(jsonObj);
            loggedUsers[i] = (String)jsonObj.get("name");
            avgUserAge += (int)jsonObj.get("age");
        }
        avgUserAge = avgUserAge / jsonArrUsers.length();
        
        model.addAttribute("logged_users", loggedUsers);
        model.addAttribute("avg_age", (int)avgUserAge);

        return new ModelAndView("stats", "command", model);
    }
}
