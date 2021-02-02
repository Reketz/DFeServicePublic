package br.com.certificado;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.protocol.Protocol;

public class CertificadoService {

    public static void initCertificado(Certificado certificado, InputStream cacerts) {
        KeyStore keyStore = getKeyStore(certificado);
        try {
            if (certificado.isAtivarProperties()) {
                CertificadoProperties.initCertificadoProperties(certificado, cacerts);
            } else {
                SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(keyStore, certificado.getNome(), certificado.getSenha(), cacerts, certificado.getSslProtocol());
                Protocol protocol = new Protocol("https", socketFactoryDinamico, 443);
                Protocol.registerProtocol("https", protocol);
            }
        } catch (Exception ex) {
            Logger.getLogger(CertificadoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static KeyStore getKeyStore(Certificado certificado) {
        try {
            KeyStore keyStore;
            switch (certificado.getTipoCertificado()) {
                case 0://windows
                    keyStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
                    keyStore.load(null, null);
                    return keyStore;
                case 1://mac
                    keyStore = KeyStore.getInstance("KeychainStore");
                    keyStore.load(null, null);
                    return keyStore;
                case 2://arquivo
                    File file = new File(certificado.getArquivo());
                    if (!file.exists()) {
                        throw new Exception("Certificado Digital não Encontrado");
                    }
                    keyStore = KeyStore.getInstance("PKCS12");
                    try (ByteArrayInputStream bs = new ByteArrayInputStream(Files.readAllBytes(file.toPath()))) {
                        keyStore.load(bs, certificado.getSenha().toCharArray());
                    }
                    return keyStore;
                case 3://arquivo_byte
                    keyStore = KeyStore.getInstance("PKCS12");
                    try (ByteArrayInputStream bs = new ByteArrayInputStream(certificado.getArquivoBytes())) {
                        keyStore.load(bs, certificado.getSenha().toCharArray());
                    }
                    return keyStore;
                case 4://A3
                    throw new Exception("Não suportado A3 - Em construição");

            }
        } catch (KeyStoreException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException ex) {
            Logger.getLogger(CertificadoService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CertificadoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Certificado getCertificadoPfx(String caminho, String password) {
        Certificado certificado = new Certificado();
        try {
            certificado.setArquivo(caminho);
            certificado.setSenha(password);
            certificado.setTipoCertificado(2);
            initDadosCertificado(certificado);
            return certificado;
        } catch (KeyStoreException ex) {
            Logger.getLogger(CertificadoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return certificado;
    }

    public static Certificado certificadoPfxBytes(byte[] certificadoBytes, String senha) {

        Certificado certificado = new Certificado();
        try {
            certificado.setArquivoBytes(certificadoBytes);
            certificado.setSenha(senha);
            certificado.setTipoCertificado(3);
            initDadosCertificado(certificado);
        } catch (KeyStoreException e) {
            System.out.println("Erro ao montar certificado!" + e);
        }
        return certificado;

    }

    private static void initDadosCertificado(Certificado certificado) throws KeyStoreException {
        KeyStore keyStore = getKeyStore(certificado);
        Enumeration<String> aliases = keyStore.aliases();
        String alias = aliases.nextElement();

        X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);

        certificado.setNome(alias);
        certificado.setVencimento(x509Certificate.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        certificado.setDataHoraVencimento(x509Certificate.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        certificado.setDiasRestantes(LocalDate.now().until(certificado.getVencimento(), ChronoUnit.DAYS));
        certificado.setValido(LocalDate.now().isBefore(certificado.getVencimento()));
    }

}
