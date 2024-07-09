package com.camunda.academy.spring.handler;

import com.camunda.academy.spring.service.SpringCreditCardService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

import java.util.HashMap;
import java.util.Map;

public class SpringCreditCardServiceHandler implements JobHandler {
    private SpringCreditCardService creditCardService = new SpringCreditCardService();

    @Override
    public void handle(JobClient client, ActivatedJob job) {
        // Obtain the Process variables
        final Map<String, Object> inputVariables = job.getVariablesAsMap();
        final String reference = (String) inputVariables.get("reference");
        final Double amount = (Double) inputVariables.get("amount");
        final String cardNumber = (String) inputVariables.get("cardNumber");
        final String cardExpiry = (String) inputVariables.get("cardExpiry");
        final String cardCVC = (String) inputVariables.get("cardCVC");

        // Charge the Credit Card
        final String confirmation = creditCardService.chargeCreditCard(reference, amount, cardNumber, cardExpiry, cardCVC);

        // Build the Output Process Variables
        final Map<String, Object> outputVariables = new HashMap<>();
        outputVariables.put("confirmation", confirmation);

        // Complete the Job
        client.newCompleteCommand(job.getKey()).variables(outputVariables).send().join();
    }
}
