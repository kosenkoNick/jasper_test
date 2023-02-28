import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.inputcontrols.InputControlStateListWrapper;

public class GetInputControls {
    public static void main(String[] args) {
        //        authentication to JasperServer
        Session session = Connector.connect();

        //        GET INPUT CONTROLS
        OperationResult<InputControlStateListWrapper> operationResult = session
            .inputControlsService()
            .inputControls()
            .forReport("/AMS/Reports/Migrations/Nick_copy_List_of_Security_Roles_3")
            .values()
            .get();
        InputControlStateListWrapper result = operationResult.getEntity();

        result.getInputControlStateList().get(0).getOptions().forEach(
            System.out::println
        );


    }
}
