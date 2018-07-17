package ru.sbtqa.tag.pagefactory.fragments;

import gherkin.ast.DataTable;
import gherkin.ast.Node;
import gherkin.ast.Step;
import gherkin.ast.TableCell;
import gherkin.ast.TableRow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.reflect.FieldUtils;

public class FragmentDataTableUtils {

    private static final int HEADER_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    private FragmentDataTableUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void applyDataTable(Step step, Step fragmentStep) throws IllegalAccessException {
        Map<String, String> dataTableAsMap = getDataTableAsMap(step);

        // apply datatable parameters to fragment step body
        FieldUtils.writeField(fragmentStep, "text", applyToText(dataTableAsMap, fragmentStep.getText()), true);

        // apply datatable parameters to fragment step argument data table
        if (fragmentStep.getArgument() != null) {
            FieldUtils.writeField(fragmentStep, "argument", applyToArgument(dataTableAsMap, fragmentStep), true);
        }
    }

    private static Map<String, String> getDataTableAsMap(Step step) {
        Map<String, String> dataTableAsMap = new HashMap<>();

        Node argument = step.getArgument();
        if (!(argument instanceof DataTable)) {
            return dataTableAsMap;
        }

        DataTable dataTable = (DataTable) step.getArgument();
        for (int i = 0; i < dataTable.getRows().get(HEADER_INDEX).getCells().size(); i++) {
            String key = dataTable.getRows().get(HEADER_INDEX).getCells().get(i).getValue();
            String value = dataTable.getRows().get(VALUE_INDEX).getCells().get(i).getValue();

            dataTableAsMap.put(key, value);
        }

        return dataTableAsMap;
    }

    private static String applyToText(Map<String, String> dataTableAsMap, String fragmentStepText) {
        String textToReplace = fragmentStepText;
        for (Map.Entry<String, String> row : dataTableAsMap.entrySet()) {
            String keyToSearch = String.format("<%s>", row.getKey());
            textToReplace = textToReplace.replaceAll(keyToSearch, row.getValue());
        }
        return textToReplace;
    }

    private static DataTable applyToArgument(Map<String, String> dataTableAsMap, Step fragmentStep) {
        DataTable dataTable = (DataTable) fragmentStep.getArgument();

        List<TableRow> resultTableRows = new ArrayList<>();
        for (TableRow row : dataTable.getRows()) {
            List<TableCell> resultCells = new ArrayList<>();
            for (TableCell cell : row.getCells()) {
                TableCell resultCell = new TableCell(cell.getLocation(), applyToText(dataTableAsMap, cell.getValue()));
                resultCells.add(resultCell);
            }
            resultTableRows.add(new TableRow(row.getLocation(), resultCells));
        }

        return new DataTable(resultTableRows);
    }
}
