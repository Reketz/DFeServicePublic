package br.com.systech;

import br.com.nfe.inutnfe.TRetInutNFe;
import static br.com.nfe.util.TransformXmlUtil.unmarshal;

public class InutHom {
    public static void main(String[] args) {
        String xml = "<retInutNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"4.00\"><infInut><tpAmb>2</tpAmb><verAplic>SVRS201905151442</verAplic><cStat>252</cStat><xMotivo>Rejeicao: Ambiente informado diverge do Ambiente de recebimento</xMotivo><cUF>25</cUF><ano>21</ano><CNPJ>11079193000134</CNPJ><mod>55</mod><serie>1</serie><nNFIni>1</nNFIni><nNFFin>1</nNFFin><dhRecbto>2021-01-18T07:43:01-03:00</dhRecbto></infInut></retInutNFe>";
        System.out.println(xml);
        TRetInutNFe response = (TRetInutNFe) unmarshal(TRetInutNFe.class, xml);
        System.out.println(response.getInfInut().getXMotivo());
    }
}
