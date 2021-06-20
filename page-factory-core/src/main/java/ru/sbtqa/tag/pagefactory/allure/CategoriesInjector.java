package ru.sbtqa.tag.pagefactory.allure;

import gherkin.deps.com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility to inject allure categories at runtime.
 * <p>
 * It applies the only unique category names. If categories are predefined by user, don't override it.
 */
public class CategoriesInjector {
    private static final Logger LOG = LoggerFactory.getLogger(CategoriesInjector.class);
    private static final List<Category> updatedCategories = Collections.synchronizedList(new ArrayList<>());

    private CategoriesInjector() {
    }

    /**
     * Inject categories.
     * <p>
     * Don't care about categories duplication, it resolves automatically as fast as it can.
     *
     * @param category categories to inject
     */
    public static void inject(Category... category) {
        synchronized (updatedCategories) {
            List<Category> categoryList = Arrays.stream(category)
                    .filter(categoryToUpdate -> !updatedCategories.contains(categoryToUpdate))
                    .collect(Collectors.toList());
            try {
                applyUserCategories(categoryList);
                updatedCategories.addAll(categoryList);
            } catch (IOException e) {
                LOG.warn("Cannot inject categories", e);
            }
        }
    }

    private static synchronized void applyUserCategories(List<Category> categories) throws IOException {
        Gson gson = new Gson();
        List<Category> userCategories = new ArrayList<>();
        InputStream in = CategoriesInjector.class.getClassLoader()
                .getResourceAsStream("categories.json");
        if (in != null) {
            InputStreamReader reader = new InputStreamReader(in);
            userCategories.addAll(Arrays.asList(gson.fromJson(reader, Category[].class)));
        }

        List<Category> injectableCategories = categories.stream()
                .filter(category -> userCategories.stream()
                        .noneMatch(userCategory -> userCategory.getName().equals(category.getName())))
                .collect(Collectors.toList());
        userCategories.addAll(injectableCategories);

        Category[] categoriesArray = userCategories.toArray(new Category[]{});
        String categoriesJson = gson.toJson(categoriesArray, Category[].class);
        File categoriesFile = new File(CategoriesInjector.class.getClassLoader()
                .getResource("").getPath() + "/categories.json");

        FileUtils.writeStringToFile(categoriesFile, categoriesJson, Charset.forName("UTF-8"));
    }
}
