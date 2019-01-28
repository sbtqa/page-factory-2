package ru.sbtqa.tag.mqfactory.interfaces;

import ru.sbtqa.tag.mqfactory.exception.JmsException;


public interface JmsUpdater<T> {

    T update(T message) throws JmsException;

}
