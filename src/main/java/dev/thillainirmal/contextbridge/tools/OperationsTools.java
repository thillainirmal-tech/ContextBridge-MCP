package dev.thillainirmal.contextbridge.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class OperationsTools {
    @Tool(description="Retrieve a concise incident runbook by symptom. Read-only and safe for automated use.")
    public Map<String,Object> findRunbook(@ToolParam(description="Incident symptom such as latency, errors, queue or database") String symptom){
        String value=symptom==null?"":symptom.toLowerCase(Locale.ROOT);
        if(value.contains("latency")) return runbook("RB-LAT-01","High latency",List.of("Check p95 by route","Inspect dependency latency","Compare latest deployment","Scale only after bottleneck confirmation"));
        if(value.contains("error")) return runbook("RB-ERR-02","HTTP errors",List.of("Group errors by code","Inspect correlated trace IDs","Check downstream health","Rollback requires human approval"));
        if(value.contains("queue")) return runbook("RB-QUE-03","Queue backlog",List.of("Measure oldest message age","Check consumer lag","Inspect poison messages","Scale consumers within quota"));
        return Map.of("found",false,"message","No matching runbook; escalate to the owning team");
    }

    @Tool(description="Preview a deployment change without executing it. This tool never mutates infrastructure.")
    public Map<String,Object> previewDeployment(@ToolParam(description="Service name") String service,
                                                @ToolParam(description="Target version") String version,
                                                @ToolParam(description="Target environment") String environment){
        boolean production="production".equalsIgnoreCase(environment)||"prod".equalsIgnoreCase(environment);
        return Map.of("mode","DRY_RUN","service",service,"targetVersion",version,"environment",environment,
                "approvalRequired",production,"steps",List.of("validate artifact signature","check error budget",
                        "deploy canary","observe metrics","promote or rollback"),"executed",false);
    }

    @Tool(description="Estimate monthly compute cost from replicas, CPU and memory. Pure calculation.")
    public Map<String,Object> estimateMonthlyCost(@ToolParam(description="Replica count") int replicas,
                                                  @ToolParam(description="CPU cores per replica") double cpu,
                                                  @ToolParam(description="Memory GiB per replica") double memoryGb){
        if(replicas<1||cpu<=0||memoryGb<=0) return Map.of("error","all values must be positive");
        double monthly=replicas*(cpu*31.2+memoryGb*4.65);
        return Map.of("replicas",replicas,"estimatedMonthlyUsd",Math.round(monthly*100.0)/100.0,
                "assumption","730 hours; illustrative rates only");
    }

    private Map<String,Object> runbook(String id,String title,List<String> steps){
        return Map.of("found",true,"id",id,"title",title,"steps",steps,"safety","review before production action");
    }
}
