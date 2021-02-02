package br.com.nfe.util;

import br.com.certificado.Certificado;
import br.com.certificado.CertificadoService;
import br.com.nfe.conscad.TConsCad;
import br.com.nfe.conscad.TRetConsCad;
import br.com.nfe.conscad.TUfCons;
import br.com.nfe.conssitnfe.TConsSitNFe;
import br.com.nfe.conssitnfe.TRetConsSitNFe;
import br.com.nfe.consstatserv.TConsStatServ;
import br.com.nfe.consstatserv.TRetConsStatServ;
import br.com.nfe.distnfe.DistDFeInt;
import br.com.nfe.enveventocancnfe.TEnvEvento;
import br.com.nfe.enveventocancnfe.TEvento;
import br.com.nfe.enveventocancnfe.TRetEnvEvento;
import br.com.nfe.envinfe.TRetEnviNFe;
import br.com.nfe.inutnfe.TInutNFe;
import br.com.nfe.inutnfe.TRetInutNFe;
import br.com.nfe.retdistdfeint.RetDistDFeInt;

import static br.com.nfe.util.TransformXmlUtil.marshal;
import static br.com.nfe.util.TransformXmlUtil.unmarshal;
import br.com.nfe.ws.autorizacao.NFeAutorizacao4Stub;
import br.com.nfe.ws.cadconsultacadastro.CadConsultaCadastro4Stub;
import br.com.nfe.ws.consulta.NFeConsultaProtocolo4Stub;
import br.com.nfe.ws.distribuicao.NFeDistribuicaoDFeStub;
import br.com.nfe.ws.inutilizacao.NFeInutilizacao4Stub;
import br.com.nfe.ws.recepcao.NFeRecepcaoEvento4Stub;
import br.com.nfe.ws.status.NFeStatusServico4Stub;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;

public class DFeServiceUtil {

    public static TRetConsStatServ getStatus(Certificado certificado, String tpAmb, String cUF, String urlWS, String cacerts) throws XMLStreamException, RemoteException, FileNotFoundException {
        CertificadoService.initCertificado(certificado, new FileInputStream(cacerts));
        String xml = buildXmlStatus(tpAmb, cUF);
        System.out.println(xml);
        OMElement ome = AXIOMUtil.stringToOM(xml);
        NFeStatusServico4Stub.NfeDadosMsg dadosMsg = new NFeStatusServico4Stub.NfeDadosMsg();
        dadosMsg.setExtraElement(ome);
        NFeStatusServico4Stub stub = new NFeStatusServico4Stub(urlWS);
        NFeStatusServico4Stub.NfeResultMsg result = stub.nfeStatusServicoNF(dadosMsg);
        System.out.println("[XML-RETORNO]: " + result.getExtraElement().toString());
        TRetConsStatServ response = (TRetConsStatServ) unmarshal(TRetConsStatServ.class, result.getExtraElement().toString());
        return response;
    }

    public static TRetConsCad consCad(Certificado certificado, String uf, String cnpjCpf, String cacerts, String urlWS) throws XMLStreamException, AxisFault, RemoteException, FileNotFoundException {
        CertificadoService.initCertificado(certificado, new FileInputStream(cacerts));

        String xml = buildConsCadastro(uf, cnpjCpf);
        System.out.println(xml);
        OMElement ome = AXIOMUtil.stringToOM(xml);
        CadConsultaCadastro4Stub.NfeDadosMsg dadosMsg = new CadConsultaCadastro4Stub.NfeDadosMsg();
        dadosMsg.setExtraElement(ome);

        CadConsultaCadastro4Stub result = new CadConsultaCadastro4Stub(urlWS);
        CadConsultaCadastro4Stub.NfeResultMsg consultaCadastro = result.consultaCadastro(dadosMsg);
        String response = consultaCadastro.getExtraElement().toString();
        System.out.println(response);
        return (TRetConsCad) unmarshal(TRetConsCad.class, response);
    }

