package br.com.systech;

import br.com.certificado.Certificado;
import br.com.certificado.CertificadoService;
import br.com.nfe.consstatserv.TRetConsStatServ;
import br.com.nfe.envinfe.ObjectFactory;
import br.com.nfe.envinfe.TEnderEmi;
import br.com.nfe.envinfe.TEndereco;
import br.com.nfe.envinfe.TEnviNFe;
import br.com.nfe.envinfe.TNFe;
import br.com.nfe.envinfe.TNFe.InfNFe.Det;
import br.com.nfe.envinfe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.nfe.envinfe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr;
import br.com.nfe.envinfe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.nfe.envinfe.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.nfe.envinfe.TNFe.InfNFe.Det.Imposto.PIS.PISOutr;
import br.com.nfe.envinfe.TNFe.InfNFe.Total.ICMSTot;
import br.com.nfe.envinfe.TUf;
import br.com.nfe.envinfe.TUfEmi;
import br.com.nfe.util.DFeServiceUtil;
import static br.com.nfe.util.TransformXmlUtil.marshal;
import java.io.File;
import java.util.Collections;
import javax.xml.stream.XMLStreamException;
import org.ini4j.Ini;

public class StatusTest {

    public static void main(String[] args) throws XMLStreamException, Exception {
        String caminho = "C:\\Dominios\\CERTIFICADO.pfx";
        String senha = "123456";
        String cacerts = "C:\\temp\\producao.cacerts";
        Ini ini = new Ini(new File("D:\\Develop\\AppProjetos\\MODULOS\\DFeService\\src\\main\\resources\\nfeServidoresHom.ini"));
        Certificado certificado = CertificadoService.getCertificadoPfx(caminho, senha);
        TRetConsStatServ status = DFeServiceUtil.getStatus(certificado, "2", "25", ini.get("PB", "NfeStatusServico4"), cacerts);
        System.out.println(status.getXMotivo());
    }

