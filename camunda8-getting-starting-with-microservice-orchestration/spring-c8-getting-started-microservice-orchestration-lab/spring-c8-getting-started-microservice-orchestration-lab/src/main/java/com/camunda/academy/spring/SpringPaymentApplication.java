package com.camunda.academy.spring;

import com.camunda.academy.spring.handler.SpringCreditCardServiceHandler;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringPaymentApplication {
    private static final String ZEEBE_ADDRESS = "1ac87d10-ca8d-4b46-b882-c802009f94b6.bru-2.zeebe.camunda.io:443";
    private static final String ZEEBE_CLIENT_ID = "p0ox9H_..PeKJBqk-tzjnc9HKYKyb1Jj";
    private static final String ZEEBE_CLIENT_SECRET = "5dEFGoq5v9wjUcQDizhjrYrj9dtleJX~4-_aZWsQ8YjMWd6Qz1turBPzW8xqJ5xk";
    private static final String ZEEBE_AUTHORIZATION_SERVER_URL = "https://login.cloud.camunda.io/oauth/token";
    private static final String ZEEBE_TOKEN_AUDIENCE = "zeebe.camunda.io";

    public static void main(String[] args) {
        SpringApplication.run(SpringPaymentApplication.class, args);

        final OAuthCredentialsProvider credentialsProvider = new OAuthCredentialsProviderBuilder()
                .authorizationServerUrl(ZEEBE_AUTHORIZATION_SERVER_URL)
                .audience(ZEEBE_TOKEN_AUDIENCE)
                .clientId(ZEEBE_CLIENT_ID)
                .clientSecret(ZEEBE_CLIENT_SECRET)
                .build();

        try (final ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress(ZEEBE_ADDRESS)
                .credentialsProvider(credentialsProvider)
                .build()) {

            //Request the Cluster Topology
            System.out.println("Connected to: " + client.newTopologyRequest().send().join());

            final Map<String, Object> startProcessVariables = new HashMap<>();
            startProcessVariables.put("reference", "C8_12345");
            startProcessVariables.put("amount", Double.valueOf(100.00));
            startProcessVariables.put("cardNumber", "1234567812345678");
            startProcessVariables.put("cardExpiry", "12/2023");
            startProcessVariables.put("cardCVC", "123");

            // Launch the Process Instance
            client.newCreateInstanceCommand()
                    .bpmnProcessId("paymentProcess")
                    .latestVersion()
                    .variables(startProcessVariables)
                    .send()
                    .join();

            // Start a Job Worker
            client.newWorker()
                    .jobType("chargeCreditCard")
                    .handler(new SpringCreditCardServiceHandler())
                    .timeout(Duration.ofSeconds(10).toMillis())
                    .open();

            // Wait for the workers
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
