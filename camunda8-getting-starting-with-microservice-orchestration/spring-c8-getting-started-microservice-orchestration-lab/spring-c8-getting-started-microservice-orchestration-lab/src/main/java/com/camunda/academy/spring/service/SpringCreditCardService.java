package com.camunda.academy.spring.service;

public class SpringCreditCardService {
    public String chargeCreditCard(final String reference,
                                   final Double amount,
                                   final String cardNumber,
                                   final String cardExpiryDate,
                                   final String cardCVC) {
        // Output the Process Variables
        System.out.println("Starting Transaction: " + reference);
        System.out.println("Card Number: " + cardNumber);
        System.out.println("Card Expiry Date: " + cardExpiryDate);
        System.out.println("Card CVC: " + cardCVC);
        System.out.println("Amount: " + amount);

        // Generate a Confirmation Number
        final String confirmation = String.valueOf(System.currentTimeMillis());
        System.out.println("Successful Transaction: " + confirmation);
        return confirmation;
    }
}
