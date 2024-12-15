# User API Specification

## Register User
Endpoint : POST /api/users

Request Body:

```json
{
    "username" : "bintang.ginanjar",
    "password" : "password",
    "name" : "Bintang Ginanjar"
}
```

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "Registration success",
    "data" : {
        "username" : "bintang.ginanjar",    
        "name" : "Bintang Ginanjar"
    }
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Username already registered"
}
```

## Login User

Endpoint : POST /api/auth/login

Request Body:

```json
{
    "username" : "bintang.ginanjar",
    "password" : "123456"
}
```

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "token" : "TOKEN",
    "expiredAt" : "30000000", //miliseconds
    "message" : "Login success"
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Wrong username of password"
}
```

## Get User

Endpoint : GET /api/users/current

Request Header :

* X-API-TOKEN : Token (mandatory)

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "User fetching success",
    "data" :
        {
            "username" : "bintang.ginanjar",
            "name" : "Bintang Ginanjar"
        }    
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Session expired, please re-login"
}
```

## Update User

Endpoint : PATCH /api/users/current

Request Header :

* X-API-TOKEN : Token (mandatory)

Request Body:

```json
{
    "name" : "Bintang Ginanjar", // put if only want to update name
    "password" : "56789" // put if only want to update password
}
```

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "User has been updated",
    "data" : {
        "username" : "bintang.ginanjar",        
    }
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "Failed to update username or password"
}
```

## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :

* X-API-TOKEN : Token (mandatory)

Response Body (Success):

```json
{
    "httpResponse" : "200",
    "message" : "You've been logout successfully"
}
```

Response Body (Failed):

```json
{
    "httpResponse" : "400",
    "message" : "You're unable to logout, please delete your session manually"
}
```