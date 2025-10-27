package org.boonbooth.configuration;

import lombok.Getter;

import java.util.Locale;

public class Config {

    private static final String CONFIG_FILE = "boonboothConfig.yml";

    private static final YamlConfig yamlConfig;

    static {
        yamlConfig = new YamlConfig(CONFIG_FILE);

        backgroundFile = yamlConfig.getString("boonbooth.background");
        title          = yamlConfig.getString("boonbooth.title");
        sceneWidth     = yamlConfig.getInt("boonbooth.scene.width");
        sceneHeight    = yamlConfig.getInt("boonbooth.scene.height");
    }

    public static final String backgroundFile;

    public static final String title;

    public static final double sceneWidth;

    public static final double sceneHeight;

}
