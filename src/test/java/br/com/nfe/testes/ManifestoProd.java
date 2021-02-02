package br.com.nfe.testes;

import br.com.certificado.Certificado;
import br.com.certificado.CertificadoService;
import br.com.nfe.envconfrecebto.TRetEnvEvento;
import br.com.nfe.util.ManifestoNFeServiceUtil;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Ini;

public class ManifestoProd {

    public static void main(String[] args) {
        try {
            String caminho = "C:\\Dominios\\CERTIFICADO.pfx";
            String senha = "123456";
            String cacerts = "C:\\temp\\producao.cacerts";
            Ini ini = new Ini(new File("C:\\temp\\ws\\nfeServidoresHom.ini"));
            Certificado certificado = CertificadoService.getCertificadoPfx(caminho, senha);
            String urlWS = ini.get("PB", "NFeRecepcaoEvento4");
            System.out.println(urlWS);

            TRetEnvEvento eventoManifestoNFe = ManifestoNFeServiceUtil.eventoManifestoNFe(certificado,
                    "1",
                    "91",
                    "24254079000145",
                    "",
                    "210210",
                    "Ciencia da Operacao",
                    "25210107827111000115550010001409131518005122",
                    urlWS, cacerts);
            System.out.println(eventoManifestoNFe.getRetEvento().get(0).getInfEvento().getXMotivo());
        } catch (IOException ex) {
            Logger.getLogger(ManifestoProd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManifestoProd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
