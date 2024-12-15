# Address API Specification

## Create Address
Endpoint : POST /api/contacts/{contactId}/addresses

Request Header :

* X-API-TOKEN : Token (mandatory)

Request Body:

```json
{
    "street" : "Street name",
    "city" : "City name",
    "province" : "Province name",
    "country" : "Country name",
    "postalCode" : "Postal code"
}
```

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "Address has been created",
    "data" : {
        "id" : "1",
        "street" : "Street name",
        "city" : "City name",
        "province" : "Province name",
        "country" : "Country name",
        "postalCode" : "Postal code"
    }
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Address creation failed"
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Unauthorized access"
}
```

## Update Address
Endpoint : PUT /api/contacts/{contactId}/addresses/{addressId}

Request Header :

* X-API-TOKEN : Token (mandatory)

Request Body:

```json
{
    "street" : "Street name",
    "city" : "City name",
    "province" : "Province name",
    "country" : "Country name",
    "postalCode" : "Postal code"
}
```

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "Address update success",
    "data" : {
        "id" : "1",
        "street" : "Street name",
        "city" : "City name",
        "province" : "Province name",
        "country" : "Country name",
        "postalCode" : "Postal code"
    }
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Address not found"
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Unauthorized access"
}
```

## Get Address
Endpoint : GET /api/contacts/{contactId}/addresses/{addressId}

Request Header :

* X-API-TOKEN : Token (mandatory)

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "Address fetch success",
    "data" : {
        "id" : "1",
        "street" : "Street name",
        "city" : "City name",
        "province" : "Province name",
        "country" : "Country name",
        "postalCode" : "Postal code"
    }
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Address not found"
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Unauthorized access"
}
```

## Remove Address
Endpoint : DELETE /api/contacts/{contactId}/addresses/{addressId}

Request Header :

* X-API-TOKEN : Token (mandatory)

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "Address has been removed",    
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Address not found"
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Unauthorized access"
}
```

## List Address
Endpoint : GET /api/contacts/{contactId}/addresses

Request Header :

* X-API-TOKEN : Token (mandatory)

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "Success",
    "data" : [
        {
            "id" : "1",
            "street" : "Street name",
            "city" : "City name",
            "province" : "Province name",
            "country" : "Country name",
            "postalCode" : "Postal code"
        }
    ]
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Address not found"
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Unauthorized access"
}
```