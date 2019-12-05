package net.marvk.tawaki.data;

import com.zaxxer.hikari.HikariDataSource;
import net.marvk.tawaki.AircraftMetadata;
import org.sqlite.SQLiteJDBCLoader;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SqliteAircraftMetadataRepository implements AircraftMetadataRepository {
    private static final String SELECT_ALL_QUERY = "SELECT distinct internalValue as registration\n" +
            "FROM AgSearchablePhotoProperty\n" +
            "WHERE propertySpec = (SELECT id_local FROM AgPhotoPropertySpec WHERE key = 'registration')\n" +
            "  AND registration IS NOT '?'\n" +
            "  AND registration IS NOT 'Multiple'";

    private final HikariDataSource dataSource;

    public SqliteAircraftMetadataRepository(final Path sqlitePath) throws RepositoryException {
        try {
            SQLiteJDBCLoader.initialize();
        } catch (final Exception e) {
            throw new RepositoryException("Failed to initialize SQLite", e);
        }

        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:sqlite:" + sqlitePath.toAbsolutePath());
    }

    @Override
    public Optional<AircraftMetadata> getByRegistration() throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AircraftMetadata> list() throws RepositoryException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {

            final ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return Collections.emptyList();
            }

            final List<AircraftMetadata> result = new ArrayList<>();
            do {
                final String registration = resultSet.getString(1);
                final AircraftMetadata aircraftMetadata = new AircraftMetadata(registration);

                result.add(aircraftMetadata);
            } while (resultSet.next());

            return result;
        } catch (final SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Stream<AircraftMetadata> stream() throws RepositoryException {
        return list().stream();
    }
}
