package com.myllenno.bluetoothdroid.connection;

public class RequestSimple {

    /**
     * Clientes que receberão a requisição.
     */
    private Object object;

    /**
     * Cliente que enviou a requisição.
     */
    private Client client;

    public RequestSimple(Object object, Client client){
        this.object = object;
        this.client = client;
    }

    /**
     * Retorna o objeto que foi recebido.
     *
     * @return
     */
    public Object getObject(){
        return object;
    }

    /**
     * Retorna o objeto que foi recebido.
     *
     * @return
     */
    public Client getClient(){
        return client;
    }
}
