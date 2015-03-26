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
``` json
{
  "header": {
    "prc": "0"
  },
  "body": {
    "access_token": "9c9466778e9b6e15064bfe491876bb38dbeb5d17ea3f366fd269706ae34d8b63f21b968c228f7b82ad9b8353d2df0e6f9565a6a26de0d87ef3835a70db15fdbb"
  }
}
```

Login Fail
``` json
{
  "header": {
    "prc": "9"
  },
  "body": {
    "message": "密碼錯誤"
  }
}
```