package dev.thillainirmal.contextbridge.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ServiceCatalogTools {
    private static final List<ServiceInfo> SERVICES=List.of(
            new ServiceInfo("payments-api","payments","java","2.8.1","healthy",3,"#payments-oncall"),
            new ServiceInfo("fraud-engine","risk","java","4.2.0","degraded",2,"#risk-platform"),
            new ServiceInfo("ledger-writer","finance","kotlin","1.9.4","healthy",4,"#ledger"),
            new ServiceInfo("notification-worker","engagement","java","3.1.2","healthy",1,"#engagement")
    );

    @Tool(description="List registered services, optionally filtered by owning team. Read-only.")
    public List<ServiceInfo> listServices(@ToolParam(description="Team name or 'all'") String team){
        if(team==null||team.isBlank()||team.equalsIgnoreCase("all")) return SERVICES;
        return SERVICES.stream().filter(s->s.team().equalsIgnoreCase(team)).toList();
    }

    @Tool(description="Get health, version, replicas and on-call channel for one service. Read-only.")
    public Map<String,Object> serviceHealth(@ToolParam(description="Exact service name") String service){
        return SERVICES.stream().filter(s->s.name().equalsIgnoreCase(service)).findFirst()
                .<Map<String,Object>>map(s->Map.of("service",s.name(),"status",s.status(),"version",s.version(),
                        "replicas",s.replicas(),"onCall",s.onCall(),"checkedAt","synthetic-demo"))
                .orElse(Map.of("error","unknown service","requested",service==null?"":service.toLowerCase(Locale.ROOT)));
    }

    public record ServiceInfo(String name,String team,String runtime,String version,String status,int replicas,String onCall){}
}
