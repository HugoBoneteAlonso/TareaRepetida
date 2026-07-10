#   Tarea 1 repetida sin IA

##  Instrucciones de arranque

- *Descargar el proyecto*
- *Asegurarse de tener Maven instalado*
- *Asegurarse de tener las dependencias cargadas si no lo estan sincronizar el proyecto*
- *Ejecutar el comando en la terminal:*
```
    mvn spring-boot:run
```

##  Ejecutar los tests

- *Se utiliza el comando:*
```
    mvn clean test  
```

- *Para comprobar la cobertura hay que dirigirse a "target/site/jacoco/index.html" dentro del proyecto*

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
  "createdAt": "2026-06-22T11:35:25.490746"
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

##  Manejo de Errores

### GET Product By Id But Not Found, Update Product Not Found, Delete Product Not Found
```
    http://localhost:8080/api/v1/products/9999
```
***Response***
```json
{
  "timeStamp": "2026-06-26T11:09:45.5915113",
  "status": 404,
  "error": "Product Not Found",
  "message": "Producto con id 9999 no encontrado",
  "path": "/api/v1/products/9999",
  "traceId": "1aff0eb8-2d8a-4240-9ffc-736a2f703649"
}
```

### GET Product Wrong Path
```
    http://localhost:8080/api/v1/products/abc
```
***Response***
```json
{
  "timeStamp": "2026-06-26T11:11:03.6259156",
  "status": 400,
  "error": "Bad Request",
  "message": "Parametro del path con tipo incorrecto",
  "path": "/api/v1/products/abc",
  "traceId": "dc138362-058f-43d8-ae4c-06f8d3a6ee34"
}
```

### Create Product With Empty Name
```
    http://localhost:8080/api/v1/products
```

***Body***
```json
{
  "name": "",
  "description": "desc",
  "price": 100.0,
  "stock": 10
}
```

***Response***
```json
{
  "timeStamp": "2026-06-26T11:11:49.4875458",
  "status": 400,
  "error": "Bad Request",
  "message": "La peticion contiene campos invalidos",
  "path": "/api/v1/products",
  "traceId": "ff95ee16-0f94-47d3-a904-270a73fde10b",
  "fieldErrors": [
    {
      "field": "name",
      "rejectedValue": "",
      "message": "El nombre no puede ser nulo"
    }
  ]
}
```

### Create Product With Negative Price
```
    http://localhost:8080/api/v1/products
```

***Body***
```json
{
  "name": "Producto prueba",
  "description": "desc",
  "price": -100.0,
  "stock": 10
}
```

***Response***
```json
{
  "timeStamp": "2026-06-26T09:53:24.0211511",
  "status": 400,
  "error": "Bad Request",
  "message": "La peticion contiene campos invalidos",
  "path": "/api/v1/products",
  "traceId": "e867bc90-1299-48d1-9f27-ca9d57e7cc36",
  "fieldErrors": [
    {
      "field": "price",
      "rejectedValue": -100.0,
      "message": "El precio debe ser positivo"
    }
  ]
}
```

### Create Product With Invalid Body
```
    http://localhost:8080/api/v1/products
```

***Body***
```json
{
  "name": "Product 1",
  "price": 10.5,
  "stock": 5
```

***Response***
```json
{
  "timeStamp": "2026-06-26T11:16:56.0162108",
  "status": 400,
  "error": "Bad Request",
  "message": "Los campos del body estan mal formados",
  "path": "/api/v1/products",
  "traceId": "84b681ea-5e2f-4a19-9cc4-fba362bdcb9f"
}
```

### Create Product With Special Characters
```
    http://localhost:8080/api/v1/products
```

***Body***
```json
{
  "name": "Nombre! nuevo",
  "description": "desc",
  "price": 100.0,
  "stock": 10
}
```

***Response***
```json
{
  "timeStamp": "2026-06-26T11:18:16.0092251",
  "status": 400,
  "error": "Bad Request",
  "message": "La peticion contiene campos invalidos",
  "path": "/api/v1/products",
  "traceId": "4e2efc96-93ac-4503-8712-4818f420258c",
  "fieldErrors": [
    {
      "field": "name",
      "rejectedValue": "Nombre! nuevo",
      "message": "El nombre no puede contener caracteres especiales"
    }
  ]
}
```

### GET Customers By Id
```
    http://localhost:8080/api/v1/customers/1
```
***Response***
```json
{
  "id": 1,
  "name": "Laura Garcia",
  "email": "laura.garcia@example.com",
  "totalOrders": 6,
  "totalSpent": 7319.82
}
```

