package br.com.caelum.livraria.jaxb;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.com.caelum.livraria.modelo.Pedido;


public class SerializadorXml {

	public String toXml(Pedido pedido) {
		try {
			Marshaller mar = JAXBContext.newInstance(Pedido.class).createMarshaller();
			StringWriter writer = new StringWriter();
			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			mar.marshal(pedido, writer);
			
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

}
