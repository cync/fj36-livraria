package br.com.caelum.livraria.jms;

import java.io.Serializable;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import br.com.caelum.livraria.jaxb.SerializadorXml;
import br.com.caelum.livraria.modelo.Pedido;

@Component
@Lazy(true)
public class EnviadorMensagemJms implements Serializable {

	private static final long serialVersionUID = 1L;

	public void enviar(Pedido pedido) {

		
		try {
			Context localContext = getContextoLocal();
			Topic topico = (Topic) localContext.lookup("jms/topico/livraria");
			TopicConnectionFactory factory = (TopicConnectionFactory) localContext.lookup("jms/ConnectionFactory");

			TopicConnection conexao = factory.createTopicConnection();
			TopicSession session = conexao.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			
			//crie e envie a mensagem aqui
			TopicPublisher publisher = session.createPublisher(topico);
			System.out.println("JMS: Enviando pedido: " + pedido);
			
			String xml = new SerializadorXml().toXml(pedido);
			System.out.println(xml);
			Message msg = session.createTextMessage(xml);
			msg.setStringProperty("formato", pedido.getFormato());
			
			publisher.publish(msg);

			session.close();
			conexao.close();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		

	}

	
	private Context getContextoLocal() throws NamingException {
		InitialContext initialContext = new InitialContext();
		Context localContext = (Context) initialContext.lookup("java:comp/env");
		return localContext;
	}
	
}
