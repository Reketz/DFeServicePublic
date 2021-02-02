package br.com.nfe.testes;

import br.com.certificado.Certificado;
import br.com.certificado.CertificadoService;
import br.com.nfe.enveventocancnfe.TRetEnvEvento;
import br.com.nfe.util.DFeServiceUtil;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Ini;

public class CancHom {

    public static void main(String[] args) {
        try {
            String caminho = "C:\\Dominios\\CERTIFICADO.pfx";
            String senha = "123456";
            String cacerts = "C:\\temp\\homologacao.cacerts";
            Ini ini = new Ini(new File("D:\\Develop\\AppProjetos\\MODULOS\\DFeService\\src\\main\\resources\\nfeServidoresHom.ini"));
            Certificado certificado = CertificadoService.getCertificadoPfx(caminho, senha);

            String urlWS = ini.get("PB", "NFeRecepcaoEvento4");
            System.out.println(urlWS);

            TRetEnvEvento envNFe = DFeServiceUtil.eventoCanc(certificado,
                    "2",
                    "25",
                    "24254079000145",
                    "123456789123456",
                    "DADOS DIVERGENTES",
                    "25200624254079000145550010000000431194463383",
                    true,
                    urlWS, cacerts);
            System.out.println(envNFe.getXMotivo());
        } catch (IOException ex) {
            Logger.getLogger(CancHom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CancHom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
