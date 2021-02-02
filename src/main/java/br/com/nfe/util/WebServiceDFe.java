package br.com.nfe.util;

import br.com.certificado.Certificado;
import org.ini4j.Ini;

public class WebServiceDFe {
    private Certificado certificado;
    private Ini ini;
    private String cacerts;

    public WebServiceDFe(Certificado certificado, Ini ini, String cacerts) {
        this.certificado = certificado;
        this.ini = ini;
        this.cacerts = cacerts;
    }

    public Certificado getCertificado() {
        return certificado;
    }

    public void setCertificado(Certificado certificado) {
        this.certificado = certificado;
    }

    public Ini getIni() {
        return ini;
    }

    public void setIni(Ini ini) {
        this.ini = ini;
    }

    public String getCacerts() {
        return cacerts;
    }

    public void setCacerts(String cacerts) {
        this.cacerts = cacerts;
    }
    
    
}
