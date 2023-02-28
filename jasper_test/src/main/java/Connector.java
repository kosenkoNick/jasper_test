import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;

public class Connector {
    static Session connect() {
        RestClientConfiguration configuration = RestClientConfiguration.loadConfiguration("configuration.properties");
        JasperserverRestClient client = new JasperserverRestClient(configuration);
        return client.authenticate("superuser", "superuser");
    }
}
