package org.boonbooth.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlConfig {

    private final Yaml yml;

    private final Map<String, Object> jsonNodes;

    public YamlConfig(String filename){
        yml = new Yaml();

        InputStream inputStream = YamlConfig.class
            .getClassLoader()
            .getResourceAsStream(filename);

        jsonNodes = yml.load(inputStream);
    }
    public String getString(String propertyName){

        // get root node

        var currentNode = jsonNodes;

        for(String name : propertyName.split("\\.")){
            Object value = currentNode.get(name);

            if(!( value instanceof Map)){
                return String.valueOf(value);
            }else{
                currentNode = (Map<String, Object>) currentNode.get(name);
            }
        }
        return null;
    }

    public int getInt(String propretyName){
        return Integer.parseInt(getString(propretyName));
    }

    public float getFloat(String propretyName){
        return Float.parseFloat(getString(propretyName));
    }
}
