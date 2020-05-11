package selenium.ui.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class Config {

    private static Properties prop = new Properties();
    public static final String CONFIG_DIR_SUFFIX = "-config";
    public static final String SHARED_SUFFIX = "_shared";
    public static final String PROPERTIES_SUFFIX = ".properties";
    private Map<String, String> environmentVariables;
    private static Properties systemProperties;
    private Map<String, String> xmlTestSuiteParams;
    private Map<String, String> xmlTestSetParams;
    private Map<String, String> xmlTestMethodParams;
    private static Map<String, Properties> propertyFiles;
    private Map<String, String> propertyFileProperties;
    private Map<String, String> defaultValues;
    private boolean verbose;

    public Config()
    {
        if(environmentVariables == null) environmentVariables = System.getenv();
        if(systemProperties == null) systemProperties = System.getProperties();
        verbose = false;
    }

    public Config(final String path) {
        readProperties(path);
    }

    public void readProperties(final String propFileName) {

        InputStream input = null;

        try {
            
            input = new FileInputStream(propFileName);

            // load a properties file
            prop.load(input);

        } catch (final IOException ex) {

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException e) {

                }
            }
        }
    }

    public static void setVerbose() {
        final Config config = (Config) Context.get("config");
        config.setVerboseInternal(true);
    }

    public static String getValue(final String key) {
        if (prop.containsKey(key)) {
            return prop.getProperty(key).trim();
        }
        return null;
    }

    public static Character getCharacter(final String key) {
        return getCharacter(key, Thread.currentThread().getId());
    }

    public static Character getCharacter(final String key, final long associatedThreadId) {
        final String value = getValue(key, associatedThreadId);
        if (value == null)
            return null;
        return value.charAt(0);
    }

    public static String getString(final String key) {
        return getString(key, Thread.currentThread().getId());
    }

    public static String getString(final String key, final long associatedThreadId) {
        final String value = getValue(key, associatedThreadId);
        return value;
    }

    public static boolean getBoolean(final String key) {
        return getBoolean(key, Thread.currentThread().getId());
    }

    // Get a config value with the assumption that it will represent a boolean value
    public static boolean getBoolean(final String key, final long associatedThreadId) {
        String value = getValue(key, associatedThreadId);
        if (value == null)
            return false;
        value = value.toLowerCase();
        switch (value) {
            case "true":
            case "yes":
            case "on":
            case "enabled":
            case "active":
            case "1":
                return true;
            case "false":
            case "no":
            case "off":
            case "disabled":
            case "inactive":
            case "0":
                return false;
            default:
                return false;
        }
    }

    public static Byte getByte(final String key) {
        return getByte(key, Thread.currentThread().getId());
    }

    public static Byte getByte(final String key, final long associatedThreadId) {
        final String value = getValue(key, associatedThreadId);
        if (value == null)
            return null;
        return Byte.parseByte(value);
    }

    public static Short getShort(final String key) {
        return getShort(key, Thread.currentThread().getId());
    }

    public static Short getShort(final String key, final long associatedThreadId) {
        final String value = getValue(key, associatedThreadId);
        if (value == null)
            return null;
        return Short.parseShort(value);
    }

    public static Integer getInteger(final String key) {
        return getInteger(key, Thread.currentThread().getId());
    }

    public static Integer getInteger(final String key, final long associatedThreadId) {
        final String value = getValue(key, associatedThreadId);
        if (value == null)
            return null;
        return Integer.parseInt(value);
    }

    public static Long getLong(final String key) {
        return getLong(key, Thread.currentThread().getId());
    }

    public static Long getLong(final String key, final long associatedThreadId) {
        final String value = getValue(key, associatedThreadId);
        if (value == null)
            return null;
        return Long.parseLong(value);
    }

    public static Double getDouble(final String key) {
        return getDouble(key, Thread.currentThread().getId());
    }

    public static Double getDouble(final String key, final long associatedThreadId) {
        final String value = getValue(key, associatedThreadId);
        if (value == null)
            return null;
        return Double.parseDouble(value);
    }

    public static Float getFloat(final String key) {
        return getFloat(key, Thread.currentThread().getId());
    }

    public static Float getFloat(final String key, final long associatedThreadId) {
        final String value = getValue(key, associatedThreadId);
        if (value == null)
            return null;
        return Float.parseFloat(value);
    }

    public static <T extends Enum<T>> T getEnum(final String key, final Class<T> enumType) {
        return getEnum(key, enumType, Thread.currentThread().getId());
    }

    public static <T extends Enum<T>> T getEnum(final String key, final Class<T> enumType,
            final long associatedThreadId) {
        final String value = getValue(key, associatedThreadId);
        if (value == null)
            return null;
        return Enum.valueOf(enumType, value);
    }
    private static String getValue(final String key, final long associatedThreadId) {
        final Config config = (Config) Context.get("config", associatedThreadId);
        if (config == null) {
            final String errorMessage = "Config could not be found in the current context!  Did you forget to extend one "
                    + "of the LUXTestFramework manager classes?";
            return null;        
        } 
        final String value = config.get(key);
        return value;
    }

    public static void setXmlTestSuiteParams(final String xmlParamsList) {
        final Config config = (Config) Context.get("config");
        config.setXmlTestSuiteParamsInternal(xmlParamsList);
    }

    public static void addPropertiesFromSharedConfigFile(final String sharedConfigFileName) {
        addPropertiesFromFile(Config.getString("sourceSet") + CONFIG_DIR_SUFFIX + "/" + sharedConfigFileName
                + SHARED_SUFFIX + PROPERTIES_SUFFIX);
    }

    public static void addPropertiesFromConfigFile(final String configFileName) {
        addPropertiesFromFile(
                Config.getString("sourceSet") + CONFIG_DIR_SUFFIX + "/" + configFileName + PROPERTIES_SUFFIX);
    }

    public static void addPropertiesFromFile(final String propertyFilePath) {
        final Config config = (Config) Context.get("config");
        config.addPropertiesFromFileInternal(propertyFilePath);
    }

    public static void addDefaultValues(final Map<String, String> defaultValues) {
        final Config config = (Config) Context.get("config");
        config.addDefaultValuesInternal(defaultValues);
    }

    private synchronized void setVerboseInternal(final boolean verbose) {
        this.verbose = verbose;
    }

    private String get(final String key) {
        String value = "";

        // Check for value in system properties first (highest precedence)
        if (verbose)
            value = systemProperties.getProperty(key);

        // Check for value in environment variables next
        if (verbose)
            value = environmentVariables.get(key);

        if (this.defaultValues != null) {
            value = this.defaultValues.get(key);
        }
        if(propertyFileProperties != null) {
            value = propertyFileProperties.get(key);
        }
        
        // Check for value in XML test method parameters next
        return value;
    }

    private synchronized void setXmlTestSuiteParamsInternal(final String xmlParamsList) {
        xmlTestSuiteParams = Config.parseXMLParamsList(xmlParamsList);
    }

    private static Map<String, String> parseXMLParamsList(final String xmlParamsList) {
        final Map<String, String> xmlParams = new ConcurrentHashMap<>();
        if (xmlParamsList == null || xmlParamsList.isEmpty()) {
            return xmlParams;
        }
        final String[] entries = xmlParamsList.split("\\\\n");
        for (final String entry : entries) {
            final int assignmentIndex = entry.indexOf("=");
            if (assignmentIndex == -1) {
                final String errorMessage = String.format(
                        "Could not find assignment operator in XML parameter entry [%s].  Unable to read parameter!",
                        entry);
            }
            final String key = entry.substring(0, assignmentIndex).trim();
            final String value = entry.substring(assignmentIndex + 1);
            xmlParams.put(key, value);
        }
        return xmlParams;
    }

    private synchronized void addPropertiesFromFileInternal(final String propertyFileName) {
        final Properties properties = getPropertiesByFileName(propertyFileName);
        final Set<Map.Entry<Object, Object>> propertyEntries = properties.entrySet();
        final Iterator<Map.Entry<Object, Object>> propertyEntryIterator = propertyEntries.iterator();

        // Make sure the map is initialized
        if (propertyFileProperties == null) {
            propertyFileProperties = new ConcurrentHashMap<>();
        }

        // Add every property to the map, overriding any existing properties with the
        // current ones
        while (propertyEntryIterator.hasNext()) {
            final Map.Entry<Object, Object> propertyEntry = propertyEntryIterator.next();
            final String propertyKey = propertyEntry.getKey().toString();
            final String propertyValue = propertyEntry.getValue().toString();
            propertyFileProperties.put(propertyKey, propertyValue);
        }
    }

    public synchronized void addProperties(final String propertyKey, final String propertyValue) {
        if (propertyFileProperties == null) {
            propertyFileProperties = new ConcurrentHashMap<>();
        }

        propertyFileProperties.put(propertyKey, propertyValue);
    }

    public synchronized static Properties getPropertiesByFileName(final String propertyFileName) {
        Properties properties;

        // If this is the first property file being loaded then we'll have to
        // instantiate the map.
        if (propertyFiles == null) {
            propertyFiles = new ConcurrentHashMap<>();
            properties = loadPropertiesFromFile(propertyFileName);
            propertyFiles.put(propertyFileName, properties);
            return properties;
        }

        // If this property file has not been loaded yet then we'll have to load it from
        // disk.
        properties = propertyFiles.get(propertyFileName);
        if (properties == null) {
            properties = loadPropertiesFromFile(propertyFileName);
            propertyFiles.put(propertyFileName, properties);
        }

        // We've loaded this property file before, so we simply return the properties
        // retrieved from the map.
        return properties;
    }

    private static Properties loadPropertiesFromFile(String propertyFileName) {
        Properties properties = null;
        InputStream stream = null;
        final String logMessage = String.format("Caching contents of property file [%s]... ", propertyFileName);
        try {
            if (!propertyFileName.endsWith(".properties"))
                propertyFileName = propertyFileName.concat(".properties");

            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFileName);
            properties = new Properties();
            properties.load(stream);
            
        } catch (final Exception e) {
            throw new RuntimeException(logMessage + "FAILURE (HALTING)", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (final IOException e) {
                    throw new RuntimeException("Error closing IOStream!", e);
                }
            }
        }
        return properties;
    }

    private synchronized void addDefaultValuesInternal(final Map<String, String> defaultValues) {
        if (this.defaultValues == null)
            this.defaultValues = new ConcurrentHashMap<>();
        final Set<Map.Entry<String, String>> defaultValueEntrySet = defaultValues.entrySet();
        final Iterator<Map.Entry<String, String>> defaultValueEntrySetIterator = defaultValueEntrySet.iterator();
        while (defaultValueEntrySetIterator.hasNext()) {
            final Map.Entry<String, String> defaultValueEntry = defaultValueEntrySetIterator.next();
            this.defaultValues.put(defaultValueEntry.getKey(), defaultValueEntry.getValue());
        }
    }
}

