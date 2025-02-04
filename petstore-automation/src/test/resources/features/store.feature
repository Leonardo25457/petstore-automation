Feature: Validación de la integridad del API de Store en PetStore
  Como automatizador principal de NTT
  Quiero validar que la API de Store funcione correctamente tanto en la creación como en la consulta de pedidos
  Para garantizar la integridad de los componentes, incluida la conexión a la base de datos

  Background:
    Given la API de Store está disponible en "https://petstore.swagger.io/v2"
    And la conexión a la base de datos se encuentra activa

  @OrdenCreacion
  Scenario Outline: Creación de Order - POST /store/order
    Given que se dispone de los siguientes datos para el nuevo pedido:
      | orderId  | petId  | quantity | shipDate              | status  | complete |
      | <orderId> | <petId> | <quantity> | <shipDate>           | <status> | <complete> |
    When se envía la solicitud POST a "/store/order" con el payload correspondiente
    Then el código de respuesta debe ser <statusCode>
    And el body de la respuesta debe contener:
      | orderId  | petId  | quantity | status  | complete |
      | <orderId> | <petId> | <quantity> | <status> | <complete> |

    Examples:
      | orderId | petId | quantity | shipDate              | status  | complete | statusCode |
      | 1001    | 2001  | 2        | 2025-02-03T10:00:00Z  | placed  | true     | 200        |

  @OrdenConsulta
  Scenario Outline: Consulta de Order - GET /store/order/{orderId}
    Given se consulta un pedido existente con id "<orderId>"
    When se envía la solicitud GET a "/store/order/<orderId>"
    Then el código de respuesta debe ser <statusCode>
    And el body de la respuesta debe contener el id "<orderId>" y el estado "<status>"

    Examples:
      | orderId | status | statusCode |
      | 1001    | placed | 200        |
