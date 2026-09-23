package repository;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.Ticket;
import model.TicketPriority;
import model.TicketStatus;

public class TicketRepository {
    private final List<Ticket> tickets = new ArrayList<>();

    public void add(Ticket ticket) {
        tickets.add(ticket);
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(tickets);
    }

    public Optional<Ticket> findById(long id) {
        return tickets.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    public List<Ticket> findByStatus(TicketStatus status) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getStatus() == status) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Ticket> findByPriority(TicketPriority priority) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getPriority() == priority) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Ticket> findOverdue() {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.isOverdue()) {
                result.add(t);
            }
        }
        return result;
    }

    public Map<TicketStatus, Long> getStatistics() {
        Map<TicketStatus, Long> stats = new EnumMap<>(TicketStatus.class);
        for (TicketStatus s : TicketStatus.values()) {
            stats.put(s, 0L);
        }
        for (Ticket t : tickets) {
            stats.merge(t.getStatus(), 1L, Long::sum);
        }
        return stats;
    }
}