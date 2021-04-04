package edu.neu.coe.info6205;

import com.google.common.io.Resources;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.IOException;
import java.net.URL;

public class ConfigUtil {
    private static final String CONFIG_NAME = "config.ini";
    private static final String VIRUS = "COVID19";

    public static float get(String key1, String key2){
        float k = 0.0f;
        Config cfg = new Config();
        URL url = Resources.getResource(CONFIG_NAME);
        cfg.setMultiSection(true);
        Ini ini = new Ini();
        ini.setConfig(cfg);
        try {
            ini.load(url);
            Profile.Section section = ini.get(key1);
            k = Float.parseFloat(section.get(key2));
        } catch (IOException e){
            e.printStackTrace();
        }
        return k;
    }

    public static void main(String[] args) {

        float k = ConfigUtil.get(VIRUS, "K");
        float r = ConfigUtil.get(VIRUS, "R");
        System.out.println(VIRUS);
        System.out.println("K factor:" + k);
        System.out.println("R factor:" + r);
    }
}
