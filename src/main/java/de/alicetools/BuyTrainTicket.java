package de.alicetools;

import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Named
public class BuyTrainTicket implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        String money = delegateExecution.getVariable("money").toString();
        double moneyDouble = Double.parseDouble(money);

        String ticketType = "";
        if (moneyDouble >= 1000) {
            ticketType = "First Class";
        } else {
            ticketType = "Second Class";
        }

        delegateExecution.setVariable("ticketType", ticketType);
    }
}
