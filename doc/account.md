### Version
0.0.1

### 帳戶相關API

#### 建立帳號
POST localhost:8080/api/account
``` json
{
  "email": "sam001@gmail.com",
  "passwd": "123456",
  "nickname": "小朱"
}
```

Respond
HttpStatusCode 201 Created
``` json
{
  "userid": "1"
}
```

HttpStatusCode 500 Internal Server Error
``` json
{
  "msg": "帳號已存在"
}
```

#### 取得 未修改
GET localhost:8080/api/account/${_id}

Respond
``` json
{
  "header": {
    "prc": "0"
  },
  "body": {
    "account": {
      "email": "spike23_4@yahoo.com.tw",
      "passwd": "",
      "nickname": "小朱",
      "createDate": ""
    }
  }
}
```

#### 更新 未修改
PUT localhost:8080/api/account/${_id}
``` json
{
  "email": "spike23_4@yahoo.com.tw",
  "passwd": "A76423HEBCDD",
  "nickname": "小朱",
  "createDate": ""
}
```

Respond
```json
{
  "header": {
    "prc": "0"
  },
  "body": {
    "_id": "AUv6aGQQG6Ft_tZGWRKE"
  }
}
```

#### 刪除 未修改
DELETE localhost:8080/api/account/${_id}

Respond
``` json
{
  "header": {
    "prc": "0"
  },
  "body": {}
}
```

#### 查詢
GET localhost:8080/api/account?nickname=帥

Respond
``` json
{
  "header": {
    "prc": "0"
  },
  "body": {
    "total": 1,
    "max_score": 1.3116325,
    "hits": [
      {
        "_id": "AUv1xv2FG6Ft_tZGWRKC",
        "_score": 1.3116325,
        "account": {
          "email": "spike23_4@yahoo.com.tw",
          "passwd": "",
          "nickname": "帥哥",
          "createDate": "20150208T115722.333Z"
        }
      }
    ]
  }
}
```

### Todo's
 - 查詢功能的問題，資料如為帥哥，帥跟哥分別都可以找到，帥哥找不到

