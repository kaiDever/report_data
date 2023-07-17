package xinmei.report.data.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class GoogleUtil {

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/Users/wanghuimin/IdeaProjects/report-data/src/main/resources/credentials.json";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    public static Credential getCredentials(final HttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Files.newInputStream(Paths.get(CREDENTIALS_FILE_PATH));
        GoogleCredential credential = GoogleCredential.fromStream(in, HTTP_TRANSPORT, JSON_FACTORY)
                .createScoped(SCOPES);
        return credential;
    }
}
