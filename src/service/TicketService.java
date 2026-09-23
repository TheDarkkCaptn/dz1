package service;

import model.NotificationService;
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

    public void resolveTicket(Ticket ticket) {
        ticket.resolve();
        if (ticket.getStatus() == TicketStatus.RESOLVED) {
            notificationService.send(
                    "По заявке №" + ticket.getId() + " найдено решение"
            );
        }
    }

    public void closeTicket(Ticket ticket) {
        ticket.close();
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            notificationService.send(
                    "Заявка №" + ticket.getId() + " закрыта"
            );
        }
    }
}
