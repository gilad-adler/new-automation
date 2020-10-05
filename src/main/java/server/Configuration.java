package server;

public class Configuration {

    private static String configName = "NA";

    public static String getConfigName() {
        return configName;
    }

    public static void setConfigName(String configName) {
        Configuration.configName = configName;
    }
}
