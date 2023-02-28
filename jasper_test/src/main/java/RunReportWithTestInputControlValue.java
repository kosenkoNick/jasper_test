import com.jaspersoft.jasperserver.dto.reports.inputcontrols.InputControlOption;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
import com.jaspersoft.jasperserver.jaxrs.client.core.exceptions.BadRequestException;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.ReportExecution;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.ReportExecutionStatusObject;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.inputcontrols.InputControlStateListWrapper;

public class RunReportWithTestInputControlValue {
    public static void main(String[] args) {
        //        authentication to JasperServer
        Session session = Connector.connect();

        String URI = "/AMS/Reports/Migrations/Nick_copy_List_of_Security_Roles_3";

        //        GET INPUT CONTROLS' VALUES
        OperationResult<InputControlStateListWrapper> operationResult = session
            .inputControlsService()
            .inputControls()
            .forReport(URI)
            .values()
            .get();
        InputControlStateListWrapper result = operationResult.getEntity();

//        RUN REPORT WITH EACH VALUE
        for (InputControlOption option : result.getInputControlStateList().get(0).getOptions()) {
            try {
//                RUN THE REPORT WITH PARAM
                OperationResult<ReportExecution> reportResult = session
                    .reportingService()
                    .report(URI)
                    .reportExecutions()
                    .reportParameter("SUBSTITUTE_USER_ID", option.getValue())
                    .pages()
                    .async(Boolean.FALSE)
                    .run();

                ReportExecution entity = reportResult.getEntity();
                System.out.println("Value = " + option.getValue());
                System.out.println(entity.getReportURI());

//                GET REPORT STATUS
                OperationResult<ReportExecutionStatusObject> status = session
                    .reportingService()
                    .reportExecution(entity.getRequestId())
                    .status();
                ReportExecutionStatusObject statusEntity = status.getEntity();
                System.out.println(statusEntity.getValue() + "\n");
            } catch (BadRequestException bre) {
                System.out.println(
                    URI + "\t report is either corrupted or received bad params");
            }
        }
    }
}
