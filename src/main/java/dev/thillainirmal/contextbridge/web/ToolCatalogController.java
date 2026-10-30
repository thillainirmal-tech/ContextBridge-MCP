package dev.thillainirmal.contextbridge.web;
import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api")
public class ToolCatalogController {@GetMapping("/tools") public List<Map<String,String>> tools(){return List.of(Map.of("name","listServices","safety","READ_ONLY","purpose","Discover services by team"),Map.of("name","serviceHealth","safety","READ_ONLY","purpose","Inspect health and ownership"),Map.of("name","findRunbook","safety","READ_ONLY","purpose","Retrieve incident guidance"),Map.of("name","previewDeployment","safety","DRY_RUN","purpose","Plan without mutation"),Map.of("name","estimateMonthlyCost","safety","PURE","purpose","Estimate infrastructure cost"));}}
