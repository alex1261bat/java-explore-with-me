package ru.practicum.ewm.main.event.model;

public enum EventSort {
    EVENT_DATE,
    VIEWS;

    public static String getSortField(String sortField) {
        if (sortField != null && sortField.equals(EVENT_DATE.toString())) {
            return "eventDate";
        }

        if (sortField != null && sortField.equals(VIEWS.toString())) {
            return "views";
        }

        return "eventId";
    }
}
