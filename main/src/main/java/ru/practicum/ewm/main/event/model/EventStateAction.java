package ru.practicum.ewm.main.event.model;

import ru.practicum.ewm.main.exceptions.EventUpdateException;

public enum EventStateAction {
    SEND_TO_REVIEW,
    CANCEL_REVIEW,
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static  EventState getState(String stateAction) {
        try {
            switch (valueOf(stateAction)) {
                case SEND_TO_REVIEW:
                    return EventState.PENDING;
                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    return EventState.CANCELED;
                case PUBLISH_EVENT:
                    return EventState.PUBLISHED;
                default:
                    throw new EventUpdateException("Событие не опубликовано");
            }
        } catch (IllegalArgumentException e) {
            throw new EventUpdateException("Событие не опубликовано");
        }
    }
}
