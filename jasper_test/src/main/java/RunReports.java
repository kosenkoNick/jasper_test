import com.jaspersoft.jasperserver.dto.resources.ClientResourceListWrapper;
import com.jaspersoft.jasperserver.dto.resources.ClientResourceLookup;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.resources.util.ResourceSearchParameter;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
import com.jaspersoft.jasperserver.jaxrs.client.core.exceptions.BadRequestException;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.ReportExecution;
import java.util.List;
import java.util.stream.Collectors;

public class RunReports {
    public static void main(String[] args) {
//        authentication to JasperServer
        Session session = Connector.connect();

//        get all the resources in Jasper Server with type = "reportUnit"
        OperationResult<ClientResourceListWrapper> allReports = session
            .resourcesService()
            .resources()
            .parameter(ResourceSearchParameter.TYPE, "reportUnit")
            .search();
        ClientResourceListWrapper resourceListWrapper = allReports.getEntity();

//        filter only /AMS folder resources
        List<ClientResourceLookup> amsReports = resourceListWrapper.getResourceLookups()
            .stream()
            .filter(type -> type.getUri().matches(".*/AMS.*"))
            .collect(Collectors.toList());

        System.out.println("Size is " + amsReports.size());
        amsReports.forEach(
            report -> System.out.println(report
                .toString()
                .replaceAll("\\{", "{\n\t")
                .replaceAll("}", "\n}")
                .replaceAll(",\\s+", ",\n\t")
            )
        );
        System.out.println("\n\n");


//        run all the reports in /AMS folder, including sub reports
        for (ClientResourceLookup lookup : amsReports) {
            try {
                OperationResult<ReportExecution> result = session
                    .reportingService()
                    .report(lookup.getUri())
                    .reportExecutions()
                    .pages()
                    .async(Boolean.FALSE)
                    .run();

                ReportExecution entity = result.getEntity();
                System.out.println(entity);
            } catch (BadRequestException bre) {
                System.out.println(
                    lookup.getLabel() + "\t report is either corrupted or received bad params");
            }
        }
    }
}