### GET Orders By Status
```
    http://localhost:8080/api/v1/orders/status?status=PENDING
```
***Response***
```json
[
  {
    "id": 1,
    "orderDate": "2026-06-30T13:33:56.085446",
    "status": "PENDING",
    "customer": {
      "id": 1,
      "name": "Laura Garcia",
      "email": "laura.garcia@example.com",
      "totalOrders": null,
      "totalSpent": null
    },
    "lines": [
      {
        "id": 1,
        "productId": 3,
        "productName": "iPhone 14",
        "quantity": 1,
        "unitPrice": 999.99,
        "lineTotal": 999.99
      },
      {
        "id": 2,
        "productId": 14,
        "productName": "Disco SSD Samsung 1TB",
        "quantity": 2,
        "unitPrice": 109.99,
        "lineTotal": 219.98
      }
    ],
    "totalAmount": 1219.97
  }
]
```

### GET Products Pageable Size and Page
```
    http://localhost:8080/api/v1/products?page=0&size=5
```
***Response***
```json
{
  "content": [
    {
      "id": 1,
      "name": "Producto actualizado",
      "description": "Descripcion 2",
      "price": 13.02,
      "stock": 22,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": {
        "id": 1,
        "name": "Televisores"
      }
    },
    {
      "id": 2,
      "name": "Laptop Lenovo ThinkPad",
      "description": "Laptop empresarial 16GB RAM",
      "price": 899.99,
      "stock": 10,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 3,
      "name": "iPhone 14",
      "description": "Apple smartphone 128GB",
      "price": 999.99,
      "stock": 20,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 4,
      "name": "Auriculares Sony WH-1000XM5",
      "description": "Auriculares inalámbricos con cancelación de ruido",
      "price": 299.99,
      "stock": 25,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 5,
      "name": "Monitor LG 27\"",
      "description": "Monitor IPS Full HD",
      "price": 179.99,
      "stock": 12,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 5,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 6,
  "totalElements": 30,
  "first": true,
  "numberOfElements": 5,
  "size": 5,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "empty": false
}
```

### GET Products Price Between
```
    http://localhost:8080/api/v1/products/search?minPrice=10&maxPrice=50
```
***Response***
```json
{
  "content": [
    {
      "id": 1,
      "name": "Producto actualizado",
      "description": "Descripcion 2",
      "price": 13.02,
      "stock": 22,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": {
        "id": 1,
        "name": "Televisores"
      }
    },
    {
      "id": 7,
      "name": "Ratón Logitech G502",
      "description": "Ratón gaming inalámbrico",
      "price": 49.99,
      "stock": 40,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 24,
      "name": "Memoria USB Kingston 128GB",
      "description": "Pendrive USB 3.2",
      "price": 24.99,
      "stock": 60,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 3,
  "first": true,
  "numberOfElements": 3,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "empty": false
}
```

### GET Products Name And Price Between
```
    http://localhost:8080/api/v1/products/search?name=tel&minPrice=10
```
***Response***
```json
{
  "content": [
    {
      "id": 26,
      "name": "Procesador Intel Core i7",
      "description": "CPU Intel de 14ª generación",
      "price": 389.99,
      "stock": 17,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 31,
      "name": "tel",
      "description": "desc",
      "price": 100.00,
      "stock": 10,
      "createdAt": "2026-07-02T13:17:30.599307",
      "category": {
        "id": 2,
        "name": "Ordenadores"
      }
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "first": true,
  "numberOfElements": 2,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "empty": false
}
```

