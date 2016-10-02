package com.myllenno.bluetoothdroid.connection;

import java.util.ArrayList;

public interface RequestGroup_Document {

    /**
     * Retorna o objeto que será enviado.
     *
     * @return
     */
    Object getObject();

    /**
     * Retorna a lista de clientes que receberão a requisição.
     *
     * @return
     */
    ArrayList<Client_Document> getListClients();
}
