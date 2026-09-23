package model;

import exception.InvalidTicketStateException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticket {
    private final long id;
    private final String title;
    private final String description;
    private final TicketPriority priority;
    private final LocalDateTime createdAt;
    private final LocalDateTime deadline;

    private TicketStatus status;
    private final List<StatusChange> history = new ArrayList<>();

    public Ticket(long id, String title, String description, TicketPriority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название заявки не может быть пустым");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Приоритет должен быть задан");
        }
        this.id = id;
        this.title = title;
        this.description = description == null ? "" : description;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
        this.deadline = calculateDeadline(this.createdAt, priority);
        this.status = TicketStatus.NEW;
    }

    private static LocalDateTime calculateDeadline(LocalDateTime createdAt, TicketPriority priority) {
        return switch (priority) {
            case CRITICAL -> createdAt.plusMinutes(30);
            case HIGH -> createdAt.plusHours(2);
            case MEDIUM -> createdAt.plusHours(8);
            case LOW -> createdAt.plusHours(24);
        };
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TicketPriority getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getDeadline() { return deadline; }
    public TicketStatus getStatus() { return status; }
    public List<StatusChange> getHistory() { return Collections.unmodifiableList(history); }

    public boolean isOverdue() {
        if (status == TicketStatus.RESOLVED
                || status == TicketStatus.CLOSED
                || status == TicketStatus.CANCELLED) {
            return false;
        }
        return LocalDateTime.now().isAfter(deadline);
    }

    private void changeStatus(TicketStatus newStatus) {
        history.add(new StatusChange(status, newStatus, LocalDateTime.now()));
        status = newStatus;
    }

    public void startProcessing() {
        if (status != TicketStatus.NEW) {
            throw new InvalidTicketStateException("В работу можно взять только новую заявку");
        }
        changeStatus(TicketStatus.IN_PROGRESS);
    }

    public void resolve() {
        if (status != TicketStatus.IN_PROGRESS) {
            throw new InvalidTicketStateException("Решить можно только заявку в работе");
        }
        changeStatus(TicketStatus.RESOLVED);
    }

    public void close() {
        if (status != TicketStatus.RESOLVED) {
            throw new InvalidTicketStateException("Закрыть можно только решённую заявку");
        }
        changeStatus(TicketStatus.CLOSED);
    }

    public void cancel() {
        if (status == TicketStatus.CLOSED) {
            throw new InvalidTicketStateException("Нельзя отменить закрытую заявку");
        }
        if (status == TicketStatus.CANCELLED) {
            throw new InvalidTicketStateException("Заявка уже отменена");
        }
        changeStatus(TicketStatus.CANCELLED);
    }

    @Override
    public String toString() {
        return "#" + id + " " + title + " | " + status + " | " + priority
                + " | создана: " + createdAt;
    }
}