package net.marvk.tawaki;

public final class AircraftMetadata {
    private final String registration;

    public AircraftMetadata(final String registration) {
        this.registration = registration;
    }

    public String getRegistration() {
        return registration;
    }

    @Override
    public String toString() {
        return "AircraftMetadata{" +
                "registration='" + registration + '\'' +
                '}';
    }
}
