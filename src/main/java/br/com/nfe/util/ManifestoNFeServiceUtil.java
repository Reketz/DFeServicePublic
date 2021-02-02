/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfe.util;

import br.com.certificado.Certificado;
import br.com.certificado.CertificadoService;
import br.com.nfe.envconfrecebto.TEnvEvento;
import br.com.nfe.envconfrecebto.TEvento;
import br.com.nfe.envconfrecebto.TRetEnvEvento;
import static br.com.nfe.util.TransformXmlUtil.marshal;
import static br.com.nfe.util.TransformXmlUtil.unmarshal;
import br.com.nfe.ws.recepcao.NFeRecepcaoEvento4Stub;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

/**
 *
 * @author SYSTECH
 */
public class ManifestoNFeServiceUtil {

    public static TRetEnvEvento eventoManifestoNFe(Certificado certificado, String tpAmb, String cUFAutor, String cnpjCpf, String nProt, String tpEvento, String descEvento, String chNFe, String urlWS, String cacerts) throws FileNotFoundException, Exception {
        System.out.println("url: " + urlWS);
        CertificadoService.initCertificado(certificado, new FileInputStream(cacerts));
        
        String dhEvento = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'-03:00'").format(LocalDateTime.now());        

        TEvento.InfEvento.DetEvento detEvento = new TEvento.InfEvento.DetEvento();
        detEvento.setDescEvento(descEvento);
        detEvento.setVersao("1.00");

        TEvento.InfEvento infEvento = new TEvento.InfEvento();
        infEvento.setId("ID" + tpEvento + chNFe + "01");
        infEvento.setCOrgao(cUFAutor);
        infEvento.setTpAmb(tpAmb);
        if (!cnpjCpf.isEmpty()) {
            infEvento.setCNPJ(cnpjCpf);
        } else {
            infEvento.setCPF(cnpjCpf);
        }
        infEvento.setChNFe(chNFe);
        infEvento.setDhEvento(dhEvento);
        infEvento.setTpEvento(tpEvento);
        infEvento.setNSeqEvento("1");
        infEvento.setVerEvento("1.00");
        infEvento.setDetEvento(detEvento);

        TEvento evento = new TEvento();
        evento.setVersao("1.00");
        evento.setInfEvento(infEvento);
        
        TEnvEvento envEvento = new TEnvEvento();
        envEvento.setIdLote("1");
        envEvento.setVersao("1.00");
        envEvento.getEvento().add(evento);
        
        String xml = marshal(envEvento).replace(" standalone=\"yes\"", "")
                .replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replaceAll("ns2:", "");
        System.out.println(xml);
        String xmlAssinado = AssinarUtil.assinaDocNFe(certificado, xml, AssinarUtil.EVENTO);
        System.out.println(xmlAssinado);
        OMElement ome = AXIOMUtil.stringToOM(xmlAssinado);

        NFeRecepcaoEvento4Stub.NfeDadosMsg dadosMsg = new NFeRecepcaoEvento4Stub.NfeDadosMsg();
        dadosMsg.setExtraElement(ome);

        NFeRecepcaoEvento4Stub stub = new NFeRecepcaoEvento4Stub(urlWS);
        /*HttpMethodParams methodParams = new HttpMethodParams();
        int retry = 0;
        methodParams.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(retry, retry != 0));
        stub._getServiceClient().getOptions().setProperty(HTTPConstants.HTTP_METHOD_PARAMS, methodParams);
        */
        NFeRecepcaoEvento4Stub.NfeResultMsg nfeRecepcaoEvento = stub.nfeRecepcaoEvento(dadosMsg);
        String response = nfeRecepcaoEvento.getExtraElement().toString();
        System.out.println(response);
        return (TRetEnvEvento) unmarshal(TRetEnvEvento.class, response);
    }

}
