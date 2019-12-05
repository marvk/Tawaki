package net.marvk.tawaki;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JetphotosFirstRegistrationCheck {
    private static final String QUERY_URL = "https://www.jetphotos.com/photo/keyword/";

    public JetphotosFirstRegistrationCheck() {
    }

    public List<FirstRegistrationCheckResult> checkFirstRegistrations(final List<AircraftMetadata> aircraftMetadata) {
        return aircraftMetadata.stream().map(this::checkFirstRegistration).collect(Collectors.toList());
    }

    private FirstRegistrationCheckResult checkFirstRegistration(final AircraftMetadata aircraftMetadata) {
        final Document document;
        try {
            document = Jsoup.connect(queryUrl(aircraftMetadata)).get();
        } catch (final IOException e) {
            return FirstRegistrationCheckResult.withError(aircraftMetadata, e);
        }

        final Element body = document.body();
        final Elements results = body.select("#results");
        final int numPhotos = results.get(0).select("div.result").size();

        return FirstRegistrationCheckResult.withResult(aircraftMetadata, numPhotos == 0);
    }

    private static String queryUrl(final AircraftMetadata aircraftMetadata) {
        return QUERY_URL + aircraftMetadata.getRegistration();
    }
}