    public static RetDistDFeInt distNFe(Certificado certificado, String tpAmb, String cUFAutor, String cnpjCpf, String nsu, String ultNsu, String chNFe, String urlWS, String cacerts) throws FileNotFoundException, XMLStreamException, AxisFault, RemoteException {
        CertificadoService.initCertificado(certificado, new FileInputStream(cacerts));

        String xml = buildXmlDist(tpAmb, cUFAutor, cnpjCpf, nsu, ultNsu, chNFe);
        System.out.println(xml);

        OMElement ome = AXIOMUtil.stringToOM(xml);

        NFeDistribuicaoDFeStub.NfeDadosMsg_type0 dados = new NFeDistribuicaoDFeStub.NfeDadosMsg_type0();
        dados.setExtraElement(ome);

        NFeDistribuicaoDFeStub.NfeDistDFeInteresse dadosMsg = new NFeDistribuicaoDFeStub.NfeDistDFeInteresse();
        dadosMsg.setNfeDadosMsg(dados);

        NFeDistribuicaoDFeStub stub = new NFeDistribuicaoDFeStub(urlWS);
        NFeDistribuicaoDFeStub.NfeDistDFeInteresseResponse nfeDistDFeInteresse = stub.nfeDistDFeInteresse(dadosMsg);

        String response = nfeDistDFeInteresse.getNfeDistDFeInteresseResult().getExtraElement().toString();

        return (RetDistDFeInt) unmarshal(RetDistDFeInt.class, response);
    }

    public static TRetConsSitNFe consSitNFe(Certificado certificado, String tpAmb, String chNFe, String cacerts, String urlWS) throws XMLStreamException, AxisFault, RemoteException, FileNotFoundException {
        CertificadoService.initCertificado(certificado, new FileInputStream(cacerts));
        String xml = buildXmlConsSit(tpAmb, chNFe);
        System.out.println(xml);

        OMElement ome = AXIOMUtil.stringToOM(xml);

        NFeConsultaProtocolo4Stub.NfeDadosMsg dadosMsg = new NFeConsultaProtocolo4Stub.NfeDadosMsg();
        dadosMsg.setExtraElement(ome);

        NFeConsultaProtocolo4Stub stub = new NFeConsultaProtocolo4Stub(urlWS);
        NFeConsultaProtocolo4Stub.NfeResultMsg nfeConsultaNF = stub.nfeConsultaNF(dadosMsg);

        String response = nfeConsultaNF.getExtraElement().toString();
        System.out.println(response);
        return (TRetConsSitNFe) unmarshal(TRetConsSitNFe.class, response);
    }

    public static TRetEnviNFe enviNFe(Certificado certificado, String cacerts, String xml, boolean validar, String urlWS) throws Exception {
        CertificadoService.initCertificado(certificado, new FileInputStream(cacerts));
        String xmlAssinado = AssinarUtil.assinaDocNFe(certificado, xml, AssinarUtil.NFE);
        System.out.println(xmlAssinado);
        if (validar) {
            new ValidarUtil().validaXml(xmlAssinado, ServicosEnum.ENVIO);
        }
        OMElement ome;
        ome = AXIOMUtil.stringToOM(xmlAssinado);

        Iterator<?> children = ome.getChildrenWithLocalName("NFe");
        while (children.hasNext()) {
            OMElement omElementNFe = (OMElement) children.next();
            if ((omElementNFe != null) && ("NFe".equals(omElementNFe.getLocalName()))) {
                omElementNFe.addAttribute("xmlns", "http://www.portalfiscal.inf.br/nfe", null);
            }
        }

        NFeAutorizacao4Stub.NfeDadosMsg dadosMsg = new NFeAutorizacao4Stub.NfeDadosMsg();
        dadosMsg.setExtraElement(ome);
        NFeAutorizacao4Stub stub = new NFeAutorizacao4Stub(urlWS);
        NFeAutorizacao4Stub.NfeResultMsg result = stub.nfeAutorizacaoLote(dadosMsg);
        System.out.println(result.getExtraElement().toString());
        TRetEnviNFe response = (TRetEnviNFe) unmarshal(TRetEnviNFe.class, result.getExtraElement().toString());
        return response;
    }

