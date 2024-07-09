package de.alicetools;

import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.BpmnError;
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
        } else if (moneyDouble >= 50) {
            ticketType = "Second Class";
        } else {
            throw new BpmnError("TooLessMoney", "With too less money you can't get a ticket!");
        }

        delegateExecution.setVariable("ticketType", ticketType);
    }
}
