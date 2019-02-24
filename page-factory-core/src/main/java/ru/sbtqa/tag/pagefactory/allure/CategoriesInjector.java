package ru.sbtqa.tag.pagefactory.allure;

import gherkin.deps.com.google.gson.Gson;
import io.qameta.allure.model.Status;
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
import java.util.List;

public class CategoriesInjector {
    private static final Logger LOG = LoggerFactory.getLogger(CategoriesInjector.class);
    private static boolean isCategoriesUpdated = false;

    public static synchronized void inject() {
        if (!isCategoriesUpdated) {
            try {
                getUserCategories();
                isCategoriesUpdated = true;
            } catch (IOException e) {
                LOG.warn("Cannot inject categories", e);
            }
        }
    }

    private static synchronized void getUserCategories() throws IOException {
        Gson gson = new Gson();
        List<Category> categories = new ArrayList<>();
        InputStream in = CategoriesInjector.class.getClassLoader()
                .getResourceAsStream("categories.json");
        if (in != null) {
            InputStreamReader reader = new InputStreamReader(in);
            categories.addAll(Arrays.asList(gson.fromJson(reader, Category[].class)));
        }

        if (!categories.stream().anyMatch(category -> category.getName().equals("Non-critical failures"))) {
            List<String> matchedStatuses = new ArrayList<>();
            matchedStatuses.add(Status.PASSED.value());
            Category nonCriticalCategory = new Category("Non-critical failures", null, matchedStatuses);

            categories.add(nonCriticalCategory);
            Category[] categoriesArray = categories.toArray(new Category[categories.size()]);
            String categoriesJson = gson.toJson(categoriesArray, Category[].class);
            File categoriesFile = new File(CategoriesInjector.class.getClassLoader()
                    .getResource("").getPath() + "/categories.json");
            FileUtils.writeStringToFile(categoriesFile, categoriesJson, Charset.forName("UTF-8"));
        }
    }
}
