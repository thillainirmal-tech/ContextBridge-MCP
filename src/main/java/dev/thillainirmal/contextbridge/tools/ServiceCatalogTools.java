package dev.thillainirmal.contextbridge.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ServiceCatalogTools {
    private static final List<ServiceInfo> BOOTSTRAP=List.of(
            new ServiceInfo("payments-api","payments","java","unset","unconfigured",0,"#payments-oncall"),
            new ServiceInfo("fraud-engine","risk","java","unset","unconfigured",0,"#risk-platform"),
            new ServiceInfo("ledger-writer","finance","kotlin","unset","unconfigured",0,"#ledger"),
            new ServiceInfo("notification-worker","engagement","java","unset","unconfigured",0,"#engagement")
    );
    private final List<ServiceInfo> services;

    public ServiceCatalogTools(){this.services=BOOTSTRAP;}
    @Autowired public ServiceCatalogTools(ObjectMapper mapper,@Value("${contextbridge.catalog-json:}")String json){
        if(json==null||json.isBlank()){this.services=BOOTSTRAP;return;}
        try{this.services=List.copyOf(mapper.readValue(json,new TypeReference<List<ServiceInfo>>(){}));}
        catch(Exception exception){throw new IllegalArgumentException("CONTEXTBRIDGE_CATALOG_JSON is invalid",exception);}
    }

    @Tool(description="List registered services, optionally filtered by owning team. Read-only.")
    public List<ServiceInfo> listServices(@ToolParam(description="Team name or 'all'") String team){
        if(team==null||team.isBlank()||team.equalsIgnoreCase("all")) return services;
        return services.stream().filter(s->s.team().equalsIgnoreCase(team)).toList();
    }

    @Tool(description="Get health, version, replicas and on-call channel for one service. Read-only.")
    public Map<String,Object> serviceHealth(@ToolParam(description="Exact service name") String service){
        return services.stream().filter(s->s.name().equalsIgnoreCase(service)).findFirst()
                .<Map<String,Object>>map(s->Map.of("service",s.name(),"status",s.status(),"version",s.version(),
                        "replicas",s.replicas(),"onCall",s.onCall(),"source","configured-catalog","checkedAt",Instant.now().toString()))
                .orElse(Map.of("error","unknown service","requested",service==null?"":service.toLowerCase(Locale.ROOT)));
    }

    public record ServiceInfo(String name,String team,String runtime,String version,String status,int replicas,String onCall){}
}
