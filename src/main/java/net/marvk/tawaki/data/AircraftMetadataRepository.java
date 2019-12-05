package net.marvk.tawaki.data;

import net.marvk.tawaki.AircraftMetadata;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface AircraftMetadataRepository {
    Optional<AircraftMetadata> getByRegistration() throws RepositoryException;

    List<AircraftMetadata> list() throws RepositoryException;

    Stream<AircraftMetadata> stream() throws RepositoryException;
}
