# Contact API Specification

## Create Contact
Endpoint : POST /api/contacts

Request Header :

* X-API-TOKEN : Token (mandatory)

Request Body:

```json
{
    "firstname" : "Bintang",
    "lastname" : "Ginanjar",
    "email" : "bintang.ginanjar@gmail.com",
    "phone" : "+6281300001111"
}
```

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "Contact has been created",
    "data" : {
        "id" : "1",
        "firstname" : "Bintang",
        "lastname" : "Ginanjar",
        "email" : "bintang.ginanjar@gmail.com",
        "phone" : "+6281300001111"
    }
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Invalid username, email or email format"
}
```

## Update Contact

Endpoint : PUT /api/contacts/{contactId}

Request Header :

* X-API-TOKEN : Token (mandatory)

Request Body:

```json
{
    "firstname" : "Bintang",
    "lastname" : "Ginanjar",
    "email" : "bintang.ginanjar@gmail.com",
    "phone" : "+6281300001111"
}
```

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "User update success",
    "data" : {
        "id" : "1",
        "firstname" : "Bintang",
        "lastname" : "Ginanjar",
        "email" : "bintang.ginanjar@gmail.com",
        "phone" : "+6281300001111"
    }
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Invalid username, email or email format"
}
```

## Get Contact
Endpoint : GET /api/contacts/{contactId}

Request Header :

* X-API-TOKEN : Token (mandatory)

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "User fetch success",
    "data" : {
        "id" : "1",
        "firstname" : "Bintang",
        "lastname" : "Ginanjar",
        "email" : "bintang.ginanjar@gmail.com",
        "phone" : "+6281300001111"
    }
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Contact not found"
}
```

## Remove Contact

Endpoint : DELETE /api/contacts/{contactId}

Request Header :

* X-API-TOKEN : Token (mandatory)

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "Contact has been deleted",    
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Contact not found"
}
```

## Search Contact

Endpoint : GET /api/contacts

- name : String, contact first name or last name, using query (optional)
- phone : String, contact phone, using query (optional)
- phone : String, email, using query (optional)
- page : Integer, start from 0
- size : Integer, default 10

Request Header :

* X-API-TOKEN : Token (mandatory)

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "data" : [
        {
            "id" : "1",
            "firstname" : "Bintang",
            "lastname" : "Ginanjar",
            "email" : "bintang.ginanjar@gmail.com",
            "phone" : "+6281300001111"
        }
    ],
    "paging" : {
        "currentPage" : 0,
        "totalPage" : 10,
        "sizePage" : 10
    }
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Unauthorized access"
}
```