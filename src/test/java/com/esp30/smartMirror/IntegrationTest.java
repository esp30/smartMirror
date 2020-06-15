package com.esp30.smartMirror;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@EmbeddedKafka(partitions = 1, topics = { "p30-classification" }, brokerProperties = "auto.create.topics.enable=true")
@SpringBootTest
public class IntegrationTest {

    private static final String TEST_TOPIC = "p30-classification";
    private static final String DEFAULT_ENDPOINT = "https://192.168.160.103:30043";

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    public void testReceivingKafkaEvents() {
        Consumer<String, String> consumer = configureConsumer();
        Producer<String, String> producer = configureProducer();

        producer.send(new ProducerRecord<>(TEST_TOPIC, "123", "my-test-value"));

        ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TEST_TOPIC);
        assert (singleRecord != null);
        assert (singleRecord.key().equals("123"));
        assert (singleRecord.value()).equals("my-test-value");

        consumer.close();
        producer.close();
    }

    private Consumer<String, String> configureConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps)
                .createConsumer();
        consumer.subscribe(Collections.singleton(TEST_TOPIC));
        return consumer;
    }

    private Producer<String, String> configureProducer() {
        Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        return new DefaultKafkaProducerFactory<String, String>(producerProps).createProducer();
    }

    @Test
    public void integrationTest() { 
        int[] initialValue = new int[2];
        int[] finalValue = new int[2];


        // Initial state
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
            initialValue = parseJsonData(json);

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Inject emotion
        Producer<String, String> producer = configureProducer();
        producer.send(new ProducerRecord<>(TEST_TOPIC, "value", "Happy"));

        // Final state
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
            initialValue = parseJsonData(json);

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Comparison and assertion
        assert(initialValue[0]+1==finalValue[0]);   // 1 more happy
        assert(initialValue[1]==finalValue[1]);     // same neutral
    }

    // Parsing
    private int[] parseJsonData(String jsonResponse){
        int[] retVal = {0,0}; // 0 - happy; 1 - neutral
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String emotion = jsonObject1.optString("value");
                if(emotion.equals("Happy")) {
                    retVal[0]++;
                }
                else {
                    retVal[1]++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
}
