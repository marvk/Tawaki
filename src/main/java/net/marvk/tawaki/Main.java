package net.marvk.tawaki;

import net.marvk.tawaki.data.RepositoryException;
import net.marvk.tawaki.data.SqliteAircraftMetadataRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public final class Main {
    private Main() {
        throw new AssertionError("No instances of main class " + Main.class);
    }

    public static void main(final String[] args) throws IOException, RepositoryException {
        final PropertiesLoader propertiesLoader = new PropertiesLoader(Paths.get("tawaki.properties"));

        propertiesLoader.writeDefaultIfNotExists();
        final Properties properties = propertiesLoader.read();

        final Path lrCatLocation = Paths.get(properties.getProperty(PropertiesLoader.LRCAT_LOCATION_KEY));

        final SqliteAircraftMetadataRepository aircraftMetadataRepository = new SqliteAircraftMetadataRepository(lrCatLocation);

        final List<FirstRegistrationCheckResult> results =
                new JetphotosFirstRegistrationCheck().checkFirstRegistrations(aircraftMetadataRepository.list());

        results.stream()
               .filter(FirstRegistrationCheckResult::isFirstRegistrationOrHasError)
               .forEach(System.out::println);
    }
}
