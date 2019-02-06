package ru.sbtqa.tag.mq.factory.interfaces;

import ru.sbtqa.tag.mq.factory.exception.JmsException;


public interface JmsUpdater<T> {

    T update(T message) throws JmsException;

}
