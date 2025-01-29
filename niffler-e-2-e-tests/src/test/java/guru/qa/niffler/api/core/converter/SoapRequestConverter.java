package guru.qa.niffler.api.core.converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import okhttp3.RequestBody;
import org.w3c.dom.Document;
import retrofit2.Converter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static guru.qa.niffler.api.core.converter.SoapConverterFactory.XML;

final class SoapRequestConverter<T> implements Converter<T, RequestBody> {

  final JAXBContext context;
  final String namespace;

  SoapRequestConverter(JAXBContext context, String namespace) {
    this.context = context;
    this.namespace = namespace;
  }

  @Override
  public RequestBody convert(final T value) throws IOException {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      SOAPMessage message = MessageFactory.newInstance().createMessage();
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

      Marshaller marshaller = context.createMarshaller();
      marshaller.marshal(value, document);

      message.getSOAPBody().addDocument(document);
      SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
      envelope.addNamespaceDeclaration("tns", namespace);
      message.writeTo(os);
      return RequestBody.create(XML, os.toByteArray());
    } catch (SOAPException | ParserConfigurationException | JAXBException e) {
      throw new RuntimeException(e);
    }
  }
}
