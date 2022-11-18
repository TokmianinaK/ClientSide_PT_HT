package navigationtiming;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static util.Constants.*;
import static util.WebDriverHolder.getDriver;

public class PerfNavigationTiming {

    Map<String, Object> timings = null;

    private static FileWriter fileWriter;

    private final String javaScriptForPerformance = "var performance = window.performance || window.webkitPerformance ||" +
            " window.mozPerformance || window.msPerformance || {};var timings = performance.timing || {};return timings;";
    private final String javaScriptForPerformanceInternetExplorer = "return {performance:JSON.stringify(performance.timing)}";

    public void writeToInflux(String pageName) {
        getAllTiming();

        if (IS_INFLUX_DISABLED) {
            writeMetricsToJsonFile(pageName, this.getLatency(),
                    this.getTimeToInteract(), this.getTimeToLoad(), this.getOnLoad(), this.getTotalTime());
        }
    }

    public Map<String, Object> getAllTiming() {
        JavascriptExecutor jsrunner = (JavascriptExecutor) getDriver();
        if ("InternetExplorer".equalsIgnoreCase(BROWSER_NAME)) {
            Map<String, Object> ieTimings = (Map<String, Object>) jsrunner.executeScript(javaScriptForPerformanceInternetExplorer);
            timings = parseNavigationTimingDataFromIe(ieTimings);
        } else {
            timings = (Map<String, Object>) jsrunner.executeScript(javaScriptForPerformance);
        }
        return timings;
    }

    private Map<String, Object> parseNavigationTimingDataFromIe(Map<String, Object> ieTimings) {
        Map<String, Object> parsedTimings = new HashMap<>();
        String mapValue = (String) ieTimings.get("performance");
        mapValue = mapValue.substring(1, mapValue.length() - 1).replace("\"", "");
        String[] keyValuePairs = mapValue.split(",");

        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            parsedTimings.put(entry[0].trim(), Long.valueOf(entry[1]));
        }
        return parsedTimings;
    }

    @SneakyThrows
    private void writeMetricsToJsonFile(String pageName, long latency, long tti, long ttl, long onload, long totalTime) {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject;
        jsonObject = PERF_METRICS_JSON.exists() ? mapper.readValue(PERF_METRICS_JSON, JSONObject.class) : new JSONObject();
        JSONArray entityArray = new JSONArray();
        JSONObject innerJsonObject = new JSONObject();
        innerJsonObject.put("latency", latency);
        innerJsonObject.put("tti", tti);
        innerJsonObject.put("ttl", ttl);
        innerJsonObject.put("onload", onload);
        innerJsonObject.put("total_time", totalTime);

        entityArray.add(innerJsonObject);
        jsonObject.put(pageName, entityArray);

        try {
            fileWriter = new FileWriter(PERF_METRICS_JSON);
            fileWriter.write(jsonObject.toJSONString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Long getAnTime(String name) {
        return (Long) timings.get((Object) name);
    }

    public Long getNavigationStart() {
        return getAnTime("navigationStart");
    }

    public Long getResponseStart() {
        return getAnTime("responseStart");
    }

    public Long getResponseEnd() {
        return getAnTime("responseEnd");
    }

    public Long getDomLoading() {
        return getAnTime("domLoading");
    }

    public Long getDomInteractive() {
        return getAnTime("domInteractive");
    }

    public Long getDomComplete() {
        return getAnTime("domComplete");
    }

    public Long getLoadEventStart() {
        return getAnTime("loadEventStart");
    }

    public Long getLoadEventEnd() {
        return getAnTime("loadEventEnd");
    }

    public long getLatency() {
        return getResponseStart() - getNavigationStart();
    }

    public long getBackendResponse() {
        return getResponseEnd() - getResponseStart();
    }

    public long getTimeToInteract() {
        return getDomInteractive() - getDomLoading();
    }

    public long getTimeToLoad() {
        return getDomComplete() - getDomInteractive();
    }

    public long getOnLoad() {
        return getLoadEventEnd() - getLoadEventStart();
    }

    public long getTotalTime() {
        return getLoadEventEnd() - getNavigationStart();
    }
}
