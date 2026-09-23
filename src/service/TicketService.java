package service;

import model.Ticket;

public class TicketService {
    private final NotificationService notificationService;
    private final PriorityCalculator priorityCalculator;

    public TicketService(NotificationService notificationService,
                         PriorityCalculator priorityCalculator) {
        this.notificationService = notificationService;
        this.priorityCalculator = priorityCalculator;
    }

    public void startTicket(Ticket ticket) {
        ticket.startProcessing();
        notificationService.send("Заявка №" + ticket.getId() + " принята в работу");
    }

    public void resolveTicket(Ticket ticket) {
        ticket.resolve();
        notificationService.send("По заявке №" + ticket.getId() + " найдено решение");
    }

    public void closeTicket(Ticket ticket) {
        ticket.close();
        notificationService.send("Заявка №" + ticket.getId() + " закрыта");
    }

    public void cancelTicket(Ticket ticket) {
        ticket.cancel();
        notificationService.send("Заявка №" + ticket.getId() + " отменена");
    }

    public PriorityCalculator getPriorityCalculator() {
        return priorityCalculator;
    }
}