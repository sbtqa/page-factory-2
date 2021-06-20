package ru.sbtqa.tag.api.annotation.applicators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import ru.sbtqa.tag.api.exception.RestPluginException;

/**
 * Queue of applicators.
 * Add, sort applicators by {@link Order} and apply it
 * @param <T> type of applicator's queue
 */
public class ApplicatorHandler<T extends Applicator> {

    private final List<T> applicators = new ArrayList<>();

    /**
     * Add applicator to queue
     * @param applicator applicator
     */
    public void add(T applicator) {
        applicators.add(applicator);
    }

    /**
     * Sort by {@link Order} and perform applicators
     */
    public void apply() {
        sort();

        for (T applicator : applicators) {
            applicator.apply();
        }

        applicators.clear();
    }

    private List<T> sort() {
        return applicators.stream()
                .sorted(Comparator.comparing(applicator -> {
                    Order order = applicator.getClass().getAnnotation(Order.class);
                    if (order != null) {
                        return order.value();
                    } else {
                        return getDefaultOrder();
                    }
                }))
                .collect(Collectors.toList());
    }

    private int getDefaultOrder() {
        try {
            return (int) Order.class.getDeclaredMethod("value").getDefaultValue();
        } catch (NoSuchMethodException e) {
            throw new RestPluginException("Default value method is not available in Order annotation");
        }
    }
}
