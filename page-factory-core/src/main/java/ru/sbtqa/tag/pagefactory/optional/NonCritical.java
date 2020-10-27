package ru.sbtqa.tag.pagefactory.optional;

public interface NonCritical {

    /**
     * Step is non critical, e.g.
     * When ? user (makes some checks).
     * '?' is a marker of a non critical step.
     *
     * @return true if step is non critical
     */
    boolean isNonCritical();
}
