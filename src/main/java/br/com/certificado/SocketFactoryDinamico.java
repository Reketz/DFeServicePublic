package br.com.certificado;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 *
 * @author SYSTECH
 */
public class SocketFactoryDinamico implements ProtocolSocketFactory {

    private KeyStore keyStore;
    private String alias;
    private String password;
    private InputStream fileCacerts;
    private SSLContext ssl;

    public SocketFactoryDinamico(KeyStore keyStore, String alias, String password, InputStream fileCacerts, String sslProtocol) {
        this.keyStore = keyStore;
        this.alias = alias;
        this.password = password;
        this.fileCacerts = fileCacerts;
        this.ssl = createSSLContext(sslProtocol);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress clienteHost, int clientePost) throws IOException, UnknownHostException {
        return ssl.getSocketFactory().createSocket(host, port, clienteHost, clientePost);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        Socket socket = ssl.getSocketFactory().createSocket();
        socket.bind(new InetSocketAddress(localAddress, localPort));
        socket.connect(new InetSocketAddress(host, port), 60000);
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return ssl.getSocketFactory().createSocket(host, port);
    }

    private SSLContext createSSLContext(String sslProtocol) {
        try {
            KeyManager[] kms = new KeyManager[]{new AliasKeyManager(keyStore, alias, password)};
            TrustManager[] tms = createTrustManager();
            SSLContext sslContext = SSLContext.getInstance(sslProtocol);
            sslContext.init(kms, tms, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            System.out.println("Erro ao criar ssl!");
        }
        return null;
    }

    private TrustManager[] createTrustManager() {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(fileCacerts, "changeit".toCharArray());//"changeit" é a senha padrão para JKS
            factory.init(trustStore);
            TrustManager[] trustManagers = factory.getTrustManagers();
            return trustManagers;
        } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException ex) {
            System.out.println("Erro ao obter TrustManager!" + ex);
        }
        return null;
    }

}
