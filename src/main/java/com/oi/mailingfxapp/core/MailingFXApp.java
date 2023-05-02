/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oi.mailingfxapp.core;

import com.oi.mailingfxapp.Task;
import com.oi.mailingfxapp.enums.DatabaseVendor;

/**
 *
 * @author mmouraam
 */
public class MailingFXApp {

    private static Task task = null;
    private static Thread taskThread = null;

    public static void initTask(String path, int columns, String tabela, int bulksize, String host, String port, String schema, String user, String password, boolean ignoreFirst, boolean replaceData, boolean commitParcial, long file_size, DatabaseVendor database, String driverClass) {
        task = new Task(path, columns, tabela, bulksize, host, port, schema, user, password, ignoreFirst, replaceData, commitParcial, file_size, database, driverClass);

        taskThread = new Thread(task);
    }

    public static void startThread() {
        System.out.println("====== start ======");
        System.out.println("task: " + taskThread);
        System.out.println("taskThread: " + taskThread);

        if (taskThread != null) {
            taskThread.setDaemon(true);
            taskThread.start();
        }
    }

    public static void shutdown() {
        if (task != null) {
            task.cancel(true);
        }

        if (taskThread != null) {
            taskThread.interrupt();
        }
    }

    public static Task getTask() {
        return task;
    }

    public static Thread getTaskThread() {
        return taskThread;
    }

}
