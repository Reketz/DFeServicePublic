/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfe.util;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author SYSTECH
 */
public class TransformXmlUtil {

    public static Object unmarshal(Class clazz, String stringXml) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(
                    new StreamSource(new StringReader(stringXml))
            );
        } catch (JAXBException e) {
            System.err.println(e);
        }
        return null;
    }

    public static String marshal(Object object) {
        final StringWriter out = new StringWriter();
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(
                    javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.FALSE
            );
//            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
            //marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); remove o cabeçalho
            marshaller.marshal(object, new StreamResult(out));
        } catch (PropertyException e) {
            System.err.println(e);
        } catch (JAXBException e) {
            System.err.println(e);
        }
        return out.toString();
    }
}
