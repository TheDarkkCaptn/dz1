package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SupportAgent extends User {
    private final List<Ticket> assignedTickets = new ArrayList<>();

    public SupportAgent(long id, String name, String email) {
        super(id, name, email);
    }

    public void assignTicket(Ticket ticket) {
        if (ticket.getStatus() == TicketStatus.NEW) {
            ticket.startProcessing(); // NEW -> IN_PROGRESS
        }
        assignedTickets.add(ticket);
    }

    public List<Ticket> getAssignedTickets() {
        return Collections.unmodifiableList(assignedTickets);
    }

    @Override
    public void performAction() {
        System.out.println(getName() + " обрабатывает заявку");
    }
}