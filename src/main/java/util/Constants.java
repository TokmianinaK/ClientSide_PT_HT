package util;

import lombok.SneakyThrows;
import pages.ComputersPage;
import pages.DesktopsPage;
import pages.MainPage;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Constants {

    public final static String BROWSER_NAME = System.getProperty("browser");
    public final static Boolean IS_INFLUX_DISABLED = Boolean.valueOf(getProperties().get("influxDisabled"));
    public static String SCENARIO_NAME;
    public final static String LOGIN_URL = getProperties().get("envUrl");
    public final static File PERF_METRICS_JSON = new File("perfMetrics.json");
    public final static BrowserFactory BROWSER_FACTORY = new BrowserFactory();
    public static DesktopsPage DESKTOPS_PAGE;
    public static MainPage MAIN_PAGE;
    public static ComputersPage COMPUTERS_PAGE;

    @SneakyThrows
    private static Map<String, String> getProperties() {
        Properties properties = new Properties();
        File propFile = new File("src/main/resources/application.properties");
        String propertiesFileName = propFile.getAbsolutePath();
        Map<String, String> propertiesMap = new HashMap<>();
        properties.load(new FileInputStream(propertiesFileName));

        propertiesMap.put("envUrl", properties.getProperty("envUrl"));
        propertiesMap.put("dbUrl", properties.getProperty("dbUrl"));
        propertiesMap.put("periodicity", properties.getProperty("periodicity"));
        propertiesMap.put("periodicity_comment", properties.getProperty("periodicity_comment"));
        propertiesMap.put("buildID", properties.getProperty("buildID"));
        propertiesMap.put("influxDisabled", properties.getProperty("influxDisabled"));

        return propertiesMap;
    }

    @SneakyThrows
    public static void pause(long sleepTime) {
        Thread.sleep(sleepTime);
    }

    public static void deleteJsonFiles() {
        if (PERF_METRICS_JSON.exists()) {
            PERF_METRICS_JSON.delete();
        }
    }
}
