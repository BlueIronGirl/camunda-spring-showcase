package om.camunda.academy;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;

/**
 * Hello world!
 */
public class PaymentApplication {
    private static final String ZEEBE_ADDRESS = "1ac87d10-ca8d-4b46-b882-c802009f94b6.bru-2.zeebe.camunda.io:443";
    private static final String ZEEBE_CLIENT_ID = "p0ox9H_..PeKJBqk-tzjnc9HKYKyb1Jj";
    private static final String ZEEBE_CLIENT_SECRET = "5dEFGoq5v9wjUcQDizhjrYrj9dtleJX~4-_aZWsQ8YjMWd6Qz1turBPzW8xqJ5xk";
    private static final String ZEEBE_AUTHORIZATION_SERVER_URL = "https://login.cloud.camunda.io/oauth/token";
    private static final String ZEEBE_TOKEN_AUDIENCE = "zeebe.camunda.io";

    public static void main(String[] args) {
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
            System.out.println("Connected to: " + client.newTopologyRequest().send().join());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