### GET Products No Filter
```
    http://localhost:8080/api/v1/products/search
```
***Response***
```json
{
  "content": [
    {
      "id": 1,
      "name": "Producto actualizado",
      "description": "Descripcion 2",
      "price": 13.02,
      "stock": 22,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": {
        "id": 1,
        "name": "Televisores"
      }
    },
    {
      "id": 2,
      "name": "Laptop Lenovo ThinkPad",
      "description": "Laptop empresarial 16GB RAM",
      "price": 899.99,
      "stock": 10,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 3,
      "name": "iPhone 14",
      "description": "Apple smartphone 128GB",
      "price": 999.99,
      "stock": 20,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 4,
      "name": "Auriculares Sony WH-1000XM5",
      "description": "Auriculares inalámbricos con cancelación de ruido",
      "price": 299.99,
      "stock": 25,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 5,
      "name": "Monitor LG 27\"",
      "description": "Monitor IPS Full HD",
      "price": 179.99,
      "stock": 12,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 6,
      "name": "Teclado Mecánico RGB",
      "description": "Teclado gaming con switches rojos",
      "price": 79.99,
      "stock": 30,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 7,
      "name": "Ratón Logitech G502",
      "description": "Ratón gaming inalámbrico",
      "price": 49.99,
      "stock": 40,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 8,
      "name": "Tablet Samsung Galaxy Tab",
      "description": "Tablet Android de 10 pulgadas",
      "price": 249.99,
      "stock": 18,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 9,
      "name": "PlayStation 5",
      "description": "Consola de nueva generación",
      "price": 549.99,
      "stock": 5,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 10,
      "name": "Xbox Series X",
      "description": "Consola Microsoft de alto rendimiento",
      "price": 499.99,
      "stock": 7,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 11,
      "name": "Cámara Canon EOS",
      "description": "Cámara réflex digital",
      "price": 799.99,
      "stock": 6,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 12,
      "name": "Smartwatch Xiaomi Band",
      "description": "Reloj inteligente deportivo",
      "price": 69.99,
      "stock": 35,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 13,
      "name": "Altavoz JBL Flip",
      "description": "Altavoz portátil Bluetooth",
      "price": 89.99,
      "stock": 22,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 14,
      "name": "Disco SSD Samsung 1TB",
      "description": "Almacenamiento SSD NVMe",
      "price": 109.99,
      "stock": 50,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 15,
      "name": "Router TP-Link AX1800",
      "description": "Router WiFi de alta velocidad",
      "price": 59.99,
      "stock": 28,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 16,
      "name": "MacBook Air M3",
      "description": "Portátil Apple ultraligero",
      "price": 1299.99,
      "stock": 8,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 17,
      "name": "iPad Air",
      "description": "Tablet Apple de alto rendimiento",
      "price": 699.99,
      "stock": 14,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 18,
      "name": "Samsung Galaxy S24",
      "description": "Smartphone Android gama alta",
      "price": 1099.99,
      "stock": 16,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 19,
      "name": "Nintendo Switch OLED",
      "description": "Consola híbrida portátil",
      "price": 349.99,
      "stock": 11,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 21,
      "name": "Webcam Logitech C920",
      "description": "Webcam Full HD para videollamadas",
      "price": 79.99,
      "stock": 26,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 2,
  "totalElements": 30,
  "first": true,
  "numberOfElements": 20,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "empty": false
}
```

### GET Orders with Filter
```
    http://localhost:8080/api/v1/orders/search?status=CONFIRMED&customerName=ana
```
***Response***
```json
{
  "content": [
    {
      "id": 1,
      "orderDate": "2026-07-02T13:19:58.208604",
      "status": "CONFIRMED",
      "customer": {
        "id": 21,
        "name": "ana",
        "email": "ana.persae982@example.com",
        "totalOrders": null,
        "totalSpent": null
      },
      "lines": [
        {
          "id": 3,
          "productId": 3,
          "productName": "iPhone 14",
          "quantity": 1,
          "unitPrice": 999.99,
          "lineTotal": 999.99
        },
        {
          "id": 4,
          "productId": 12,
          "productName": "Smartwatch Xiaomi Band",
          "quantity": 5,
          "unitPrice": 69.99,
          "lineTotal": 349.95
        }
      ],
      "totalAmount": 1349.94
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "first": true,
  "numberOfElements": 1,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "empty": false
}
```

### GET Orders with Date Filter
```
    http://localhost:8080/api/v1/orders/search?from=2024-01-01T00:00:00
```
***Response***
```json
{
  "content": [
    {
      "id": 1,
      "orderDate": "2026-07-02T13:19:58.208604",
      "status": "CONFIRMED",
      "customer": {
        "id": 21,
        "name": "ana",
        "email": "ana.persae982@example.com",
        "totalOrders": null,
        "totalSpent": null
      },
      "lines": [
        {
          "id": 3,
          "productId": 3,
          "productName": "iPhone 14",
          "quantity": 1,
          "unitPrice": 999.99,
          "lineTotal": 999.99
        },
        {
          "id": 4,
          "productId": 12,
          "productName": "Smartwatch Xiaomi Band",
          "quantity": 5,
          "unitPrice": 69.99,
          "lineTotal": 349.95
        }
      ],
      "totalAmount": 1349.94
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "first": true,
  "numberOfElements": 1,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "empty": false
}
```

### GET Products By Category
```
    hhttp://localhost:8080/api/v1/products/search?category=1
```
***Response***
```json
{
  "content": [
    {
      "id": 1,
      "name": "Producto actualizado",
      "description": "Descripcion 2",
      "price": 13.02,
      "stock": 22,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": {
        "id": 1,
        "name": "Televisores"
      }
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "first": true,
  "numberOfElements": 1,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "empty": false
}
```

