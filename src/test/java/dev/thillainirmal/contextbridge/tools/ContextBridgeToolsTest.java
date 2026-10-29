package dev.thillainirmal.contextbridge.tools;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
class ContextBridgeToolsTest {
    private final ServiceCatalogTools catalog=new ServiceCatalogTools();
    private final OperationsTools operations=new OperationsTools();
    @Test void filtersServicesByTeam(){assertThat(catalog.listServices("risk")).singleElement().extracting(ServiceCatalogTools.ServiceInfo::name).isEqualTo("fraud-engine");}
    @Test void reportsUnknownServiceSafely(){assertThat(catalog.serviceHealth("missing")).containsEntry("error","unknown service");}
    @Test void retrievesErrorRunbook(){assertThat(operations.findRunbook("http errors")).containsEntry("found",true).containsEntry("id","RB-ERR-02");}
    @Test void productionPreviewRequiresApprovalAndNeverExecutes(){Map<String,Object> r=operations.previewDeployment("payments-api","2.9.0","production");assertThat(r).containsEntry("approvalRequired",true).containsEntry("executed",false).containsEntry("mode","DRY_RUN");}
    @Test void costEstimateIsDeterministic(){assertThat(operations.estimateMonthlyCost(3,2,4)).containsEntry("estimatedMonthlyUsd",243.0);}
    @Test void rejectsInvalidCostInputs(){assertThat(operations.estimateMonthlyCost(0,2,4)).containsKey("error");}
}
