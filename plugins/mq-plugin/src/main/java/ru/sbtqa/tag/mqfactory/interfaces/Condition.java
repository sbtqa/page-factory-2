package ru.sbtqa.tag.mqfactory.interfaces;

/**
 * @author Vadim Trofimov
 * @since 10.01.2018
 */
public interface Condition<T> {

    boolean isSupply(T message);

}
