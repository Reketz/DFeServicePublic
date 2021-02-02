/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfe.util;

import br.com.certificado.Certificado;
import br.com.certificado.CertificadoService;
import br.com.nfe.cce.TEnvEvento;
import br.com.nfe.cce.TEvento;
import br.com.nfe.cce.TRetEnvEvento;
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
public class CCeServiceUtil {

    public static TRetEnvEvento eventoCCe(Certificado certificado, String tpAmb, String cUFAutor, String cnpjCpf, String nProt, String xJust, String chNFe, String urlWS, String cacerts) throws FileNotFoundException, Exception {
        CertificadoService.initCertificado(certificado, new FileInputStream(cacerts));
        String dhEvento = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'-03:00'").format(LocalDateTime.now());

        TEvento.InfEvento.DetEvento detEvento = new TEvento.InfEvento.DetEvento();
        detEvento.setDescEvento("Carta de Correcao");
        detEvento.setVersao("1.00");
        detEvento.setXCorrecao(xJust);
        detEvento.setXCondUso("A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e pode ser utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja relacionado com: I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca de preco, quantidade, valor da operacao ou da prestacao; II - a correcao de dados cadastrais que implique mudanca do remetente ou do destinatario; III - a data de emissao ou de saida.");

        TEvento.InfEvento infEvento = new TEvento.InfEvento();
        infEvento.setId("ID110110" + chNFe + "01");
        infEvento.setCOrgao(cUFAutor);
        infEvento.setTpAmb(tpAmb);
        if (!cnpjCpf.isEmpty()) {
            infEvento.setCNPJ(cnpjCpf);
        } else {
            infEvento.setCPF(cnpjCpf);
        }
        infEvento.setChNFe(chNFe);
        infEvento.setDhEvento(dhEvento);
        infEvento.setTpEvento("110110");
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
        NFeRecepcaoEvento4Stub.NfeResultMsg nfeRecepcaoEvento = stub.nfeRecepcaoEvento(dadosMsg);
        String response = nfeRecepcaoEvento.getExtraElement().toString();
        System.out.println(response);
        return (TRetEnvEvento) unmarshal(TRetEnvEvento.class, response);
    }

}
