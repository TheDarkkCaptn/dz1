package service;

import model.TicketPriority;

public interface PriorityCalculator {
    TicketPriority calculate(String title, String description);
}