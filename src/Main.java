import java.util.List;
import model.Customer;
import model.SupportAgent;
import model.Ticket;
import repository.TicketRepository;
import service.ConsoleNotificationService;
import service.NotificationService;
import service.TicketService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== HELP DESK ===");

        // 1. Клиент
        Customer customer = new Customer(1, "Анна Петрова", "anna@mail.ru");

        // 2. Специалист поддержки
        SupportAgent agent = new SupportAgent(2, "Сергей Иванов", "sergey@helpdesk.ru");

        // 3. Заявка
        Ticket ticket = new Ticket(
                1,
                "Не работает Wi-Fi",
                "Ноутбук не подключается к беспроводной сети"
        );

        System.out.println("Клиент: " + customer.getName());
        System.out.println("Специалист: " + agent.getName());

        // 4. Начальный статус
        System.out.println("Заявка #" + ticket.getId() + ": "
                + ticket.getTitle() + " | " + ticket.getStatus());

        // Сервис уведомлений и сервис заявок
        NotificationService notificationService = new ConsoleNotificationService();
        TicketService ticketService = new TicketService(notificationService);

        // 5. В работу
        ticketService.startTicket(ticket);
        System.out.println("Статус: " + ticket.getStatus());

        // 6. Решена
        ticketService.resolveTicket(ticket);
        System.out.println("Статус: " + ticket.getStatus());

        // 7. Закрыта
        ticketService.closeTicket(ticket);
        System.out.println("Статус: " + ticket.getStatus());

        // 9. В репозиторий
        TicketRepository repository = new TicketRepository();
        repository.add(ticket);

        // 10. Полный список
        System.out.println("\nВсе заявки:");
        List<Ticket> all = repository.findAll();
        for (Ticket t : all) {
            System.out.println("#" + t.getId() + " " + t.getTitle()
                    + " | " + t.getStatus());
        }
    }
}
