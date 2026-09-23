package service;

import model.TicketPriority;

public class DefaultPriorityCalculator implements PriorityCalculator {
    @Override
    public TicketPriority calculate(String title, String description) {
        String text = (title + " " + description).toLowerCase();
        if (text.contains("авария") || text.contains("критич")) {
            return TicketPriority.CRITICAL;
        }
        if (text.contains("срочно") || text.contains("wi-fi") || text.contains("интернет")) {
            return TicketPriority.HIGH;
        }
        if (text.contains("ошибка") || text.contains("принтер")) {
            return TicketPriority.MEDIUM;
        }
        return TicketPriority.LOW;
    }
}