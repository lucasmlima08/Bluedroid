package com.myllenno.bluetoothdroid.connection;

import java.util.ArrayList;

public class RequestGroup {

    /**
     * Objeto que foi recebido.
     */
    private Object object;

    /**
     * Clientes que receberão a requisição.
     */
    private ArrayList<Client> listClients;

    public RequestGroup(Object object, ArrayList<Client> listClients){
        this.object = object;
        this.listClients = listClients;
    }

    /**
     * Retorna o objeto que será enviado.
     *
     * @return
     */
    public Object getObject(){
        return object;
    }

    /**
     * Retorna a lista de clientes que receberão a requisição.
     *
     * @return
     */
    public ArrayList<Client> getListClients(){
        return listClients;
    }
}
