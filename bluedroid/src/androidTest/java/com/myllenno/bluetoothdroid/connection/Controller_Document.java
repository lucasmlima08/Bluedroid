package com.myllenno.bluetoothdroid.connection;

import java.util.ArrayList;
import java.util.logging.Handler;

public interface Controller_Document {

    /**
     * Adiciona um cliente na lista de clientes.
     *
     * @param client
     */
    void addClient(Client client);

    /**
     * Adiciona uma lista de clientes na lista de clientes.
     *
     * @param clients
     */
    void addClient(ArrayList<Client> clients);

    /**
     * Retorna o primeiro cliente conectado.
     * Remove o cliente da lista
     *
     * @return
     */
    Client getFirstClient();

    /**
     * Retorna a lista com todos os clientes conectados.
     * Apaga toda a lista de clientes conectados.
     *
     * @return
     */
    ArrayList<Client> getAllClients();

    /**
     * Apaga a lista com todos os clientes conectados.
     *
     * @return
     */
    void clearAllClients();

    /**
     * Adiciona uma requisição para envio.
     *
     * @param request
     */
    void addRequestSend(Object request);

    /**
     * Adiciona uma lista de requisições para envio.
     *
     * @param requests
     */
    void addRequestSend(ArrayList<Object> requests);

    /**
     * Retorna a lista de todas as requisições que serão enviadas.
     * Remove a requisição da lista.
     *
     * @return
     */
    Object getFirstRequestSend();

    /**
     * Retorna a lista de todas as requisições que serão enviadas.
     * Limpa a lista de requisições que serão enviadas.
     *
     * @return
     */
    ArrayList<Object> getAllRequestsSend();

    /**
     * Remove todas as requisições de envio.
     *
     * @return
     */
    void clearAllRequestSend();

    /**
     * Retorna a primeira requisição que não foi enviada.
     * Apaga a requisição.
     *
     * @return
     */
    Object getFirstRequestNotSend();

    /**
     * Retorna a lista com todas as requisições não enviadas.
     * Apaga a lista de requisições não enviadas.
     *
     * @return
     */
    ArrayList<Object> getAllRequestsNotSend();

    /**
     * Remove todas as requisições não enviadas.
     *
     * @return
     */
    void clearAllRequestNotSend();

    /**
     * Retorna a primeira requisição recebida da lista.
     *
     * @return
     */
    RequestSimple getFirstRequestReceived();

    /**
     * Retorna a lista com todas as requisições recebidas.
     *
     * @return
     */
    ArrayList<RequestSimple> getAllRequestsReceived();

    /**
     * Remove todas as requisições recebidas.
     *
     * @return
     */
    void clearAllRequestReceived();

    /**
     * Define o tipo de objeto que será recebido.
     *
     * @param objectType
     */
    void setObjectType(Object objectType);

    /**
     * Adiciona a classe de eventos para receber informações de quando um evento ocorre.
     *
     * @param handler
     */
    void setHandler(Handler handler);

    /**
     * Recebe uma requisição de um cliente.
     * Pode se usado em um thread.
     *
     * @param client
     * @return
     */
    RequestSimple receiveRequest(Client client);

    /**
     * Recebe as requisições de todos os clientes conectados.
     * Pode ser usado em um thread.
     */
    void receiveAllRequests();

    /**
     * Envia as requisições para todos os clientes da lista de clientes.
     * Pode ser usado em um thread.
     */
    void sendAllRequests();

    /**
     * Envia apenas uma requisição para uma lista de clientes.
     * Pode ser usado em um thread.
     *
     * @param request
     */
    void sendRequest(Object request);

    /**
     * Verifica se o cliente está disponível.
     *
     * @param client
     * @return
     */
    boolean isAvailable(Client client);

    /**
     * Envia apenas uma requisição para um único cliente.
     * Pode ser usado em um thread.
     *
     * @param request
     */
    void sendRequest(RequestSimple request);

    /**
     * Atualiza a lista de clientes.
     */
    void refreshClients();

    /**
     * Apaga todos os clientes e as listas de requisições dos clientes.
     */
    void clearAll();

    /**
     * Remove um cliente da lista de clientes na posição indicada.
     *
     * @param index
     */
    void removeClient(int index);
}
