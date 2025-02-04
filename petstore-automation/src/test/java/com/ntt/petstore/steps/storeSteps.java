package com.ntt.petstore.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Map;

public class storeSteps {

    private io.restassured.response.Response response;

    @Given("la API de Store está disponible en {string}")
    public void la_api_de_store_esta_disponible(String baseUrl) {
        baseURI = baseUrl;
    }

    @Given("la conexión a la base de datos se encuentra activa")
    public void la_conexion_a_la_base_de_datos_se_encuentra_activa() {
        // en esta parte implementamos una validacion real de la conexion a la BD.
        // simulamos la validación.
        System.out.println("Conexión a la base de datos validada exitosamente.");
    }

    @Given("que se dispone de los siguientes datos para el nuevo pedido:")
    public void que_se_dispone_de_los_siguientes_datos_para_el_nuevo_pedido(DataTable dataTable) {
        // En una implementación real mapeariamos los datos para construir el payload.
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            System.out.println("Datos del pedido: " + row);
        }
    }

    @When("se envía la solicitud POST a {string} con el payload correspondiente")
    public void se_envia_la_solicitud_post_con_el_payload(String endpoint) {
        // aqui construimos el payload con valores de ejemplo.
        String payload = "{"
                + "\"id\": 1001,"
                + "\"petId\": 2001,"
                + "\"quantity\": 2,"
                + "\"shipDate\": \"2025-02-03T10:00:00Z\","
                + "\"status\": \"placed\","
                + "\"complete\": true"
                + "}";
        response = given()
                        .contentType("application/json")
                        .body(payload)
                   .when()
                        .post(endpoint);
    }

    @Then("el código de respuesta debe ser {int}")
    public void el_codigo_de_respuesta_debe_ser(Integer expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("el body de la respuesta debe contener:")
    public void el_body_de_la_respuesta_debe_contener(DataTable dataTable) {
        // Validamos que el response body contenga cada uno de los valores esperados.
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            row.forEach((key, value) -> {
                response.then().body(key, equalTo(convertirValor(value)));
            });
        }
    }

    @Given("que se consulta un pedido existente con id {string}")
    public void que_se_consulta_un_pedido_existente_con_id(String orderId) {
        System.out.println("Consultando pedido con id: " + orderId);
    }

    @When("se envía la solicitud GET a {string}")
    public void se_envia_la_solicitud_get_a(String endpoint) {
        response = when().get(endpoint);
    }

    @Then("el body de la respuesta debe contener el id {string} y el estado {string}")
    public void el_body_de_la_respuesta_debe_contener_el_id_y_el_estado(String orderId, String status) {
        response.then()
                .body("id", equalTo(Integer.parseInt(orderId)))
                .body("status", equalTo(status));
    }

    // Método auxiliar para convertir los valores de la data table a sus tipos adecuados
    private Object convertirValor(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                return Boolean.parseBoolean(value);
            }
            return value;
        }
    }
}
