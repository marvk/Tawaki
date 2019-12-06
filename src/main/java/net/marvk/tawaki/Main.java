package net.marvk.tawaki;

import net.marvk.tawaki.data.AircraftMetadataRepository;
import net.marvk.tawaki.data.RepositoryException;
import net.marvk.tawaki.data.SqliteAircraftMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public final class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
        throw new AssertionError("No instances of main class " + Main.class);
    }

    public static void main(final String[] args) throws IOException {
        final PropertiesIo propertiesIo = new PropertiesIo(Paths.get("tawaki.properties"));

        propertiesIo.writeDefaultIfNotExists();
        final Properties properties = propertiesIo.read();

        final Path lrCatLocation = lrcatPath(propertiesIo, properties);

        try {
            final AircraftMetadataRepository aircraftMetadataRepository = new SqliteAircraftMetadataRepository(lrCatLocation);

            final List<FirstRegistrationCheckResult> results =
                    new JetphotosFirstRegistrationCheck().checkFirstRegistrations(aircraftMetadataRepository.list());

            results.stream()
                   .filter(FirstRegistrationCheckResult::isFirstRegistrationOrHasError)
                   .forEach(Main::print);
        } catch (final RepositoryException e) {
            LOGGER.error("", e);
            System.out.println("Database error: " + e);
            System.out.println("Check the log file for more information");
            System.exit(1);
        }
    }

    private static void print(final FirstRegistrationCheckResult e) {
        final StringBuilder sb = new StringBuilder();

        sb.append(e.getAircraftMetadata().getRegistration());

        if (e.hasResult()) {
            if (e.isFirstRegistration()) {
                sb.append(" -> FIRST REGISTRATION!");
            } else {
                sb.append(" /");
            }
        } else {
            sb.append(" -> Failed to retrieve first registration status with ")
              .append(e.getThrowable());
        }

        System.out.println(sb.toString());
    }

    private static Path lrcatPath(final PropertiesIo propertiesIo, final Properties properties) throws IOException {
        final String pathString = properties.getProperty(PropertiesIo.LRCAT_LOCATION_KEY).replaceAll("\"", "");
        final Path pathFromPropertiesFile = Paths.get(pathString);

        if (Files.isRegularFile(pathFromPropertiesFile)) {
            return pathFromPropertiesFile;
        }

        while (true) {
            final Scanner scanner = new Scanner(System.in);

            try {
                System.out.println("Please enter the path to your Lightroom.lrcat file");
                System.out.print("> ");
                final String s = scanner.nextLine();

                final Path result = Paths.get(s.replaceAll("\"", ""));

                if (Files.isRegularFile(result)) {
                    properties.setProperty(PropertiesIo.LRCAT_LOCATION_KEY, result.toString());
                    propertiesIo.write(properties);

                    return result;
                }
            } catch (final InvalidPathException e) {
                LOGGER.warn("Invalid path entered by user", e);
                System.out.println("Invalid path: " + e.getInput());
            }

            System.out.println("Invalid path");
        }
    }
}