### GET Products By Multiple Sorting
```
    http://localhost:8080/api/v1/products/search?sort=price,desc&sort=name,asc
```
***Response***
```json
{
  "content": [
    {
      "id": 16,
      "name": "MacBook Air M3",
      "description": "Portátil Apple ultraligero",
      "price": 1299.99,
      "stock": 8,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 18,
      "name": "Samsung Galaxy S24",
      "description": "Smartphone Android gama alta",
      "price": 1099.99,
      "stock": 16,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 3,
      "name": "iPhone 14",
      "description": "Apple smartphone 128GB",
      "price": 999.99,
      "stock": 20,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 2,
      "name": "Laptop Lenovo ThinkPad",
      "description": "Laptop empresarial 16GB RAM",
      "price": 899.99,
      "stock": 10,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 11,
      "name": "Cámara Canon EOS",
      "description": "Cámara réflex digital",
      "price": 799.99,
      "stock": 6,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 25,
      "name": "Tarjeta Gráfica RTX 4070",
      "description": "GPU NVIDIA para gaming y edición",
      "price": 699.99,
      "stock": 9,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 17,
      "name": "iPad Air",
      "description": "Tablet Apple de alto rendimiento",
      "price": 699.99,
      "stock": 14,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 9,
      "name": "PlayStation 5",
      "description": "Consola de nueva generación",
      "price": 549.99,
      "stock": 5,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 10,
      "name": "Xbox Series X",
      "description": "Consola Microsoft de alto rendimiento",
      "price": 499.99,
      "stock": 7,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 26,
      "name": "Procesador Intel Core i7",
      "description": "CPU Intel de 14ª generación",
      "price": 389.99,
      "stock": 17,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 30,
      "name": "Escritorio Elevable",
      "description": "Mesa regulable en altura",
      "price": 349.99,
      "stock": 6,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 19,
      "name": "Nintendo Switch OLED",
      "description": "Consola híbrida portátil",
      "price": 349.99,
      "stock": 11,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 4,
      "name": "Auriculares Sony WH-1000XM5",
      "description": "Auriculares inalámbricos con cancelación de ruido",
      "price": 299.99,
      "stock": 25,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 29,
      "name": "Silla Gaming DXRacer",
      "description": "Silla ergonómica ajustable",
      "price": 299.99,
      "stock": 7,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 8,
      "name": "Tablet Samsung Galaxy Tab",
      "description": "Tablet Android de 10 pulgadas",
      "price": 249.99,
      "stock": 18,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 27,
      "name": "Placa Base ASUS Prime",
      "description": "Compatible con Intel DDR5",
      "price": 199.99,
      "stock": 15,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 5,
      "name": "Monitor LG 27\"",
      "description": "Monitor IPS Full HD",
      "price": 179.99,
      "stock": 12,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 22,
      "name": "Micrófono HyperX QuadCast",
      "description": "Micrófono USB para streaming",
      "price": 139.99,
      "stock": 19,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 28,
      "name": "Fuente Corsair 750W",
      "description": "Fuente certificada 80 Plus Gold",
      "price": 119.99,
      "stock": 20,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    },
    {
      "id": 14,
      "name": "Disco SSD Samsung 1TB",
      "description": "Almacenamiento SSD NVMe",
      "price": 109.99,
      "stock": 50,
      "createdAt": "2026-07-02T13:15:59.131754",
      "category": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 2,
  "totalElements": 30,
  "first": true,
  "numberOfElements": 20,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "empty": false
}
```

##  Enlaces usados para la Tarea
https://stackoverflow.com/questions/60279760/how-to-generate-a-createddate-localdatetime-as-timestamp  
https://stackoverflow.com/questions/49954812/how-can-you-make-a-created-at-column-generate-the-creation-date-time-automatical  
https://www.galisteocantero.com/configurar-base-de-datos-en-memoria-h2-con-spring-boot/  
https://www.geeksforgeeks.org/advance-java/hibernate-generatedvalue-annotation-in-jpa/  
https://medium.com/@ruwanpradeep9911/implementing-swagger-with-spring-boot-a-step-by-step-guide-4b121e607bd1  
https://bill-tetrault.github.io/howtomarkdown/  
https://www.geeksforgeeks.org/advance-java/spring-mvc-controlleradvice-annotation-for-global-exception-handling/  
https://zetcode.com/springboot/controlleradvice/  
https://medium.com/javajams/master-spring-boot-customized-exceptions-a-practical-guide-c5bc3e1a1efb  
https://www.baeldung.com/javax-validation-method-constraints  
https://www.javaguides.net/2025/03/spring-boot-webmvctest-annotation.html  
https://es.stackoverflow.com/questions/607140/c%c3%b3mo-saber-si-un-string-contiene-alg%c3%ban-caracter-especial-como-etc-e  