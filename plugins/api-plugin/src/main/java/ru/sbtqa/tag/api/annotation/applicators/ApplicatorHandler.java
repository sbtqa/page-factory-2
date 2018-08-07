package ru.sbtqa.tag.api.annotation.applicators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import ru.sbtqa.tag.api.exception.ApiException;

public class ApplicatorHandler<T extends Applicator> {

    private List<T> applicators = new ArrayList<>();

    public void add(T applicator) {
        applicators.add(applicator);
    }

    public void apply() {

        //TODO вынести в метод
         applicators = applicators.stream()
                .sorted(Comparator.comparing(applicator -> {
                    if(applicator.getClass().getAnnotation(Order.class) != null) {
                        return applicator.getClass().getAnnotation(Order.class).value();
                    } else {
                        try {
                            return (int) Order.class.getDeclaredMethod("value").getDefaultValue();
                        } catch (NoSuchMethodException e) {
                            throw new ApiException("asd");
                        }
                    }


                }))
                .collect(Collectors.toList());

        for (T applicator : applicators) {
            applicator.apply();
        }
        applicators.clear();
    }
}
