package ru.sbtqa.tag.pagefactory.aspects;

import java.net.URI;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class UpdateGlueAspect {

    @Pointcut("execution(* cucumber.runtime.RuntimeOptions.getGlue())")
    public void getGlue() {
    }

    @Around("getGlue()")
    public List<URI> getGlue(ProceedingJoinPoint joinPoint) throws Throwable {
        List<URI> uris = (List<URI>) joinPoint.proceed();
        URI transformerUri = new URI("classpath:ru/sbtqa/tag/pagefactory/transformer");
        if (!uris.contains(transformerUri)) {
            uris.add(transformerUri);
        }
        return uris;
    }
}