    public static TRetInutNFe inutNFe(Certificado certificado, String cacerts, String tpAmb, String cUF, String CNPJ, String mod, String serie, String nNFIni, String nNFFin, String xJust, String urlWS) throws Exception {
        CertificadoService.initCertificado(certificado, new FileInputStream(cacerts));
        String xml = buildXmlInut(tpAmb, cUF, CNPJ, mod, serie, nNFIni, nNFFin, xJust);
        System.out.println(xml);
        String xmlAssinado = AssinarUtil.assinaDocNFe(certificado, xml, AssinarUtil.INFINUT);
        System.out.println(xmlAssinado);

        OMElement ome;
        ome = AXIOMUtil.stringToOM(xmlAssinado);

        NFeInutilizacao4Stub.NfeDadosMsg dadosMsg = new NFeInutilizacao4Stub.NfeDadosMsg();
        dadosMsg.setExtraElement(ome);

        NFeInutilizacao4Stub sub = new NFeInutilizacao4Stub(urlWS);
        NFeInutilizacao4Stub.NfeResultMsg nfeInutilizacaoNF = sub.nfeInutilizacaoNF(dadosMsg);
        System.out.println(nfeInutilizacaoNF.getExtraElement().toString());
        TRetInutNFe response = (TRetInutNFe) unmarshal(TRetInutNFe.class, nfeInutilizacaoNF.getExtraElement().toString());
        return response;
    }

    public static TRetEnvEvento eventoCanc(Certificado certificado, String tpAmb, String cUFAutor, String cnpjCpf, String nProt, String xJust, String chNFe, boolean validar, String urlWS, String cacerts) throws Exception {
        CertificadoService.initCertificado(certificado, new FileInputStream(cacerts));
        String xml = buildXmlCanc(tpAmb, cUFAutor, cnpjCpf, nProt, xJust, chNFe);
        System.out.println(xml);
        String xmlAssinado = AssinarUtil.assinaDocNFe(certificado, xml, AssinarUtil.EVENTO);
        System.out.println(xmlAssinado);
        if (validar) {
            new ValidarUtil().validaXml(xmlAssinado, ServicosEnum.CANCELAMENTO);
        }
        OMElement ome = AXIOMUtil.stringToOM(xmlAssinado);

        NFeRecepcaoEvento4Stub.NfeDadosMsg dadosMsg = new NFeRecepcaoEvento4Stub.NfeDadosMsg();
        dadosMsg.setExtraElement(ome);

        NFeRecepcaoEvento4Stub stub = new NFeRecepcaoEvento4Stub(urlWS);
        NFeRecepcaoEvento4Stub.NfeResultMsg nfeRecepcaoEvento = stub.nfeRecepcaoEvento(dadosMsg);
        String response = nfeRecepcaoEvento.getExtraElement().toString();
        System.out.println(response);
        return (TRetEnvEvento) unmarshal(TRetEnvEvento.class, response);
    }

    //metodos responsáveis por criar os xml
    public static String buildXmlStatus(String tpAmb, String cUF) {
        TConsStatServ status = new TConsStatServ();
        status.setVersao("4.00");
        status.setTpAmb(tpAmb);
        status.setCUF(cUF);
        status.setXServ("STATUS");
        return marshal(status).replace(" standalone=\"yes\"", "");
    }

    public static String buildConsCadastro(String uf, String cnpjCpf) {
        TConsCad.InfCons infCons = new TConsCad.InfCons();
        infCons.setXServ("CONS-CAD");
        infCons.setUF(TUfCons.valueOf(uf));
        if (cnpjCpf.length() == 14) {
            infCons.setCNPJ(cnpjCpf);
        } else {
            infCons.setCPF(cnpjCpf);
        }
        TConsCad consCad = new TConsCad();
        consCad.setVersao("2.00");
        consCad.setInfCons(infCons);

        return marshal(consCad).replace(" standalone=\"yes\"", "");
    }

