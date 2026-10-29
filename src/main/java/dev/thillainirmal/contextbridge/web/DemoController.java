package dev.thillainirmal.contextbridge.web;
import dev.thillainirmal.contextbridge.tools.OperationsTools;
import dev.thillainirmal.contextbridge.tools.ServiceCatalogTools;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController @RequestMapping("/api")
public class DemoController {
    private final ServiceCatalogTools catalog; private final OperationsTools operations;
    public DemoController(ServiceCatalogTools catalog,OperationsTools operations){this.catalog=catalog;this.operations=operations;}
    @GetMapping("/tools") public List<Map<String,String>> tools(){return List.of(
            Map.of("name","listServices","safety","READ_ONLY","purpose","Discover services by team"),
            Map.of("name","serviceHealth","safety","READ_ONLY","purpose","Inspect health and ownership"),
            Map.of("name","findRunbook","safety","READ_ONLY","purpose","Retrieve incident guidance"),
            Map.of("name","previewDeployment","safety","DRY_RUN","purpose","Plan without mutation"),
            Map.of("name","estimateMonthlyCost","safety","PURE","purpose","Estimate infrastructure cost"));}
    @GetMapping("/demo") public Map<String,Object> demo(){return Map.of("services",catalog.listServices("all"),
            "health",catalog.serviceHealth("fraud-engine"),"runbook",operations.findRunbook("http errors"),
            "deployment",operations.previewDeployment("fraud-engine","4.2.1","production"),
            "cost",operations.estimateMonthlyCost(3,2,4));}
}
