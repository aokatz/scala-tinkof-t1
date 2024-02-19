package org.tinkoff.task.one.service;

import org.tinkoff.task.one.model.ApplicationStatusResponse;

public interface Handler {
    ApplicationStatusResponse performOperation(String id);
}
