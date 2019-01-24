package ru.sbtqa.tag.mqfactory.interfaces;

import ru.sbtqa.tag.mqfactory.exception.JmsException;

/**
 * @author Vadim Trofimov
 * @since 25.12.2017
 */
public interface JmsUpdater<T> {

    T update(T message) throws JmsException;

}
