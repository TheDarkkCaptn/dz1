package model;

import java.time.LocalDateTime;

public record StatusChange(TicketStatus from, TicketStatus to, LocalDateTime timestamp) {
    @Override
    public String toString() {
        return from + " -> " + to + " (" + timestamp + ")";
    }
}