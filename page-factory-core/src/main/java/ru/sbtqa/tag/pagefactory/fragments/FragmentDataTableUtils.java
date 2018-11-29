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

class FragmentDataTableUtils {

    private static final int HEADER_INDEX = 0;
    private static final int FIRST_ROW_INDEX = 1;

    private FragmentDataTableUtils() {}

    static List<Map<String, String>> getDataTable(Step step) {
        List<Map<String, String>> dataTableAsListOfMaps = new ArrayList<>();

        Node argument = step.getArgument();
        if (!(argument instanceof DataTable)) {
            return dataTableAsListOfMaps;
        }

        DataTable dataTable = (DataTable) step.getArgument();

        for (int i = FIRST_ROW_INDEX; i < dataTable.getRows().size(); i++) {
            Map<String, String> dataTableRow = new HashMap<>();

            List<TableRow> rows = dataTable.getRows();
            for (int j = 0; j < rows.get(HEADER_INDEX).getCells().size(); j++) {
                String key = rows.get(HEADER_INDEX).getCells().get(j).getValue();

                List<TableCell> cells = dataTable.getRows().get(i).getCells();
                String value = cells.get(j).getValue();

                dataTableRow.put(key, value);
            }

            dataTableAsListOfMaps.add(dataTableRow);
        }

        return dataTableAsListOfMaps;
    }

    static String applyToText(Map<String, String> dataTableAsMap, String fragmentStepText) {
        String textToReplace = fragmentStepText;
        for (Map.Entry<String, String> row : dataTableAsMap.entrySet()) {
            String keyToSearch = String.format("<%s>", row.getKey());
            textToReplace = textToReplace.replace(keyToSearch, row.getValue());
        }
        return textToReplace;
    }

    static DataTable applyToArgument(Map<String, String> dataTableAsMap, Step fragmentStep) {
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