    public static String buildXmlDist(String tpAmb, String cUFAutor, String cnpjCpf, String nsu, String ultNsu, String chNFe) {
        DistDFeInt dist = new DistDFeInt();
        dist.setVersao("1.01");
        dist.setTpAmb(tpAmb);
        dist.setCUFAutor(cUFAutor);
        if (cnpjCpf.length() == 14) {
            dist.setCNPJ(cnpjCpf);
        } else {
            dist.setCPF(cnpjCpf);
        }

        if (!ultNsu.isEmpty()) {
            DistDFeInt.DistNSU ultDistNSU = new DistDFeInt.DistNSU();
            ultDistNSU.setUltNSU(ultNsu);
            dist.setDistNSU(ultDistNSU);
        }

        if (!nsu.isEmpty()) {
            DistDFeInt.ConsNSU nsuCons = new DistDFeInt.ConsNSU();
            nsuCons.setNSU(nsu);
            dist.setConsNSU(nsuCons);
        }

        if (!chNFe.isEmpty()) {
            DistDFeInt.ConsChNFe consChNFe = new DistDFeInt.ConsChNFe();
            consChNFe.setChNFe(chNFe);
            dist.setConsChNFe(consChNFe);
        }

        return marshal(dist).replace(" standalone=\"yes\"", "");
    }

    public static String buildXmlConsSit(String tpAmb, String chNFe) {
        TConsSitNFe consSit = new TConsSitNFe();
        consSit.setVersao("4.00");
        consSit.setTpAmb(tpAmb);
        consSit.setXServ("CONSULTAR");
        consSit.setChNFe(chNFe);

        return marshal(consSit).replace(" standalone=\"yes\"", "")
                .replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replaceAll("ns2:", "");
    }

    public static String buildXmlInut(String tpAmb, String cUF, String CNPJ, String mod, String serie, String nNFIni, String nNFFin, String xJust) {
        String ano = DateTimeFormatter.ofPattern("yy").format(LocalDate.now());
        TInutNFe.InfInut infInut = new TInutNFe.InfInut();

        infInut.setId("ID" + cUF + ano + CNPJ + mod
                + String.format("%3s", serie).replace(" ", "0")
                + String.format("%9s", nNFIni).replace(" ", "0")
                + String.format("%9s", nNFFin).replace(" ", "0"));
        infInut.setTpAmb(tpAmb);
        infInut.setXServ("INUTILIZAR");
        infInut.setCUF(cUF);
        infInut.setAno(ano);
        infInut.setCNPJ(CNPJ);
        infInut.setMod(mod);
        infInut.setSerie(serie);
        infInut.setNNFIni(nNFIni);
        infInut.setNNFFin(nNFFin);
        infInut.setXJust(xJust);

        TInutNFe inut = new TInutNFe();
        inut.setInfInut(infInut);
        inut.setVersao("4.00");

        return marshal(inut)
                .replace(" standalone=\"yes\"", "")
                .replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replaceAll("ns2:", "");
    }

    public static String buildXmlCanc(String tpAmb, String cUFAutor, String cnpjCpf, String nProt, String xJust, String chNFe) {
        String dhEvento = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'-03:00'").format(LocalDateTime.now());

        TEvento.InfEvento.DetEvento detEvento = new TEvento.InfEvento.DetEvento();
        detEvento.setDescEvento("Cancelamento");
        detEvento.setVersao("1.00");
        detEvento.setNProt(nProt);
        detEvento.setXJust(xJust);

        TEvento.InfEvento infEvento = new TEvento.InfEvento();
        infEvento.setId("ID110111" + chNFe + "01");
        infEvento.setCOrgao(cUFAutor);
        infEvento.setTpAmb(tpAmb);
        if (!cnpjCpf.isEmpty()) {
            infEvento.setCNPJ(cnpjCpf);
        } else {
            infEvento.setCPF(cnpjCpf);
        }
        infEvento.setChNFe(chNFe);
        infEvento.setDhEvento(dhEvento);
        infEvento.setTpEvento("110111");
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

        return marshal(envEvento).replace(" standalone=\"yes\"", "")
                .replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replaceAll("ns2:", "");
    }

}
