package translator.web.ws;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;
import translator.Application;
import translator.exception.TranslatorException;
import translator.web.ws.schema.GetTranslationRequest;
import translator.web.ws.schema.GetTranslationResponse;

import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT, classes = Application.class)
public class TranslatorEndpointErrorTest {

	private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

	@LocalServerPort
	private int port;

	@Before
	public void init() throws Exception {
		marshaller.setPackagesToScan(ClassUtils.getPackageName(GetTranslationRequest.class));
		marshaller.afterPropertiesSet();
	}

    @Rule
    public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testLanguageNotSupported() {
        GetTranslationRequest request = new GetTranslationRequest();
        String langNotSupported = "01";
        request.setLangFrom("en");
        request.setLangTo(langNotSupported);
        request.setText("This is a test of translation service");
        thrown.expectMessage("Lang:"+langNotSupported+" is not supported");
        Object response = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
                + port + "/ws", request);
    }
}
