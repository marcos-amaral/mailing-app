/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oi.mailingfxapp;

import com.oi.mailingfxapp.dao.MailingFXAppDao;
import com.oi.mailingfxapp.enums.DatabaseVendor;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mmouraam
 */
public class Task extends javafx.concurrent.Task<Long> {

    private final String host;
    private final String port;
    private final String schema;
    private final String user;
    private final String password;

    private final String path;
    private final int columns;
    private final String tabela;
    private final int bulksize;
    private boolean ignoreFirst;
    private final boolean replaceData;
    private final boolean commitParcial;
    private final long file_size;
    private final DatabaseVendor database;
    private final String driverClass;

    private Connection connection;

    public Task(String path, int columns, String tabela, int bulksize, String host, String port, String schema, String user, String password, boolean ignoreFirst, boolean replaceData, boolean commitParcial, long file_size, DatabaseVendor database, String driverClass) {
        this.host = host;
        this.port = port;
        this.schema = schema;
        this.user = user;
        this.password = password;
        this.path = path;
        this.columns = columns;
        this.tabela = tabela;
        this.bulksize = bulksize;
        this.ignoreFirst = ignoreFirst;
        this.file_size = file_size;
        this.replaceData = replaceData;
        this.commitParcial = commitParcial;
        this.database = database;
        this.driverClass = driverClass;
    }

    @Override
    protected Long call() throws Exception {
        connection = null;
        PreparedStatement preparedStatement = null;

        FileInputStream inputStream = null;
        Scanner sc = null;

        long registros = 0;

        try {
            this.updateMessage("Criando conexão...");
            connection = MailingFXAppDao.getConnection(host, port, schema, user, password, database, driverClass);

            connection.setAutoCommit(false);

            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");

            String sql;

            if (replaceData) {
                sql = "TRUNCATE TABLE " + tabela;

                this.updateMessage(sql);

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                MailingFXAppDao.closePreparedStatement(preparedStatement);
            }

            int i = 1;

            preparedStatement = MailingFXAppDao.getInsertStatement(tabela, replaceData, columns, connection, database);

            long workDone = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (ignoreFirst) {
                    ignoreFirst = false;

                } else {
                    registros++;

                    String[] split = line.split(";");
                    preparedStatement.clearParameters();
                    for (int j = 0; j < split.length; j++) {
                        preparedStatement.setString(j + 1, split[j]);
                    }
                    preparedStatement.addBatch();

                    if (sc.ioException() != null) {
                        throw sc.ioException();
                    }

                    if (i == bulksize) {
                        preparedStatement.clearParameters();
                        preparedStatement.executeLargeBatch();
                        if (commitParcial) {
                            connection.commit();
                        }
                        MailingFXAppDao.closePreparedStatement(preparedStatement);
                        preparedStatement = MailingFXAppDao.getInsertStatement(tabela, replaceData, columns, connection, database);
                        i = 0;

                        this.updateMessage("INSERT: " + registros + " registros");
                    }
                    i++;

                }
                workDone += line.getBytes().length;
                this.updateProgress(workDone, file_size);
            }

            if (i < bulksize && i > 0) {
                preparedStatement.clearParameters();
                preparedStatement.executeLargeBatch();
            }

            connection.commit();

            this.updateProgress(file_size, file_size);

        } catch (IOException | ClassNotFoundException | SQLException e) {

            this.updateProgress(file_size, file_size);

            if (connection != null && !commitParcial) {
                this.updateMessage("ERRO: executando rollback...aguarde");
                connection.rollback();
            }

            registros = 0;

            this.updateMessage("ERRO: " + e.getMessage() + " (verifique o log)");

            throw e;

        } finally {
            MailingFXAppDao.closePreparedStatement(preparedStatement);
            MailingFXAppDao.returnConnection(connection);

            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }

        return registros;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (connection != null && !commitParcial) {
            try {
                if (!connection.isClosed()) {
                    this.updateMessage("ERRO: executando rollback...aguarde");
                    connection.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return super.cancel(mayInterruptIfRunning);
    }

}
