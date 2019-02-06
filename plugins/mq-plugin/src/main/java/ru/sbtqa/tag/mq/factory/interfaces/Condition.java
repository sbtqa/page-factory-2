package ru.sbtqa.tag.mq.factory.interfaces;


public interface Condition<T> {

    boolean isSupply(T message);

}
