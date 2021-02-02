package br.com.certificado;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.Security;

public class CertificadoProperties {
    public static void initCertificadoProperties(Certificado certificado, InputStream cacerts) throws IOException, Exception{
        
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        System.clearProperty("javax.net.ssl.keyStore");
        System.clearProperty("javax.net.ssl.keyStorePassword");
        System.clearProperty("javax.net.ssl.trustStore");

        System.setProperty("jdk.tls.client.protocols", "TLSv1.2"); // Servidor do Sefaz RS

        switch (certificado.getTipoCertificado()) {
            case 0://windows
                System.setProperty("javax.net.ssl.keyStoreProvider", "SunMSCAPI");
                System.setProperty("javax.net.ssl.keyStoreType", "Windows-MY");
                System.setProperty("javax.net.ssl.keyStoreAlias", certificado.getNome());
                break;
            case 1://mac
                System.setProperty("javax.net.ssl.keyStoreType", "KeychainStore");
                System.setProperty("javax.net.ssl.keyStoreAlias", certificado.getNome());
                break;
            case 2://arquivo
                System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
                System.setProperty("javax.net.ssl.keyStore", certificado.getArquivo());
                break;
            case 3://arquivo bytes
                File cert = File.createTempFile("cert", ".pfx");
                Files.write(cert.toPath(),certificado.getArquivoBytes());
                System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
                System.setProperty("javax.net.ssl.keyStore", cert.getAbsolutePath());
                break;
            case 4://tipo A3
              throw new Exception("Token A3 não pode utilizar Configuração através de Properties.");
        }

        System.setProperty("javax.net.ssl.keyStorePassword", certificado.getSenha());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        //Extrair Cacert do Jar
        String cacert;
        try {
            File file = File.createTempFile("tempfile", ".tmp");
            OutputStream out = new FileOutputStream(file);
            int read;
            byte[] bytes = new byte[1024];

            while ((read = cacerts.read(bytes)) !=
                    -1) {
                out.write(bytes, 0, read);
            }
            out.close();
            cacert = file.getAbsolutePath();
            file.deleteOnExit();
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
        System.setProperty("javax.net.ssl.trustStore", cacert);
        
    }
}
