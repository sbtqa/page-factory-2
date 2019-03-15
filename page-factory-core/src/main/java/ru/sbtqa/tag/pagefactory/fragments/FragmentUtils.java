package ru.sbtqa.tag.pagefactory.fragments;

import gherkin.ast.Step;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.qautils.i18n.I18N;
import ru.sbtqa.tag.stepdefs.CoreGenericSteps;

class FragmentUtils {

    private static final String FRAGMENT_STEP_REGEX_KEY = "ru.sbtqa.tag.pagefactory.insertFragment";

    private FragmentUtils() {}

    /**
     * Find the name of the scenario (fragment) to substitute for this step
     *
     * @param step step to substitute
     * @param language step's language
     * @return name of the scenario (fragment) to substitute
     */
    static String getFragmentName(Step step, String language) throws FragmentException {
        Map<String, String> props = getFragmentStepRegex(language);
        for (Map.Entry<String, String> entry : props.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.startsWith(FRAGMENT_STEP_REGEX_KEY)) {
                Pattern pattern = Pattern.compile(value);
                Matcher matcher = pattern.matcher(step.getText());
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        }
        throw new FragmentException("Fragment name not found");
    }

    /**
     * Check the step for the need to replace the fragment
     *
     * @param step step to check
     * @param language step's language
     * @return returns true if the step needs to be replaced with a fragment
     */
    static boolean isStepFragmentRequire(Step step, String language) {
        Map<String, String> props = getFragmentStepRegex(language);

        for (Map.Entry<String, String> entry : props.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.startsWith(FRAGMENT_STEP_REGEX_KEY) && Pattern.matches(value, step.getText())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get regex of steps in need of replacement. Language required
     *
     * @param language language of regex
     * @return regex of steps in need of replacement
     */
    private static Map<String, String> getFragmentStepRegex(String language) {
         return I18N.getI18n(CoreGenericSteps.class, new Locale(language)).toMap();
    }
}
