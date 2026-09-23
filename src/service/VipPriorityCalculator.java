package service;

import model.TicketPriority;

public class VipPriorityCalculator implements PriorityCalculator {
    @Override
    public TicketPriority calculate(String title, String description) {
        return TicketPriority.CRITICAL;
    }
}