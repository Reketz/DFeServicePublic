/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfe.util;

import br.com.certificado.Certificado;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author SYSTECH
 */
public class AssinarUtil {

    public static final String INFINUT = "infInut";
    public static final String EVENTO = "evento";
    public static final String NFE = "NFe";
    private static NodeList elements;
    private static PrivateKey privateKey;
    private static KeyInfo keyInfo;

    public static String assinaDocNFe(Certificado certificado, String xml, String tipo) throws Exception {

        Document document = documentFactory(xml);
        // Crie um DOM XMLSignatureFactory que será usado para gerar o assinatura envolvida.
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
        ArrayList<Transform> transformList = signatureFactory(signatureFactory);
        loadCertificates(certificado, signatureFactory);
        document.getDocumentElement().normalize();
        for (int i = 0; i < document.getElementsByTagName(tipo).getLength(); i++) {
            assinarNFe(tipo, signatureFactory, transformList, privateKey, keyInfo, document, i);
        }

        return outputXML(document);

    }

    private static void assinarNFe(String tipo, XMLSignatureFactory fac, ArrayList<Transform> transformList, PrivateKey privateKey, KeyInfo ki, Document document, int indexNFe) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, MarshalException, XMLSignatureException {

        switch (tipo) {
            case EVENTO:
                elements = document.getElementsByTagName("infEvento");
                break;
            case INFINUT:
                elements = document.getElementsByTagName("infInut");
                break;
            default:
                elements = document.getElementsByTagName("infNFe");
                break;
        }

        org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(indexNFe);
        String id = el.getAttribute("Id");

        el.setIdAttribute("Id", true);
        // Cria uma referência para o documento envelopado (neste caso, você
        // estão assinando todo o documento, então um URI de "" significa que,
        // e também especifica o algoritmo de resumo SHA1 e a Transformação ENVELOPED.  
        Reference ref = fac.newReference("#" + id, fac.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
        // Cria o SignedInfo.
        SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                Collections.singletonList(ref));
        // Crie a XMLSignature, mas não assine ainda.
        XMLSignature signature = fac.newXMLSignature(si, ki);

        DOMSignContext dsc = null;
        // Crie um DOMSignContext e especifique o RSA PrivateKey e
        // localização do elemento pai XMLSignature resultante.
        if (tipo.equals(INFINUT)) {
            dsc = new DOMSignContext(privateKey, document.getFirstChild());
        } else {
            dsc = new DOMSignContext(privateKey, document.getElementsByTagName(tipo).item(indexNFe));
        }

        dsc.setBaseURI("ok");
        // Marshal, gerar e assinar a assinatura envolvida.
        signature.sign(dsc);
    }

    private static ArrayList<Transform> signatureFactory(XMLSignatureFactory signatureFactory) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        ArrayList<Transform> transformList = new ArrayList<>();
        TransformParameterSpec tps = null;
        Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);
        Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);

        transformList.add(envelopedTransform);
        transformList.add(c14NTransform);
        return transformList;
    }

    private static Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {
        // Instancie o documento a ser assinado.
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        return docBuilder.parse(new InputSource(new StringReader(xml)));
    }

    private static void loadCertificates(Certificado certificado, XMLSignatureFactory signatureFactory) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException, NoSuchProviderException, CertificateException, IOException {
        // Carregue o KeyStore e obtenha a chave de assinatura e o certificado.
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(certificado.getArquivo()), certificado.getSenha().toCharArray());
        KeyStore.PrivateKeyEntry pkEntry = null;
        Enumeration<String> aliasEnum = keyStore.aliases();
        String aliasKey = aliasEnum.nextElement();

        pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(aliasKey, new KeyStore.PasswordProtection("123456".toCharArray()));
        privateKey = pkEntry.getPrivateKey();
        // Crie o KeyInfo contendo o X509Data.
        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
        List<X509Certificate> x509Content = new ArrayList<>();

        x509Content.add((X509Certificate) pkEntry.getCertificate());
        X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
        keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
    }

    private static String outputXML(Document doc) throws Exception {

        String xml = "";

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc), new StreamResult(os));
            xml = os.toString();
            xml = xml.replaceAll("\\r\\n", "");
            xml = xml.replaceAll(" standalone=\"no\"", "");
        } catch (TransformerException e) {
            throw new Exception("Erro ao Transformar Documento:" + e.getMessage());
        }
        return xml;
    }

}
