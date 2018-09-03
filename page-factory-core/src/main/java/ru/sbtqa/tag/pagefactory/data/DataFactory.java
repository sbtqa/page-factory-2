package ru.sbtqa.tag.pagefactory.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import ru.sbtqa.tag.datajack.TestDataProvider;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.qautils.properties.Props;

import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;

public class DataFactory {

    private static TestDataProvider testDataProvider;
    private static String configCollection;
    private static final String BASE_FQDN = "rru.sbtqa.tag.datajack.providers.";
    private enum Providers {PropertiesDataProvider, ExcelDataProvider, MongoDataProvider, JsonDataProvider}

    public static TestDataProvider getDataProvider() throws DataException {
        if (testDataProvider == null) {
            configCollection = Props.get("data.initial.collection", null);
            String dataType = Props.get("data.type", "stash");

            switch (dataType) {
                case "json":

                    testDataProvider = initProvider("json.JsonDataProvider",
                            Props.get("data.folder"),
                            Props.get("data.initial.collection"),
                            Props.get("data.extension", "json")
                    );
                    break;
                case "properties":
                    testDataProvider = initProvider("properties.PropertiesDataProvider",
                            Props.get("data.folder"),
                            Props.get("data.initial.collection"),
                            Props.get("data.extension", "properties")
                    );
                    break;
                case "excel":
                    testDataProvider = initProvider("ExcelDataProvider",
                            Props.get("data.folder"),
                            Props.get("data.initial.collection")
                    );
                    break;
                case "mongo":
                    testDataProvider = initProvider("MongoDataProvider",
                            new MongoClient(new MongoClientURI(Props.get("data.uri"))).getDB("data.db"),
                            Props.get("data.initial.collection")
                    );
                    break;
                default:
                    throw new DataException(format("Data adaptor %s isn't supported", dataType));
            }
        }
        return testDataProvider;
    }

    public static void updateCollection(TestDataProvider newObject) {
        testDataProvider = newObject;
    }

    public static String getConfigCollection() {
        return configCollection;
    }


    private static TestDataProvider initProvider(String provider, Object... args) throws DataException {
        try {

            Class<? extends TestDataProvider> providerClass =
                    (Class<? extends TestDataProvider>) Class.forName(BASE_FQDN + provider,false, DataFactory.class.getClassLoader());

            return ConstructorUtils.invokeConstructor(providerClass, args);
        } catch (ClassNotFoundException e) {
            throw new DataException(format("Could not find data provider %s in classpath. " +
                    "Make sure you're added required dependency", provider));
        } catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new DataException(format("Could not initialize data provider %s", provider), ex);
        }
    }

}
