package com.myllenno.bluetoothdroid.configuration;

import android.app.Activity;

public interface Support_Document {

    /**
     * Retorna um valor booleano que indica se o dispositivo tem suporte ao bluetooth.
     *
     * @return
     */
    Boolean isSupported();

    /**
     * Verifica se o bluetooth está ativado no dispositivo.
     *
     * @return
     */
    Boolean isEnabled();

    /**
     * Pede permissão para ativar o bluetooth no dispositivo.
     *
     * @param activity
     */
    void enable(Activity activity);

    /**
     * Pede permissão para tornar o dispositivo visível por 300 segundos.
     *
     * @param activity
     */
    void enableVisibility(Activity activity);
}
