package com.oi.mailingfxapp.core;

import com.oi.mailingfxapp.config.ClassPathHacker;
import com.oi.mailingfxapp.config.Config;
import com.oi.mailingfxapp.enums.DatabaseVendor;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class MailingController {

    //Logs tab///////////////////////////
    @FXML
    ScrollPane mainScrollPane;
    /////////////////////////////////////
    //Executar tab///////////////////////
    @FXML
    TextField textFieldPath;

    @FXML
    Spinner<Integer> spinnerColunas;

    @FXML
    TextField textFieldTabela;

    @FXML
    RadioButton radioSubstituir;

    @FXML
    RadioButton radioIncrementar;

    @FXML
    CheckBox ignoreFirst;

    @FXML
    ProgressBar progressBar;

    @FXML
    ProgressIndicator progressIndicator;

    @FXML
    Button btnProcurar;
    
    @FXML
    Button btnExecutar;

    @FXML
    Text statusExec;
    /////////////////////////////////////
    //Configuração tab///////////////////
    @FXML
    TextField textFieldHost;

    @FXML
    TextField textFieldPort;

    @FXML
    TextField textFieldSid;

    @FXML
    TextField textFieldUser;

    @FXML
    PasswordField passowordFleldPwd;

    @FXML
    TextField textFieldDriver;

    @FXML
    RadioButton oracleDb;

    @FXML
    RadioButton mysqlDb;

    @FXML
    RadioButton postgreDb;

    @FXML
    RadioButton sqlserverDb;

    @FXML
    ChoiceBox<String> comboBlocks;

    @FXML
    CheckBox checkCommit;

    @FXML
    Button btnDriver;

    @FXML
    TextField textFieldDriverClass;

    @FXML
    Text statusConfig;
    /////////////////////////////////////

    Text textArea = new Text();

    private static final double EPSILON = 0.0000005;

    private Desktop desktop = Desktop.getDesktop();

    @FXML
    public void initialize() {
        //btnExecutar.setStyle("-fx-background-color: #00ff00");
        
        textFieldHost.setText(Config.getAppProps().getProperty("default_host") != null ? Config.getAppProps().getProperty("default_host") : textFieldHost.getText());
        textFieldPort.setText(Config.getAppProps().getProperty("default_port") != null ? Config.getAppProps().getProperty("default_port") : textFieldPort.getText());
        textFieldSid.setText(Config.getAppProps().getProperty("default_sid") != null ? Config.getAppProps().getProperty("default_sid") : textFieldSid.getText());
        textFieldUser.setText(Config.getAppProps().getProperty("default_user") != null ? Config.getAppProps().getProperty("default_user") : textFieldUser.getText());
        passowordFleldPwd.setText(Config.getAppProps().getProperty("default_password") != null ? new String(Base64.getDecoder().decode(Config.getAppProps().getProperty("default_password").getBytes())): passowordFleldPwd.getText());
        textFieldDriver.setText(Config.getAppProps().getProperty("default_driver") != null ? Config.getAppProps().getProperty("default_driver") : textFieldDriver.getText());
        checkCommit.setSelected(Config.getAppProps().getProperty("commit_parcial") != null ? Boolean.valueOf(Config.getAppProps().getProperty("commit_parcial")) : true);
        textFieldDriverClass.setText(Config.getAppProps().getProperty("default_classe_driver") != null ? Config.getAppProps().getProperty("default_classe_driver") : textFieldDriverClass.getText());

        String default_database = Config.getAppProps().getProperty("default_database");
        if (DatabaseVendor.ORACLE.getLabel().equals(default_database)) {
            oracleDb.setSelected(true);
        } else if (DatabaseVendor.MYSQL.getLabel().equals(default_database)) {
            mysqlDb.setSelected(true);
        } else if (DatabaseVendor.POSTGRESQL.getLabel().equals(default_database)) {
            postgreDb.setSelected(true);
        } else if (DatabaseVendor.SQLSERVER.getLabel().equals(default_database)) {
            sqlserverDb.setSelected(true);
        }

        comboBlocks.setItems(FXCollections.observableArrayList("100", "1000", "10000", "100000"));
        comboBlocks.setValue(Config.getAppProps().getProperty("default_blocks") != null ? Config.getAppProps().getProperty("default_blocks") : "1000");

        final FileChooser fileChooserMailing = new FileChooser();
        fileChooserMailing.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        btnProcurar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooserMailing.showOpenDialog(btnProcurar.getScene().getWindow());
                if (file != null) {
                    textFieldPath.setText(file.getAbsolutePath());
                }
            }
        });

        final FileChooser fileChooserDriver = new FileChooser();
        fileChooserDriver.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JAR Files", "*.jar")
        );
        btnDriver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooserDriver.showOpenDialog(btnDriver.getScene().getWindow());
                if (file != null) {
                    textFieldDriver.setText(file.getAbsolutePath());
                }
            }
        });

        oracleDb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) {
                if (oracleDb.isSelected()) {
                    radioIncrementar.setText("Incrementar (ignora duplicados)");
                }
            }
        });

        mysqlDb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) {
                if (mysqlDb.isSelected()) {
                    radioIncrementar.setText("Incrementar (ignora duplicados)");
                }
            }
        });

        postgreDb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) {
                if (postgreDb.isSelected()) {
                    radioIncrementar.setText("Incrementar (ignora duplicados)");
                }
            }
        });

        sqlserverDb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) {
                if (sqlserverDb.isSelected()) {
                    radioIncrementar.setText("Incrementar");
                }
            }
        });

        textArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();

                content.putString(textArea.getText());
                clipboard.setContent(content);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.getDialogPane().setMinWidth(500);
                alert.setTitle("");
                alert.setHeaderText("Stacktrace copiado para a área de transferência!");

                alert.showAndWait();
            }
        });

    }

    private void reset() {
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);

        mainScrollPane.setContent(null);

        progressIndicator.progressProperty().unbind();
        progressIndicator.setProgress(-1);

        statusExec.textProperty().unbind();
        statusExec.setText("");
        statusConfig.textProperty().unbind();
        statusConfig.setText("");
    }

    @FXML
    public void executar() {
        //textArea.setMinSize(640, 393);
        try {
            Alert alert = new Alert(AlertType.WARNING);
            alert.getDialogPane().setMinWidth(500);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Atenção!");

            Optional<ButtonType> result = null;
            if (radioSubstituir.isSelected()) {
                alert.setContentText("A aplicação irá agora deletar todos os dados da tabela.\n"
                        + "É recomendado realizar um backup da tabela atual antes de continuar.\n\n"
                        + "Se já realizou o backup ou deseja ignorar esse aviso, clique em OK.");

                result = alert.showAndWait();
            } else if (checkCommit.isSelected()) {
                alert.setContentText("Você selecionou a opção de comitar as alterações durante os INSERTS. (Commit parcial)\n"
                        + "Essa opção é recomendada para cargas de mailings muito grandes.\n"
                        + "Porém, é recomendado realizar um backup da tabela atual antes de continuar.\n\n"
                        + "Se já realizou o backup ou deseja ignorar esse aviso, clique em OK.");

                result = alert.showAndWait();
            }

            if ((result != null && result.isPresent() && result.get() == ButtonType.OK) || !(radioSubstituir.isSelected() || checkCommit.isSelected())) {
                long inicio = System.currentTimeMillis();

                if (textFieldDriver.getText() != null && !"".equals(textFieldDriver.getText())) {
                    ClassPathHacker.addToClasspath(new File(textFieldDriver.getText()));
                } else if (oracleDb.isSelected()) {
                    ClassPathHacker.addToClasspath(new File(".//ojdbc8-19.3.0.0.jar"));
                } else if (mysqlDb.isSelected()) {
                    ClassPathHacker.addToClasspath(new File(".//mysql-connector-java-8.0.21.jar"));
                } else if (postgreDb.isSelected()) {
                    ClassPathHacker.addToClasspath(new File(".//postgresql-42.2.16.jar"));
                } else if (sqlserverDb.isSelected()) {
                    ClassPathHacker.addToClasspath(new File(".//mssql-jdbc-8.4.1.jre8.jar"));
                }

                reset();

                File file = new File(textFieldPath.getText());

                DatabaseVendor database = null;
                if (oracleDb.isSelected()) {
                    database = DatabaseVendor.ORACLE;
                } else if (mysqlDb.isSelected()) {
                    database = DatabaseVendor.MYSQL;
                } else if (postgreDb.isSelected()) {
                    database = DatabaseVendor.POSTGRESQL;
                } else if (sqlserverDb.isSelected()) {
                    database = DatabaseVendor.SQLSERVER;
                }

                MailingFXApp.initTask(textFieldPath.getText(),
                        spinnerColunas.getValue(),
                        textFieldTabela.getText(),
                        Integer.valueOf(comboBlocks.getValue()),
                        textFieldHost.getText(),
                        textFieldPort.getText(),
                        textFieldSid.getText(),
                        textFieldUser.getText(),
                        passowordFleldPwd.getText(),
                        ignoreFirst.isSelected(),
                        radioSubstituir.isSelected(),
                        checkCommit.isSelected(),
                        file.length(),
                        database,
                        textFieldDriverClass.getText());

                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(MailingFXApp.getTask().progressProperty());

                statusExec.textProperty().unbind();
                statusExec.textProperty().bind(MailingFXApp.getTask().messageProperty());

                MailingFXApp.getTask().addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        Long registros = MailingFXApp.getTask().getValue();
                        statusExec.textProperty().unbind();
                        statusExec.setText("SUCESSO: " + registros + "registros");

                        progressIndicator.setProgress(1);
                    }
                });

                MailingFXApp.getTask().addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        Throwable e = MailingFXApp.getTask().getException();

                        statusExec.textProperty().unbind();
                        statusExec.setText("ERRO: " + e.getMessage() + " (verifique o log)");

                        StringBuilder sb = new StringBuilder();
                        sb.append(e.getMessage()).append("\n");
                        for (StackTraceElement s : e.getStackTrace()) {
                            sb.append(s.toString()).append("\n");
                        }
                        textArea.textProperty().unbind();
                        textArea.setText(sb.toString());

                        progressIndicator.setProgress(1);
                    }
                });

                MailingFXApp.getTask().addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        statusExec.textProperty().unbind();
                        if (checkCommit.isSelected()) {
                            statusExec.setText("CANCELADO: Os dados já inseridos permanecem na base!");
                        }
                        else{
                            statusExec.setText("CANCELADO!");
                        }

                        progressIndicator.setProgress(1);
                    }
                });

                MailingFXApp.startThread();
            } else {

            }

        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append(e.getMessage()).append("\n");
            for (StackTraceElement s : e.getStackTrace()) {
                sb.append(s.toString()).append("\n");
            }
            textArea.setText(sb.toString());
            progressIndicator.setProgress(1);

        } finally {
            mainScrollPane.setContent(textArea);

        }
    }

    @FXML
    public void cancelar() {
        MailingFXApp.shutdown();
    }

    @FXML
    public void saveConfig() {
        try {
            Config.getAppProps().setProperty("default_host", textFieldHost.getText());
            Config.getAppProps().setProperty("default_port", textFieldPort.getText());
            Config.getAppProps().setProperty("default_sid", textFieldSid.getText());
            Config.getAppProps().setProperty("default_user", textFieldUser.getText());
            Config.getAppProps().setProperty("default_password", new String(Base64.getEncoder().encode(passowordFleldPwd.getText().getBytes())));
            Config.getAppProps().setProperty("commit_parcial", String.valueOf(checkCommit.isSelected()));
            Config.getAppProps().setProperty("default_driver", textFieldDriver.getText());
            Config.getAppProps().setProperty("default_classe_driver", textFieldDriverClass.getText());
            Config.getAppProps().setProperty("default_blocks", comboBlocks.getValue());

            if (oracleDb.isSelected()) {
                Config.getAppProps().setProperty("default_database", DatabaseVendor.ORACLE.getLabel());
            } else if (mysqlDb.isSelected()) {
                Config.getAppProps().setProperty("default_database", DatabaseVendor.MYSQL.getLabel());
            } else if (postgreDb.isSelected()) {
                Config.getAppProps().setProperty("default_database", DatabaseVendor.POSTGRESQL.getLabel());
            } else if (sqlserverDb.isSelected()) {
                Config.getAppProps().setProperty("default_database", DatabaseVendor.SQLSERVER.getLabel());
            }

            Config.store();

            statusConfig.setText("Configurações salvas.");

        } catch (IOException ex) {
            statusConfig.setText("Erro salvando configurações:" + ex.getMessage());
        }

    }

}
