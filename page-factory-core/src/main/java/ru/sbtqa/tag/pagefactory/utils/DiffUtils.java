package ru.sbtqa.tag.pagefactory.utils;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.junit.CoreStepsImpl;

public class DiffUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CoreStepsImpl.class);

    public static String diff(String string1, String string2) {
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .mergeOriginalRevised(true)
                .oldTag(f -> "|")
                .newTag(f -> "|")
                .build();

        List<DiffRow> rows = new ArrayList<>();
        try {
            rows = generator.generateDiffRows(
                    Collections.singletonList(string1),
                    Collections.singletonList(string2));
        } catch (DiffException e) {
            LOG.info("There is an error in string diff", e);
        }

        return !rows.isEmpty() ? rows.get(0).getOldLine() : "";
    }
}
