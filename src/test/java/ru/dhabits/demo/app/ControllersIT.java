package ru.dhabits.demo.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ControllersIT {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    public void exampleUserSuccessTest() {

        final ResponseEntity<String> exchange = restTemplate.exchange(
                "/test",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class
        );

        assertThat(exchange.getStatusCode())
                .as("Код ответа должен быть 200 OK")
                .isEqualTo(HttpStatus.OK);

        assertThat(exchange.getBody())
                .isNotNull();

        final String body = exchange.getBody();

        assertThat(body)
                .as("Сообщение ответа должно совпадать с ожидаемым")
                .isEqualTo("Test message");
    }

}