    public static void createNFe() {

        int numeroNfe = 53;
        String cnpj = "24254079000145";
        String cnf = "12046258";
        String modelo = "55";
        int serie = 1;
        String tipoEmissao = "1";
        String chave = "2521012425407900014555001000000053212046258";
        String cDv = "6";

        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();
        ide.setCUF("25");
        ide.setCNF(cnf);
        ide.setNatOp("NOTA FISCAL CONSUMIDOR ELETRONICA");
        ide.setMod(modelo);
        ide.setSerie(String.valueOf(serie));

        ide.setNNF(String.valueOf(numeroNfe));
        ide.setDhEmi("2021-01-04T08:49:43-03:00");
        ide.setTpNF("1");
        ide.setIdDest("1");
        ide.setCMunFG("2516904");
        ide.setTpImp("1");
        ide.setTpEmis(tipoEmissao);
        ide.setCDV(cDv);
        ide.setTpAmb("2");
        ide.setFinNFe("1");
        ide.setIndFinal("1");
        ide.setIndPres("1");
        ide.setProcEmi("0");
        ide.setVerProc("1.0");

        TNFe.InfNFe.Emit emit = new TNFe.InfNFe.Emit();
        emit.setCNPJ(cnpj);
        emit.setXNome("SYSTECH SOLUCOES");

        TEnderEmi enderEmit = new TEnderEmi();
        enderEmit.setXLgr("RUA JOSE BARBOSA");
        enderEmit.setNro("SN");
//        enderEmit.setXCpl("QD 17 LT 01-02-03");
        enderEmit.setXBairro("SAO JOSE");
        enderEmit.setCMun("2516904");
        enderEmit.setXMun("UIRAUNA");
        enderEmit.setUF(TUfEmi.valueOf("PB"));
        enderEmit.setCEP("58915000");
        enderEmit.setCPais("1058");
        enderEmit.setXPais("Brasil");
        enderEmit.setFone("86156025");
        emit.setEnderEmit(enderEmit);

        emit.setIE("162675496");
        emit.setCRT("1");

        TNFe.InfNFe.Dest dest = new TNFe.InfNFe.Dest();
        dest.setCNPJ("76417005000186");
        dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");

        TEndereco enderDest = new TEndereco();
        enderDest.setXLgr("Rua: Teste");
        enderDest.setNro("0");
        enderDest.setXBairro("TESTE");
        enderDest.setCMun("4109708");
        enderDest.setXMun("IBAITI");
        enderDest.setUF(TUf.valueOf("PR"));
        enderDest.setCEP("84900000");
        enderDest.setCPais("1058");
        enderDest.setXPais("Brasil");
        enderDest.setFone("4845454545");
        dest.setEnderDest(enderDest);

        dest.setEmail("teste@test");
        dest.setIndIEDest("9");

        Det det = new Det();
        det.setNItem("1");

        TNFe.InfNFe.Det.Prod prod = new TNFe.InfNFe.Det.Prod();
        prod.setCProd("7898480650104");
        prod.setCEAN("7898480650104");
        prod.setXProd("NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
        prod.setNCM("27101932");
        prod.setCEST("0600500");
        prod.setIndEscala("S");
        prod.setCFOP("5405");
        prod.setUCom("UN");
        prod.setQCom("1.0000");
        prod.setVUnCom("13.0000");
        prod.setVProd("13.00");
        prod.setCEANTrib("7898480650104");
        prod.setUTrib("UN");
        prod.setQTrib("1.0000");
        prod.setVUnTrib("13.0000");
        prod.setIndTot("1");

        det.setProd(prod);

        TNFe.InfNFe.Det.Imposto imposto = new TNFe.InfNFe.Det.Imposto();

        ICMS icms = new ICMS();

        ICMS.ICMSSN500 icms500 = new ICMS.ICMSSN500();
        icms500.setOrig("0");
        icms500.setCSOSN("500");

        icms.setICMSSN500(icms500);

        PIS pis = new PIS();
        PISOutr pisOutr = new PISOutr();
        pisOutr.setCST("49");
        pisOutr.setVBC("13.00");
        pisOutr.setPPIS("0.00");
        pisOutr.setVPIS("0.00");
        pis.setPISOutr(pisOutr);

        COFINS cofins = new COFINS();
        COFINSOutr cofinsOutr = new COFINSOutr();
        cofinsOutr.setCST("49");
        cofinsOutr.setVBC("13.00");
        cofinsOutr.setPCOFINS("0.00");
        cofinsOutr.setVCOFINS("0.00");
        cofins.setCOFINSOutr(cofinsOutr);

        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));
        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));
        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));

        det.setImposto(imposto);

        TNFe.InfNFe.Total total = new TNFe.InfNFe.Total();
        ICMSTot icmstot = new ICMSTot();
        icmstot.setVBC("0.00");
        icmstot.setVICMS("0.00");
        icmstot.setVICMSDeson("0.00");
        icmstot.setVFCP("0.00");
        icmstot.setVFCPST("0.00");
        icmstot.setVFCPSTRet("0.00");
        icmstot.setVBCST("0.00");
        icmstot.setVST("0.00");
        icmstot.setVProd("13.00");
        icmstot.setVFrete("0.00");
        icmstot.setVSeg("0.00");
        icmstot.setVDesc("0.00");
        icmstot.setVII("0.00");
        icmstot.setVIPI("0.00");
        icmstot.setVIPIDevol("0.00");
        icmstot.setVPIS("0.00");
        icmstot.setVCOFINS("0.00");
        icmstot.setVOutro("0.00");
        icmstot.setVNF("13.00");
        total.setICMSTot(icmstot);

        TNFe.InfNFe.Transp transp = new TNFe.InfNFe.Transp();
        transp.setModFrete("9");

        TNFe.InfNFe.Pag pag = new TNFe.InfNFe.Pag();
        TNFe.InfNFe.Pag.DetPag detPag = new TNFe.InfNFe.Pag.DetPag();
        detPag.setTPag("01");
        detPag.setVPag("13.00");
        pag.getDetPag().add(detPag);

        TNFe.InfNFe infNFe = new TNFe.InfNFe();
        infNFe.setId("NFe" + chave + cDv);
        infNFe.setVersao("4.00");
        infNFe.setIde(ide);
        infNFe.setEmit(emit);
        infNFe.setDest(dest);
        infNFe.getDet().addAll(Collections.singletonList(det));

        infNFe.setTotal(total);

        infNFe.setTransp(transp);

        infNFe.setPag(pag);

        TNFe nfe = new TNFe();
        nfe.setInfNFe(infNFe);

        TEnviNFe enviNFe = new TEnviNFe();
        enviNFe.setIdLote("1");
        enviNFe.setIndSinc("1");
        enviNFe.setVersao("4.00");
        enviNFe.getNFe().add(nfe);
        String xml = marshal(enviNFe).replace(" standalone=\"yes\"", "").replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
        System.out.println(xml);
    }

}
