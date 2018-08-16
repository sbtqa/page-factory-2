package ru.sbtqa.tag.pagefactory.data;

import static java.lang.String.format;
import ru.sbtqa.tag.datajack.TestDataObject;
import ru.sbtqa.tag.datajack.adaptors.ExcelDataObjectAdaptor;
import ru.sbtqa.tag.datajack.adaptors.JsonDataObjectAdaptor;
import ru.sbtqa.tag.datajack.adaptors.PropertiesDataObjectAdaptor;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.qautils.properties.Props;

public class DataFactory {

    private static TestDataObject dataContainer;

    private DataFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static TestDataObject getInstance() throws DataException {
        if (dataContainer == null) {
            String initialCollection = Props.get("data.initial.collection", null);
            String dataFolder = Props.get("data.folder");
            String dataType = Props.get("data.type", "stash");

            switch (dataType) {
                case "json":
                    dataContainer = new JsonDataObjectAdaptor(
                            dataFolder,
                            initialCollection,
                            Props.get("data.extension", "json")
                    );
                    break;
                case "properties":
                    dataContainer = new PropertiesDataObjectAdaptor(
                            dataFolder,
                            initialCollection,
                            Props.get("data.extension", "properties")
                    );
                    break;
                case "excel":
                    dataContainer = new ExcelDataObjectAdaptor(
                            dataFolder,
                            initialCollection
                    );
                    break;
                default:
                    throw new DataException(format("Data adaptor %s isn't supported", dataType));
            }
        }
        return dataContainer;
    }

    public static void updateCollection(TestDataObject newObject) {
        dataContainer = newObject;
    }

    public static String getConfigCollection() {
        return Props.get("data.initial.collection", null);
    }

}
