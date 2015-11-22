package com.kohls.aqa.webstore.util;

import java.io.IOException;
import java.util.Properties;

import static java.lang.String.format;
import static java.lang.System.getProperties;

public final class Config {

    public static final String STORE_IP = "store.prod.ip";
    public static final String STORE_URL = "url";
    public static final String DRIVER_TYPE = "driverType";

    private static final Properties ENV_PROPERTIES = getProperties();
    public static final String ENV_NAME = (String) ENV_PROPERTIES.get("env");
    private static final String PATH_TO_ENV_PROPERTIES = format("/config/%s.properties", ENV_NAME);

    private static Properties props = new Properties();

    static {
        try {
            props.load(Config.class.getResourceAsStream(PATH_TO_ENV_PROPERTIES));
            props.putAll(ENV_PROPERTIES);
            props.put(STORE_URL, getStoreURL());
        } catch (IOException e) {
            throw new RuntimeException("Can't load config resources", e);
        }
    }

    public static synchronized String getProperty(String propKey) {
        return props.getProperty(propKey);
    }

    private static final String getStoreURL() {
            String ip = getProperty(STORE_IP);
            return "http://" + ip;
        
    }
}
