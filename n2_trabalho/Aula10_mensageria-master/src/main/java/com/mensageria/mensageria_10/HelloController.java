package com.mensageria.mensageria_10;

import com.mensageria.mensageria_10.rabbitmq.Producer;
import com.mensageria.mensageria_10.model.Message;
import com.mensageria.mensageria_10.dao.MessageDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Optional;

public class HelloController {

    @FXML
    private Button btnAtualizar;

    @FXML
    private Button btnDeletar;

    @FXML
    private Button btnEnviar;

    @FXML
    private Button btnHistorico;

    @FXML
    private Label labelStatus;

    @FXML
    private ListView<String> listViewHistorico;

    @FXML
    private TextField textAlerta;

    @FXML
    private TextField textCidade;

    @FXML
    private TextField textID;



    @FXML
    protected void onEnviarButtonClick() {
        String cidade = textCidade.getText();
        String alerta = textAlerta.getText();

        if (cidade.isEmpty() || alerta.isEmpty()) {
            labelStatus.setText("Cidade e Alerta são campos obrigatórios.");
            return;
        }

        String message = "CREATE|" + cidade + "|" + alerta;
        Producer.sendMessage(message);
        labelStatus.setText("Alerta Enviado: " + cidade + " - " + alerta);
    }


    @FXML
    protected void onAtualizarButtonClick() {
        Long id = Long.parseLong(textID.getText());
        String cidade = textCidade.getText();
        String alerta = textAlerta.getText();

        if (id == null || cidade.isEmpty() || alerta.isEmpty()) {
            labelStatus.setText("ID, Cidade e Alerta são campos obrigatórios.");
            return;
        }

        String message = "UPDATE|" + cidade + "|" + alerta + "|" + id;
        Producer.sendMessage(message);
        labelStatus.setText("Alerta Atualizado: " + cidade + " - " + alerta);
    }


    @FXML
    protected void onDeletarButtonClick() {
        Long id = Long.parseLong(textID.getText());

        if (id == null) {
            labelStatus.setText("ID inválido.");
            return;
        }

        String message = "DELETE|" + id;
        Producer.sendMessage(message);
        labelStatus.setText("Alerta Deletado: ID " + id);
    }


    @FXML
    protected void onHistoricoButtonClick() {
        MessageDAO dao = new MessageDAO();
        Optional<List<Message>> messagesOpt = dao.findAll();

        if (messagesOpt.isPresent()) {
            List<Message> messages = messagesOpt.get();
            listViewHistorico.getItems().clear();

            if (!messages.isEmpty()) {
                for (Message message : messages) {
                    listViewHistorico.getItems().add(message.getId() + " - " + message.getCidade() + " - " + message.getAlerta());
                }
            } else {
                labelStatus.setText("Nenhum alerta no histórico.");
            }
        } else {
            labelStatus.setText("Erro ao buscar o histórico.");
        }
    }
}
