import java.util.ArrayList;
import java.util.List;
import model.Administrator;
import model.Customer;
import model.SupportAgent;
import model.Ticket;
import model.User;
import repository.TicketRepository;
import service.ConsoleNotificationService;
import service.NotificationService;
import service.TicketService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== HELP DESK ===");

        TicketRepository repository = new TicketRepository();
        repository.add(new Ticket(
                1,
                "Не работает интернет",
                "После перезагрузки компьютера пропало подключение"
        ));
        repository.add(new Ticket(
                2,
                "Ошибка приложения",
                "При запуске возникает ошибка"
        ));
        repository.add(new Ticket(
                3,
                "Не печатает принтер",
                "Принтер не отвечает"
        ));

        System.out.println("\nСписок заявок:");
        for (Ticket ticket : repository.findAll()) {
            System.out.println("#" + ticket.getId() + " " + ticket.getTitle()
                    + " | " + ticket.getStatus());
        }

        System.out.println("\nПользователи и полиморфизм:");
        List<User> users = new ArrayList<>();
        users.add(new Customer(1, "Анна", "anna@mail.ru"));
        users.add(new SupportAgent(2, "Сергей", "sergey@helpdesk.ru"));
        users.add(new Administrator(3, "Олег", "admin@helpdesk.ru"));

        for (User user : users) {
            user.performAction();
        }

        System.out.println("\nРабота с заявкой через TicketService:");
        NotificationService notificationService = new ConsoleNotificationService();
        TicketService ticketService = new TicketService(notificationService);

        Ticket ticket = repository.findAll().get(0);
        ticketService.startTicket(ticket);
        ticket.resolve();
        ticket.close();

        System.out.println("Статус заявки #" + ticket.getId() + ": " + ticket.getStatus());

        System.out.println("\nПроверка запрещённых переходов:");
        Ticket another = repository.findAll().get(1);
        another.resolve();
        another.close();
    }
}