package net.marvk.tawaki;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class PropertiesIo {
    public static final String LRCAT_LOCATION_KEY = "LIGHTROOM_CATALOG_LOCATION";
    private final Path propertiesPath;

    public PropertiesIo(final Path propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    public void writeDefaultIfNotExists() throws IOException {
        if (!Files.exists(propertiesPath)) {
            storeProperties(defaultProperties());
        }
    }

    private void storeProperties(final Properties properties) throws IOException {
        properties.store(new FileWriter(propertiesPath.toFile()), null);
    }

    public Properties read() throws IOException {
        final Properties properties = new Properties(defaultProperties());
        properties.load(new FileReader(propertiesPath.toFile()));

        return properties;
    }

    private static Properties defaultProperties() {
        final Properties properties = new Properties();

        properties.put(LRCAT_LOCATION_KEY, "");

        return properties;
    }

    public void write(final Properties properties) throws IOException {
        storeProperties(properties);
    }
}
