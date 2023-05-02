/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oi.mailingfxapp.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author mmouraam
 */
public class Config {

    private static final Properties appProps = new Properties();
    private static final String FILE_NAME = "app.properties";

    public void init() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            appProps.load(input);

        } catch (FileNotFoundException ex) {
            appProps.setProperty("default_host", "");
            appProps.setProperty("default_port", "");
            appProps.setProperty("default_sid", "");
            appProps.setProperty("default_user", "");
            appProps.setProperty("default_password", "");
            appProps.setProperty("default_blocks", "1000");
            appProps.setProperty("commit_parcial", "true");
            appProps.setProperty("default_driver", "");
            appProps.setProperty("default_classe_driver", "");
            appProps.setProperty("default_database", "");

            String newAppConfigPropertiesFile = FILE_NAME;
            try {
                appProps.store(new FileWriter(newAppConfigPropertiesFile), "store to properties file");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void store() throws IOException {
        appProps.store(new FileWriter(FILE_NAME), "");
    }

    public static Properties getAppProps() {
        return appProps;
    }

}
