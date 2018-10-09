package ru.sbtqa.tag.pagefactory.data;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.datajack.TestDataProvider;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.qautils.properties.Props;

import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;

public class DataFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DataFactory.class);
    private static TestDataProvider testDataProvider;
    private static final String BASE_FQDN = "ru.sbtqa.tag.datajack.providers.";

    private enum PROVIDERS {
        JSON_DATA_PROVIDER("json.JsonDataProvider"),
        PROPERTIES_DATA_PROVIDER("properties.PropertiesDataProvider"),
        EXCEL_DATA_PROVIDER("excel.ExcelDataProvider"),
        MONGO_DATA_PROVIDER("mongo.MongoDataProvider");

        private String value;

        PROVIDERS(String value) {
            this.value = value;
        }

        String getName() {
            return value.split("\\.")[1];
        }

    }

    private DataFactory() {
    }

    public static TestDataProvider getDataProvider() throws DataException {
        if (testDataProvider == null) {
            String initialCollection = Props.get("data.initial.collection");
            String dataFolder = Props.get("data.folder");
            String dataType = Props.get("data.type", "stash");

            switch (dataType) {
                case "json":
                    testDataProvider = initProvider(PROVIDERS.JSON_DATA_PROVIDER,
                            dataFolder,
                            initialCollection,
                            Props.get("data.extension", "json")
                    );
                    break;
                case "properties":
                    testDataProvider = initProvider(PROVIDERS.PROPERTIES_DATA_PROVIDER,
                            dataFolder,
                            initialCollection,
                            Props.get("data.extension", "properties")
                    );
                    break;
                case "excel":
                    testDataProvider = initProvider(PROVIDERS.EXCEL_DATA_PROVIDER,
                            dataFolder,
                            initialCollection
                    );
                    break;
                case "mongo":
                    MongoClient mongoClient = new MongoClient(new MongoClientURI(Props.get("data.uri")));
                    DB db = mongoClient.getDB(Props.get("data.db"));

                    testDataProvider = initProvider(PROVIDERS.MONGO_DATA_PROVIDER, db, initialCollection);
                    break;
                case "stash":
                    LOG.warn("Data provider isnt't set. Leaving all placeholders as is.");
                    break;
                default:
                    throw new DataException(format("Data provider %s isn't supported", dataType));
            }
        }
        return testDataProvider;
    }

    public static void updateCollection(TestDataProvider newObject) {
        testDataProvider = newObject;
    }

    private static TestDataProvider initProvider(PROVIDERS provider, Object... args) throws DataException {
        try {
            Class<? extends TestDataProvider> providerClass =
                    (Class<? extends TestDataProvider>) DataFactory.class.getClassLoader()
                            .loadClass(BASE_FQDN + provider.value);

            return ConstructorUtils.invokeConstructor(providerClass, args);
        } catch (ClassNotFoundException e) {
            throw new DataException(format("Could not find data provider %s in classpath. " +
                    "Make sure you're added required maven dependency", provider.getName()));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new DataException(format("Could not initialize data provider %s", provider), ex);
        }
    }

}