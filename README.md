#   Tarea 1 repetida sin IA

##  Instrucciones de arranque

- *Descargar el proyecto*
- *Asegurarse de tener Maven instalado*
- *Asegurarse de tener las dependencias cargadas si no lo estan sincronizar el proyecto*
- *Ejecutar el comando en la terminal:*
```
    mvn spring-boot:run
```

##  Ejemplos de llamadas a la API desde Postman

### GET All Products
```
    http://localhost:8080/api/v1/products
```
***Response***
```json
[
  {
    "id": 1,
    "name": "TV Samsung 55\"",
    "description": "Smart TV 4K UHD",
    "price": 499.99,
    "stock": 15,
    "createdAt": "2026-06-19T13:49:08.268044"
  },
  {
    "id": 2,
    "name": "Laptop Lenovo ThinkPad",
    "description": "Laptop empresarial 16GB RAM",
    "price": 899.99,
    "stock": 10,
    "createdAt": "2026-06-19T13:49:08.272043"
  },
  {
    "id": 3,
    "name": "iPhone 14",
    "description": "Apple smartphone 128GB",
    "price": 999.99,
    "stock": 20,
    "createdAt": "2026-06-19T13:49:08.272043"
  }
]
```

### GET All Products By Name
```
    http://localhost:8080/api/v1/products?name=Altavoz JBL
```
***Response***
```json
[
  {
    "id": 13,
    "name": "Altavoz JBL",
    "description": "Altavoz portátil Bluetooth",
    "price": 89.99,
    "stock": 22,
    "createdAt": "2026-06-19T13:49:08.282046"
  }
]
```

### GET Product By Id
```
    http://localhost:8080/api/v1/products/4
```
***Response***
```json
{
  "id": 4,
  "name": "Auriculares Sony",
  "description": "Noise Cancelling inalámbricos",
  "price": 199.99,
  "stock": 25,
  "createdAt": "2026-06-19T13:49:08.273044"
}
```

### POST Create Product
```
    http://localhost:8080/api/v1/products
```

***Body***
```json
{
  "name": "Producto prueba",
  "description": "desc",
  "price": 100,
  "stock": 10
}
```

***Response***
```json
{
  "id": 16,
  "name": "Producto prueba",
  "description": "desc",
  "price": 100.0,
  "stock": 10,
  "createdAt": null
}
```

### PUT Update Product
```
    http://localhost:8080/api/v1/products/1
```

***Body***
```json
{
  "name": "Producto actualizado",
  "description": "Descripcion 2",
  "price": 13.02,
  "stock": 22
}
```

***Response***
```json
{
  "id": 1,
  "name": "Producto actualizado",
  "description": "Descripcion 2",
  "price": 13.02,
  "stock": 22,
  "createdAt": "2026-06-19T14:52:59.511474"
}
```

### DELETE Delete Product
```
    http://localhost:8080/api/v1/products/2
```

***Response***
```json
{
}
```

### GET Product By Id But Not Found
```
    http://localhost:8080/api/v1/products/20
```
***Response***
```json
{
  "timestamp": "2026-06-19T14:55:40.3974441",
  "status": 404,
  "error": "PRODUCT_NOT_FOUND",
  "message": "Producto con el id 20 no encontrado"
}
```

##  Enlaces usados para la Tarea
[Link]https://stackoverflow.com/questions/60279760/how-to-generate-a-createddate-localdatetime-as-timestamp
[Link]https://stackoverflow.com/questions/49954812/how-can-you-make-a-created-at-column-generate-the-creation-date-time-automatical
[Link]https://www.galisteocantero.com/configurar-base-de-datos-en-memoria-h2-con-spring-boot/
[Link]https://www.geeksforgeeks.org/advance-java/hibernate-generatedvalue-annotation-in-jpa/
[Link]https://medium.com/@ruwanpradeep9911/implementing-swagger-with-spring-boot-a-step-by-step-guide-4b121e607bd1
[Link]https://bill-tetrault.github.io/howtomarkdown/
[Link]https://www.geeksforgeeks.org/advance-java/spring-mvc-controlleradvice-annotation-for-global-exception-handling/
[Link]https://zetcode.com/springboot/controlleradvice/
[Link]https://medium.com/javajams/master-spring-boot-customized-exceptions-a-practical-guide-c5bc3e1a1efb