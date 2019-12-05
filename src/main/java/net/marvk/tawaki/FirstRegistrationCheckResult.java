package net.marvk.tawaki;

public final class FirstRegistrationCheckResult {
    private final AircraftMetadata aircraftMetadata;
    private final Boolean firstRegistration;
    private final Throwable throwable;

    private FirstRegistrationCheckResult(final AircraftMetadata aircraftMetadata, final Boolean firstRegistration, final Throwable throwable) {
        if (firstRegistration != null && throwable != null) {
            throw new IllegalArgumentException();
        }

        if (firstRegistration == null && throwable == null) {
            throw new IllegalArgumentException();
        }

        this.aircraftMetadata = aircraftMetadata;
        this.firstRegistration = firstRegistration;
        this.throwable = throwable;
    }

    public AircraftMetadata getAircraftMetadata() {
        return aircraftMetadata;
    }

    public Boolean isFirstRegistration() {
        return firstRegistration;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public boolean hasResult() {
        return firstRegistration != null;
    }

    public boolean hasError() {
        return !hasResult();
    }

    public boolean isFirstRegistrationOrHasError() {
        return hasError() || isFirstRegistration();
    }

    public static FirstRegistrationCheckResult withError(final AircraftMetadata aircraftMetadata, final Throwable throwable) {
        return new FirstRegistrationCheckResult(aircraftMetadata, null, throwable);
    }

    public static FirstRegistrationCheckResult withResult(final AircraftMetadata aircraftMetadata, final boolean firstRegistration) {
        return new FirstRegistrationCheckResult(aircraftMetadata, firstRegistration, null);
    }

    @Override
    public String toString() {
        return "FirstRegistrationCheckResult{" +
                "aircraftMetadata=" + aircraftMetadata +
                (hasResult()
                        ? ", firstRegistration=" + firstRegistration
                        : ", throwable=" + throwable) +
                '}';
    }
}
