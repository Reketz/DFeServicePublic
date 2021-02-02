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

public class CancProd {

    public static void main(String[] args) {
        try {
            String caminho = "C:\\Dominios\\CERTIFICADO.pfx";
            String senha = "123456";
            String cacerts = "C:\\Dominios\\app\\conf\\cacerts";
            Ini ini = new Ini(new File("C:\\Dominios\\app\\conf\\ini\\nfeServidoresProd.ini"));
            Certificado certificado = CertificadoService.getCertificadoPfx(caminho, senha);
            
            String urlWS = ini.get("PB", "nfeRecepcaoEvento4");
            System.out.println(urlWS);

            DFeServiceUtil.eventoCanc(certificado,
                    "1",
                    "25",
                    "24254079000145",
                    "325210003128043",
                    "DADOS DE IMPOSTOS FALTANDO INFORMACAO",
                    "25210224254079000145550010000001941177703861",
                    true,
                    urlWS, cacerts);

        } catch (IOException ex) {
            Logger.getLogger(CancProd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CancProd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
