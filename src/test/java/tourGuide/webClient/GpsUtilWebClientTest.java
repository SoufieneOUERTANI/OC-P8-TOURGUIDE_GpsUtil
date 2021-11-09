package tourGuide.webClient;


import Constants.Constants;
import gpsUtil.GpsUtilService;
import gpsUtil.location.Attraction;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
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
/*
    @Value("${server.port}")
    private int serverPort;*/

    @Autowired
    GpsUtilWebClient gpsUtilWebClient;

    private WebClient webClient;
    private final String BASE_URL = "http://localhost:8083";
    private ExchangeFunction exchangeFunction;

    @Captor
    private ArgumentCaptor<ClientRequest> argumentCaptor;

    @BeforeEach
    void setUp() {
//        logger.debug("serverPort : "+serverPort);
/*        MockitoAnnotations.initMocks(this);
        this.exchangeFunction = mock(ExchangeFunction.class);
        ClientResponse mockResponse = mock(ClientResponse.class);
        Mockito.when(this.exchangeFunction.exchange(this.argumentCaptor.capture())).thenReturn(Mono.just(mockResponse));
        this.webClient = WebClient
                .builder()
                .baseUrl(BASE_URL)
                .exchangeFunction(exchangeFunction)
                .build();*/
    }

    @AfterEach
    void tearDown() {
    }
    @Ignore
    @Test
//    void getUserLocation() {
    void webClient_verifyCalledUrl_1() {

        MockitoAnnotations.initMocks(this);
        this.exchangeFunction = mock(ExchangeFunction.class);
        ClientResponse mockResponse = mock(ClientResponse.class);
        Mockito.when(this.exchangeFunction.exchange(this.argumentCaptor.capture())).thenReturn(Mono.just(mockResponse));
        this.webClient = WebClient
                .builder()
                .baseUrl(BASE_URL)
                .exchangeFunction(exchangeFunction)
                .build();

//        logger.debug("serverPort : "+serverPort);

        this.webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/getUserLocation")
                                .queryParam("userId", 8).build()
                )
                .exchange()
                .block(Duration.ofSeconds(1));
        verifyCalledUrl("/getUserLocation?userId=8");

    }

    @Test
    void webClient_verifyCalledUrl_2() {

        MockitoAnnotations.initMocks(this);
        this.exchangeFunction = mock(ExchangeFunction.class);
        ClientResponse mockResponse = mock(ClientResponse.class);
        Mockito.when(this.exchangeFunction.exchange(this.argumentCaptor.capture())).thenReturn(Mono.just(mockResponse));
        this.webClient = WebClient
                .builder()
                .baseUrl(BASE_URL)
                .exchangeFunction(exchangeFunction)
                .build();

//        logger.debug("serverPort : "+serverPort);

        this.webClient.get().uri(Constants.GPS_UTIL_USER_LOCATION, 8)
                .exchange()
                .block(Duration.ofSeconds(1));
        verifyCalledUrl("/getUserLocation?userId=8");

    }


    @org.junit.jupiter.api.Test
    void getUserLocation() {
        UUID userId = UUID.randomUUID();
        this.webClient = WebClient
                .builder()
                .baseUrl(BASE_URL)
                //.exchangeFunction(exchangeFunction)
                .build();
        //gpsUtilWebClient=new GpsUtilWebClient(webClient, exchangeFunction);
        gpsUtilWebClient=new GpsUtilWebClient();
        VisitedLocation visitedLocation = gpsUtilWebClient.getUserLocation(userId);
        org.assertj.core.api.Assertions.assertThat(visitedLocation)
                .isNotNull()
                .hasFieldOrPropertyWithValue("userId", userId)
                .hasFieldOrProperty("location")
                .hasFieldOrProperty("timeVisited");

    }
    @Test
    void getAttractions() {
        //gpsUtilWebClient=new GpsUtilWebClient(webClient, exchangeFunction);
        gpsUtilWebClient=new GpsUtilWebClient();
        List<Attraction> attractionList= gpsUtilWebClient.getAttractions();
        org.assertj.core.api.Assertions.assertThat(attractionList)
                .isNotNull()
                .isNotEmpty();

    }

    private void verifyCalledUrl(String relativeUrl) {
        ClientRequest request = this.argumentCaptor.getValue();
        logger.debug("request.url().toString() : "+request.url().toString());
        logger.debug("String.format(\"%s%s\", BASE_URL, relativeUrl) : "+String.format("%s%s", BASE_URL, relativeUrl));
        Assertions.assertEquals(String.format("%s%s", BASE_URL, relativeUrl), request.url().toString());
        Mockito.verify(this.exchangeFunction).exchange(request);
        verifyNoMoreInteractions(this.exchangeFunction);
    }
}