package tourGuide.webClient;


import org.junit.jupiter.api.Assertions;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tourGuide.TourGuideController;
import tourGuide.TourGuideService;
import user.VisitedLocation;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
/*        ( properties = {
        "server.port=8080",
})*/
class GpsUtilWebClientTest {

    private static final Logger logger = LoggerFactory.getLogger(GpsUtilWebClientTest.class);

    @Value("${server.port}")
    private int serverPort;

    @MockBean
    TourGuideController tourGuideController;

    private final String BASE_URL = "http://localhost:8080";

    private WebClient webClient;

    @Captor
    private ArgumentCaptor<ClientRequest> argumentCaptor;

    private ExchangeFunction exchangeFunction;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        logger.debug("serverPort : "+serverPort);
        MockitoAnnotations.initMocks(this);
        this.exchangeFunction = mock(ExchangeFunction.class);
        ClientResponse mockResponse = mock(ClientResponse.class);
        Mockito.when(this.exchangeFunction.exchange(this.argumentCaptor.capture())).thenReturn(Mono.just(mockResponse));
        this.webClient = WebClient
                .builder()
                .baseUrl(BASE_URL)
                .exchangeFunction(exchangeFunction)
                .build();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void getUserLocation() {
        logger.debug("serverPort : "+serverPort);


        this.webClient.get()
                .uri("/products")
                .exchange()
                .block(Duration.ofSeconds(1));
        verifyCalledUrl("/products");


/*        Mono<VisitedLocation> webClientMono = WebClient.create("http://localhost:"+serverPort)
                .get().uri( uriBuilder -> uriBuilder
                        .path("/getUserLocation")
                        .queryParam("userId", UUID.randomUUID() ).build())
                .retrieve()
                .bodyToMono(VisitedLocation.class);

        webClientMono.subscribe(System.out::println);

        verifyCalledUrl("/products/?name=AndroidPhone&color=black&deliveryDate=13/04/2019");*/


        //return webClientMono.block();
    }

/*    @org.junit.jupiter.api.Test
    void getAttractions() {
    }*/

    private void verifyCalledUrl(String relativeUrl) {
        ClientRequest request = this.argumentCaptor.getValue();
        logger.debug("request.url().toString() : "+request.url().toString());
        logger.debug("String.format(\"%s%s\", BASE_URL, relativeUrl) : "+String.format("%s%s", BASE_URL, relativeUrl));
        Assertions.assertEquals(String.format("%s%s", BASE_URL, relativeUrl), request.url().toString());
        Mockito.verify(this.exchangeFunction).exchange(request);
        verifyNoMoreInteractions(this.exchangeFunction);
    }
}