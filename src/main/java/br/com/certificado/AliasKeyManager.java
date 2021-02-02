package br.com.certificado;

import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509KeyManager;

/**
 *
 * @author SYSTECH Class que controle os alias dos certificados do tipo X509
 */
public class AliasKeyManager implements X509KeyManager{
    
    private KeyStore keyStore;
    private String alias;
    private String password;

    public AliasKeyManager(KeyStore keyStore, String alias, String password) {
        this.keyStore = keyStore;
        this.alias = alias;
        this.password = password;
    }
    
    @Override
    public String[] getClientAliases(String string, Principal[] prncpls) {
        return new String[]{alias};
    }

    @Override
    public String chooseClientAlias(String[] strings, Principal[] prncpls, Socket socket) {
        return alias;
    }

    @Override
    public String[] getServerAliases(String string, Principal[] prncpls) {
        return new String[]{alias};
    }

    @Override
    public String chooseServerAlias(String string, Principal[] prncpls, Socket socket) {
        return alias;
    }

    @Override
    public X509Certificate[] getCertificateChain(String string) {
        try {
            Certificate[] certificateChain = this.keyStore.getCertificateChain(string);
            X509Certificate[] x509Certificate = new X509Certificate[certificateChain.length];
            System.arraycopy(certificateChain, 0, x509Certificate, 0, certificateChain.length);//copia o conteudo de um array pro outro
            return x509Certificate;
        } catch (KeyStoreException ex) {
            System.out.println("Não foi possível carregar o keystore para o alias:" + alias);
        }
        return null;
    }

    @Override
    public PrivateKey getPrivateKey(String string) {
        try {
            return (PrivateKey) keyStore.getKey(string, password == null ? null : password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            System.out.println("Erro ao obter PrivateKey: ");
        }
        return null;
    }
    
}
