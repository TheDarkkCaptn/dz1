package service;

import model.Ticket;
import model.TicketStatus;

public class TicketService {
    private final NotificationService notificationService;

    public TicketService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void startTicket(Ticket ticket) {
        ticket.startProcessing();
        if (ticket.getStatus() == TicketStatus.IN_PROGRESS) {
            notificationService.send(
                    "Заявка №" + ticket.getId() + " принята в работу"
            );
        }
    }
}