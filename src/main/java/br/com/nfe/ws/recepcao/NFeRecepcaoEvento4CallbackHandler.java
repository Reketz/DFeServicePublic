/**
 * NFeRecepcaoEvento4CallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.9  Built on : Nov 16, 2018 (12:05:37 GMT)
 */
package br.com.nfe.ws.recepcao;


/**
 *  NFeRecepcaoEvento4CallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class NFeRecepcaoEvento4CallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public NFeRecepcaoEvento4CallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public NFeRecepcaoEvento4CallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for nfeRecepcaoEvento method
     * override this method for handling normal response from nfeRecepcaoEvento operation
     */
    public void receiveResultnfeRecepcaoEvento(
        br.com.nfe.ws.recepcao.NFeRecepcaoEvento4Stub.NfeResultMsg result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from nfeRecepcaoEvento operation
     */
    public void receiveErrornfeRecepcaoEvento(java.lang.Exception e) {
    }
}
