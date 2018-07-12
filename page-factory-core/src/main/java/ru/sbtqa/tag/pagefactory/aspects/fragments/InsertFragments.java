package ru.sbtqa.tag.pagefactory.aspects.fragments;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class InsertFragments {

    @Pointcut("call(* *.addChildren(..))")
    public void click() {
    }

    @Before("click()")
    public void stash() {
        System.out.println("WWWWWWW!");
    }
}
