import exception.InvalidTicketStateException;
import exception.TicketNotFoundException;
import java.util.List;
import java.util.Map;
import model.Administrator;
import model.Customer;
import model.SupportAgent;
import model.Ticket;
import model.TicketPriority;
import model.TicketStatus;
import model.User;
import repository.TicketRepository;
import service.ConsoleNotificationService;
import service.DefaultPriorityCalculator;
import service.EmailNotificationService;
import service.NotificationService;
import service.PriorityCalculator;
import service.TelegramNotificationService;
import service.TicketService;
import service.VipPriorityCalculator;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== HELP DESK ===");

        Customer anna = new Customer(1, "Анна Петрова", "anna@mail.ru");
        SupportAgent sergey = new SupportAgent(2, "Сергей Иванов", "sergey@helpdesk.ru");
        Administrator oleg = new Administrator(3, "Олег", "admin@helpdesk.ru");

        System.out.println("\nПользователи:");
        for (User user : List.of(anna, sergey, oleg)) {
            user.performAction();
        }

        TicketRepository repository = new TicketRepository();
        NotificationService consoleNotification = new ConsoleNotificationService();
        PriorityCalculator defaultCalc = new DefaultPriorityCalculator();
        TicketService ticketService = new TicketService(consoleNotification, defaultCalc);

        Ticket t1 = new Ticket(1, "Не работает Wi-Fi",
                "Срочно, ноутбук не подключается к беспроводной сети",
                TicketPriority.HIGH);
        Ticket t2 = new Ticket(2, "Ошибка приложения",
                "При запуске возникает ошибка",
                TicketPriority.MEDIUM);
        Ticket t3 = new Ticket(3, "Не печатает принтер",
                "Принтер не отвечает",
                TicketPriority.LOW);

        repository.add(t1);
        repository.add(t2);
        repository.add(t3);

        System.out.println("\nВсе заявки (toString):");
        for (Ticket t : repository.findAll()) {
            System.out.println(t);
        }

        System.out.println("\n--- Жизненный цикл заявки #1 ---");
        ticketService.startTicket(t1);
        System.out.println("Статус: " + t1.getStatus());
        ticketService.resolveTicket(t1);
        System.out.println("Статус: " + t1.getStatus());
        ticketService.closeTicket(t1);
        System.out.println("Статус: " + t1.getStatus());

        System.out.println("\n--- Назначение заявки #2 агенту Сергею ---");
        sergey.assignTicket(t2);
        System.out.println("Статус заявки #2: " + t2.getStatus());

        System.out.println("\n--- Отмена заявки #3 ---");
        ticketService.cancelTicket(t3);
        System.out.println("Статус заявки #3: " + t3.getStatus());

        System.out.println("\n--- История изменений заявки #1 ---");
        t1.getHistory().forEach(System.out::println);

        System.out.println("\n--- Поиск по id ---");
        repository.findById(2).ifPresentOrElse(
                t -> System.out.println("Найдена: " + t),
                () -> System.out.println("Заявка не найдена")
        );

        try {
            Ticket notFound = repository.findById(999).orElseThrow(
                    () -> new TicketNotFoundException("Заявка с id=999 не найдена")
            );
            System.out.println(notFound);
        } catch (TicketNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nЗаявок NEW: "
                + repository.findByStatus(TicketStatus.NEW).size());
        System.out.println("Заявок с приоритетом HIGH: "
                + repository.findByPriority(TicketPriority.HIGH).size());

        System.out.println("\n--- Статистика по статусам ---");
        Map<TicketStatus, Long> stats = repository.getStatistics();
        for (Map.Entry<TicketStatus, Long> entry : stats.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\n--- Просроченные заявки ---");
        List<Ticket> overdue = repository.findOverdue();
        if (overdue.isEmpty()) {
            System.out.println("Нет просроченных заявок");
        } else {
            overdue.forEach(System.out::println);
        }

        System.out.println("\n--- Запрещённые переходы (исключения) ---");
        try {
            t1.startProcessing(); // уже CLOSED
        } catch (InvalidTicketStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            new Ticket(4, "   ", "пустое название", TicketPriority.LOW);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }

        System.out.println("\n--- Смена реализации уведомлений (полиморфизм) ---");
        Ticket t4 = new Ticket(4, "Тест уведомлений", "проверка", TicketPriority.LOW);
        repository.add(t4);

        TicketService emailService =
                new TicketService(new EmailNotificationService(), defaultCalc);
        emailService.startTicket(t4);

        TicketService telegramService =
                new TicketService(new TelegramNotificationService(), defaultCalc);
        telegramService.resolveTicket(t4);

        System.out.println("\n--- PriorityCalculator (Strategy) ---");
        PriorityCalculator vipCalc = new VipPriorityCalculator();
        System.out.println("Default для 'авария': "
                + defaultCalc.calculate("авария", "всё упало"));
        System.out.println("VIP для 'авария': "
                + vipCalc.calculate("авария", "всё упало"));

        System.out.println("\n=== END ===");
    }
}