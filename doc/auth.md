### Version
0.0.1

### 登入授權相關API

#### Login (Add Auth)
POST localhost:8080/api/auth
``` json
{
  "email": "ddd@gmail.com",
  "passwd": "123456"
}
```

Login Success Respond
HttpStatusCode 201 Created
``` json
{
  "access_token": "d5919c108cc8e4bfb58899428ce0c210db3a0cb4177ce59abd1a04fba524eee03af7cc471d1e618d2b02ae7a36a603d8b9511128225ca1f0f64768e26dbe842e"
}
```

Login Fail
HttpStatusCode 500 Internal Server Error
``` json
{
  "msg": "密碼錯誤"
}
```