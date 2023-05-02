/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oi.mailingfxapp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

/**
 *
 * @author mmouraam
 */
public class Main {

    public static void main(String[] args) throws Exception {
        mainCripto();
    }

    public static void mainCripto() {

        String senha = "123";
        String encoded = new String(Base64.getEncoder().encode(senha.getBytes()));
        System.out.println(encoded);
        System.out.println(new String(Base64.getDecoder().decode(encoded.getBytes())));

    }

    public static void mainMailing() throws RuntimeException, Exception {
        //dao.batchinsert();

        FileWriter fileWriter = new FileWriter("c:\\dev\\temp\\mailing3milhoes500mil.txt");

        System.out.println(randomAlphaNumeric(11));

        for (int i = 0; i < 3500000; i++) {
            fileWriter.write(i + ";" + randomAlphaNumeric(14) + ";" + randomAlphaNumeric(11) + ";7307235420009434351507;0;141.67\n");
        }

        fileWriter.close();
    }

    public static void main2() throws FileNotFoundException, IOException {

        FileInputStream inputStream = null;
        Scanner sc = null;

        try {
            long size = 0;
            inputStream = new FileInputStream("c:\\dev\\temp\\mailing5.txt");
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                size += line.getBytes().length;
            }
            System.out.println(size);//2368888890
            //2333888890
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    public static void mainFilesize() throws FileNotFoundException, IOException {

        FileInputStream inputStream = null;
        Scanner sc = null;

        try {
            File file = new File("c:\\dev\\temp\\mailing5.txt");
            System.out.println(file.length() + " bytes");

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    private static final String NUMERIC_STRING = "0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
