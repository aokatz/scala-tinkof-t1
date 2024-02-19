package org.tinkoff.task.one.service;

import org.tinkoff.task.one.model.Response;

public interface Client {
    //блокирующий вызов сервиса 1 для получения статуса заявки
    Response getApplicationStatus1(String id);

    //блокирующий вызов сервиса 2 для получения статуса заявки
    Response getApplicationStatus2(String id);

}
