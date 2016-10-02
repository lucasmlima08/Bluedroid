package com.myllenno.bluetoothdroid.connection;

import com.myllenno.bluetoothdroid.report.HandlerDialog;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Controller {

    /**
     * Classe de comunicação do evento ocorrido.
     */
    private HandlerDialog handlerDialog;

    /**
     * Instância responsável por controlar os eventos da classe.
     */
    private Handler handler;

    /**
     * Tipo de objeto recebido pela aplicação.
     */
    private Object objectType;

    /**
     * Classe de comunicação do cliente.
     */
    private Client client;

    /**
     * Classe de comunicação do servidor.
     */
    private Server server;

    /**
     * Lista de clientes conectados.
     */
    private ArrayList<Client> listClients;

    /**
     * Lista de requisições para envio.
     */
    private ArrayList<Object> listRequestsSend;

    /**
     * Lista de requisições que não puderão ser enviadas.
     */
    private ArrayList<Object> listRequestsNotSend;

    /**
     * Lista de requisições recebidas.
     */
    private ArrayList<RequestSimple> listRequestsReceived;

    /**
     * Recebe o identificador único do cliente e do servidor, e o pin de comunicação.
     *
     * @param uuid
     * @param pin
     */
    public Controller(String uuid, String pin) {
        listClients = new ArrayList<>();
        listRequestsSend = new ArrayList<>();
        listRequestsReceived = new ArrayList<>();
        listRequestsNotSend = new ArrayList<>();

        client = new Client(uuid);
        server = new Server(uuid, pin);
        handlerDialog = new HandlerDialog();

        objectType = new Object();
        handler = null;
    }

    /**
     * Adiciona a classe de eventos para receber informações de quando um evento ocorre.
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
        client.setHandler(handler);
        server.setHandler(handler);
    }

    /**
     * Define o tipo de objeto que será recebido.
     *
     * @param objectType
     */
    public void setObjectType(Object objectType) {
        this.objectType = objectType;
    }

    /**
     * Adiciona um cliente na lista de clientes.
     *
     * @param client
     */
    public void addClient(Client client) {
        listClients.add(client);
    }

    /**
     * Adiciona uma lista de clientes na lista de clientes.
     *
     * @param clients
     */
    public void addClient(ArrayList<Client> clients) {
        listClients.addAll(clients);
    }

    /**
     * Retorna o primeiro cliente conectado.
     * Remove o cliente da lista
     *
     * @return
     */
    public Client getFirstClient() {
        Client client = null;
        if (!listClients.isEmpty()) {
            client = listClients.get(0);
            listClients.remove(0);
        }
        return client;
    }

    /**
     * Retorna a lista com todos os clientes conectados.
     * Apaga toda a lista de clientes conectados.
     *
     * @return
     */
    public ArrayList<Client> getAllClients() {
        ArrayList<Client> listClients = new ArrayList<>();
        listClients.addAll(this.listClients);
        clearAllClients();
        return listClients;
    }

    /**
     * Fecha a conexão com todos os clientes.
     * Remove todos os clientes da lista.
     *
     * @return
     */
    public void clearAllClients() {
        for (int i=0; i < listClients.size(); i++){
            listClients.get(i).closeConnection();
        }
        listClients.clear();
    }

    /**
     * Adiciona uma requisição para envio.
     *
     * @param request
     */
    public void addRequestSend(Object request) {
        listRequestsSend.add(request);
    }

    /**
     * Adiciona uma lista de requisições para envio.
     *
     * @param requests
     */
    public void addRequestSend(ArrayList<Object> requests) {
        listRequestsSend.addAll(requests);
    }

    /**
     * Retorna a primeira requisição que será enviada.
     * Remove a requisição da lista.
     *
     * @return
     */
    public Object getFirstRequestSend() {
        Object object = null;
        if (!listRequestsSend.isEmpty()) {
            object = listRequestsSend.get(0);
            listRequestsSend.remove(0);
        }
        return object;
    }

    /**
     * Retorna a lista de todas as requisições que serão enviadas.
     * Limpa a lista de requisições que serão enviadas.
     *
     * @return
     */
    public ArrayList<Object> getAllRequestsSend() {
        ArrayList<Object> listRequestsSend = new ArrayList<>();
        listRequestsSend.addAll(this.listRequestsSend);
        clearAllRequestSend();
        return listRequestsSend;
    }

    /**
     * Remove todas as requisições de envio da lista.
     *
     * @return
     */
    public void clearAllRequestSend() {
        listRequestsSend.clear();
    }

    /**
     * Retorna a primeira requisição que não foi enviada.
     * Remove a requisição da lista.
     *
     * @return
     */
    public Object getFirstRequestNotSend() {
        Object request = null;
        if (!listRequestsNotSend.isEmpty()) {
            request = listRequestsNotSend.get(0);
            listRequestsNotSend.remove(0);
        }
        return request;
    }

    /**
     * Retorna a lista com todas as requisições não enviadas.
     * Remove as requisições da lista de requisições não enviadas.
     *
     * @return
     */
    public ArrayList<Object> getAllRequestsNotSend() {
        ArrayList<Object> listRequestsNotSend = new ArrayList<>();
        listRequestsNotSend.addAll(this.listRequestsNotSend);
        clearAllRequestNotSend();
        return listRequestsNotSend;
    }

    /**
     * Remove todas as requisições não enviadas da lista.
     *
     * @return
     */
    public void clearAllRequestNotSend() {
        listRequestsNotSend.clear();
    }

    /**
     * Retorna a primeira requisição recebida da lista.
     * Remove a requisição da lista.
     *
     * @return
     */
    public RequestSimple getFirstRequestReceived() {
        RequestSimple request = null;
        if (!listRequestsReceived.isEmpty()) {
            request = listRequestsReceived.get(0);
            listRequestsReceived.remove(0);
        }
        return request;
    }

    /**
     * Retorna a lista com todas as requisições recebidas.
     * Remove todas as requisições da lista de requisições recebidas.
     *
     * @return
     */
    public ArrayList<RequestSimple> getAllRequestsReceived() {
        ArrayList<RequestSimple> listRequestsReceived = new ArrayList<>();
        listRequestsReceived.addAll(this.listRequestsReceived);
        clearAllRequestReceived();
        return listRequestsReceived;
    }

    /**
     * Remove todas as requisições recebidas.
     *
     * @return
     */
    void clearAllRequestReceived() {
        listRequestsReceived.clear();
    }

    /**
     * Recebe uma requisição de um cliente.
     * Pode se usado em um thread.
     *
     * @param client
     * @return
     */
    private RequestSimple receiveRequest(Client client) {
        try {
            // Recebe a requisição do cliente conectado e salva na lista.
            Object object = client.receiveData(objectType);
            RequestSimple requestReceived = new RequestSimple(object, client);
            return requestReceived;
        } catch (Exception e) {
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Recebe as requisições de todos os clientes conectados.
     * Pode ser usado em um thread.
     */
    public void receiveAllRequests() {
        for (int i = 0; i < listClients.size(); i++) {
            // Verifica se está conectado.
            if (listClients.get(i).getClient() != null && listClients.get(i).getClient().isConnected()) {
                // Recebe a requisição do dispositivo conectado.
                RequestSimple requestReceive = receiveRequest(listClients.get(i));
                listRequestsReceived.add(requestReceive);
            } else {
                // Remove o cliente da lista.
                removeClient(i);
                i--;
            }
        }
        handlerPublish(Level.INFO, handlerDialog.REQUESTS_RECEIVED_ALL);
    }

    /**
     * Envia apenas uma requisição para uma lista de clientes.
     * Pode ser usado em um thread.
     *
     * @param request
     */
    private void sendRequest(Object request) {

        // Verifica se é uma requisição simples.
        if (request instanceof RequestSimple) {
            RequestSimple requestSimple = (RequestSimple) request;
            // Primeiro passo: Verifica se o cliente está disponível.
            if (requestSimple.getClient().isAvailable()) {
                // Segundo passo: Envia a requisição.
                requestSimple.getClient().sendData(requestSimple.getObject());
            // Caso o cliente não esteja disponível.
            } else {
                // Guarda a requisição não enviada.
                listRequestsNotSend.add(requestSimple);
                handlerPublish(Level.INFO, handlerDialog.DATA_NOT_SEND);
            }

        // Verifica se é uma requisição em grupo.
        } else if (request instanceof RequestGroup) {

            RequestGroup requestGroup = (RequestGroup) request;
            // Primeiro passo: Percorre a lista de clientes do grupo.
            for (int i = 0; i < requestGroup.getListClients().size(); i++) {
                // Segundo passo: Verifica se o cliente está disponível.
                if (requestGroup.getListClients().get(i).isAvailable()) {
                    // Terceiro passo: Envia a requisição.
                    requestGroup.getListClients().get(i).sendData(requestGroup.getObject());
                // Caso o cliente não esteja disponível.
                } else {
                    // Guarda a requisição não enviada.
                    RequestSimple requestSimple = new RequestSimple(requestGroup.getObject(), requestGroup.getListClients().get(i));
                    listRequestsNotSend.add(requestSimple);
                    handlerPublish(Level.INFO, handlerDialog.DATA_NOT_SEND);
                }
            }
        // Verifica se é uma requisição desconhecida.
        } else {
            handlerPublish(Level.INFO, handlerDialog.REQUESTS_UNKNOWN);
        }
    }

    /**
     * Envia as requisições para todos os clientes da lista de clientes.
     * Pode ser usado em um thread.
     */
    public void sendAllRequests() {
        // Primeiro passo: Percorre a lista de requisições enquanto houver requisição.
        while (!listRequestsSend.isEmpty()) {
            try {
                // Segundo passo: Envia para o método de envio da requisição.
                sendRequest(listRequestsSend.get(0));
                listRequestsSend.remove(0);
            } catch (Exception e) {
                handlerPublish(Level.SEVERE, e.toString());
                e.printStackTrace();
            }
        }
        handlerPublish(Level.INFO, handlerDialog.REQUESTS_SENT_ALL);
    }

    /**
     * Envia apenas uma requisição para um único cliente.
     * Pode ser usado em um thread.
     *
     * @param request
     */
    private void sendRequest(RequestSimple request) {
        // Primeiro passo: Verifica se o cliente está disponível.
        if (request.getClient().isAvailable()) {
            // Segundo passo: Envia a requisição.
            request.getClient().sendData(request.getObject());
        // Caso o cliente não esteja disponível.
        } else {
            // Guarda a requisição não enviada.
            RequestSimple requestSimple = new RequestSimple(request.getObject(), request.getClient());
            listRequestsNotSend.add(requestSimple);
            handlerPublish(Level.INFO, handlerDialog.DATA_NOT_SEND);
        }
    }

    /**
     * Atualiza a lista de clientes.
     */
    public void refreshClients() {
        // Primeiro passo: Percorre a lista de clientes.
        for (int i = 0; i < listClients.size(); i++) {
            // Segundo passo: Verifica se está indisponível.
            if (!listClients.get(i).isAvailable()) {
                // Terceiro passo: Remove da lista de clientes.
                removeClient(i);
                i--;
            }
        }
    }

    /**
     * Apaga todos os clientes e as listas de requisições dos clientes.
     */
    public void clearAll() {
        clearAllClients();
        clearAllRequestSend();
        clearAllRequestNotSend();
        clearAllRequestReceived();
    }

    /**
     * Remove um cliente da lista de clientes na posição indicada.
     *
     * @param index
     */
    public void removeClient(int index) {
        if (listClients.size() > index) {
            listClients.remove(index);
            handlerPublish(Level.WARNING, handlerDialog.CLIENT_REMOVED);
        }
    }

    /**
     * Publica o evento ocorrido.
     *
     * @param level
     * @param dialog
     */
    private void handlerPublish(Level level, String dialog) {
        if (handler != null) {
            handler.publish(new LogRecord(level, dialog));
        }
    }
}
