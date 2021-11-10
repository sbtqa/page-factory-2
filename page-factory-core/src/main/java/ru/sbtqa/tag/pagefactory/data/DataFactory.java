package ru.sbtqa.tag.pagefactory.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.datajack.TestDataProvider;
import ru.sbtqa.tag.datajack.callback.GeneratorCallback;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;

public class DataFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DataFactory.class);
    private static final Configuration PROPERTIES = Configuration.create();
    private static TestDataProvider testDataProvider;
    private static final String BASE_FQDN = PROPERTIES.getDataProviderFqdn() + ".";

    private enum PROVIDERS {
        JSON_DATA_PROVIDER("json.JsonDataProvider"),
        PROPERTIES_DATA_PROVIDER("properties.PropertiesDataProvider"),
        EXCEL_DATA_PROVIDER("excel.ExcelDataProvider"),
        MONGO_DATA_PROVIDER("mongo.MongoDataProvider");

        private final String value;

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
            String initialCollection = PROPERTIES.getDataInitialCollection();
            String dataFolder = PROPERTIES.getDataFolder();
            String dataType = PROPERTIES.getDataType();
            String dataExtension = PROPERTIES.getDataExtension();


            switch (dataType) {
                case "json":
                    testDataProvider = initProvider(PROVIDERS.JSON_DATA_PROVIDER,
                            dataFolder,
                            initialCollection,
                            (dataExtension.equals("")) ? "json" : dataExtension
                    );
                    break;
                case "properties":
                    testDataProvider = initProvider(PROVIDERS.PROPERTIES_DATA_PROVIDER,
                            dataFolder,
                            initialCollection,
                            (dataExtension.equals("")) ? "properties" : dataExtension,
                            PROPERTIES.getDataArrayDelimiter()
                    );
                    break;
                case "excel":
                    testDataProvider = initProvider(PROVIDERS.EXCEL_DATA_PROVIDER,
                            dataFolder,
                            initialCollection
                    );
                    break;
                case "mongo":
                    MongoClient mongoClient = new MongoClient(new MongoClientURI(PROPERTIES.getDataUri()));
                    MongoDatabase db = mongoClient.getDatabase(PROPERTIES.getDataDb());

                    testDataProvider = initProvider(PROVIDERS.MONGO_DATA_PROVIDER, db, initialCollection);
                    break;
                case "stash":
                    LOG.debug("Data provider isn't set. Leaving all placeholders as is.");
                    break;
                default:
                    LOG.debug(format("Using custom data provider %s", dataType));
                    if (!dataExtension.isEmpty() && !PROPERTIES.getDataDb().isEmpty()) {
                        throw new DataException("data.extension and data.db could not be set both");
                    }
                    testDataProvider = initProvider(dataType,
                            dataFolder,
                            initialCollection,
                            dataExtension+PROPERTIES.getDataDb()
                    );
            }
        }
        if (!PROPERTIES.getGeneratorsClass().isEmpty()) {
            String className = PROPERTIES.getGeneratorsClass();
            try {
                testDataProvider.applyGenerator((Class<? extends GeneratorCallback>) DataFactory.class.getClassLoader().loadClass(className));
            } catch (ClassNotFoundException e) {
                throw new DataException(format("Could not find generators class at classpath: %s", className));
            } catch (ClassCastException ex) {
                throw new ClassCastException(format("Class %s doesn't extend %s", className, GeneratorCallback.class.getName()));
            }
        }
        return testDataProvider;
    }

    public static void updateCollection(TestDataProvider newObject) {
        testDataProvider = newObject;
    }

    private static TestDataProvider initProvider(PROVIDERS provider, Object... args) throws DataException {
        return initProvider(provider.value, args);
    }

    private static TestDataProvider initProvider(String provider, Object... args) throws DataException {
        try {
            Class<?> providerClass = DataFactory.class.getClassLoader().loadClass(BASE_FQDN + provider);
            return (TestDataProvider) ConstructorUtils.invokeConstructor(providerClass, args);
        } catch (ClassNotFoundException e) {
            throw new DataException(format("Could not find data provider %s in classpath. " +
                    "Make sure you're added required maven dependency", provider));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new DataException(format("Could not initialize data provider %s", provider), ex);
        }
    }

}