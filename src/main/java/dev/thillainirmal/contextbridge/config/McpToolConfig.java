package dev.thillainirmal.contextbridge.config;
import dev.thillainirmal.contextbridge.tools.OperationsTools;
import dev.thillainirmal.contextbridge.tools.ServiceCatalogTools;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
@Configuration
public class McpToolConfig {
    @Bean
    public List<ToolCallback> toolCallbacks(ServiceCatalogTools catalog, OperationsTools operations){
        return List.of(ToolCallbacks.from(catalog,operations));
    }
}
