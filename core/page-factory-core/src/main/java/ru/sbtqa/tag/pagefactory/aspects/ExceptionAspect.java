package ru.sbtqa.tag.pagefactory.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.openqa.selenium.NoSuchElementException;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Aspect
/**
 * Aspect for translation an Exceptions to AssertationError errors <br/>
 * to split infrastructure errors from functional errors
 *
 *
 * @version $Id: $Id
 */
public class ExceptionAspect {

    static long lastFailureTimestamp = 0;

    /**
     * <p>
     * translateException.</p>
     *
     * @param joinPoint a {@link org.aspectj.lang.ProceedingJoinPoint} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Throwable if any.
     */
    @Around("execution(* *..*(..)) && within(ru.sbtqa.tag.*) && !within(ru.sbtqa.tag.bdd.util.Allure*) && !within(ru.sbtqa.tag.bdd.util.Allure*)")
    public Object translateException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Exception | AssertionError e) {
            //Add exceptions filter divided by || that are functional
            if ((e instanceof NoSuchElementException || e instanceof NullPointerException)
                    && null != PageContext.getCurrentPage()) {
                throw new AutotestError(getErrorText(e.getMessage()), e);
            } else {
                throw e;
            }
        }
    }

    private String getErrorText(String throwMessage) throws PageInitializationException, IllegalArgumentException, IllegalAccessException {
        String errorText = "";

        Field[] fields = PageContext.getCurrentPage().getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            Object currentObject = null;
            if (PageContext.getCurrentPage() != null) {
                currentObject = field.get(PageContext.getCurrentPage());
            }

            if (null != currentObject && throwMessage.contains(field.getName())) {
                for (Annotation annotation : field.getAnnotations()) {
                    if (annotation instanceof ElementTitle) {
                        errorText = "There is no element with title == " + ((ElementTitle) annotation).value();
                        break;
                    }
                }
            }
        }

        return errorText;
    }

}
