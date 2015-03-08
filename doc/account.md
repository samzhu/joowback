### Version
0.0.1

### Tech
相關參考技術網站

* [淺談 REST 軟體架構風格] - 如何設計 RESTful Web Service
* [elasticsearch] - 官方網站
* [elastic4s] - scala用的elasticsearch客戶端

[淺談 REST 軟體架構風格]:http://blog.toright.com/posts/1399/%E6%B7%BA%E8%AB%87-rest-%E8%BB%9F%E9%AB%94%E6%9E%B6%E6%A7%8B%E9%A2%A8%E6%A0%BC-part-ii-%E5%A6%82%E4%BD%95%E8%A8%AD%E8%A8%88-restful-web-service.html
[elasticsearch]:http://www.elasticsearch.org/
[elastic4s]:https://github.com/sksamuel/elastic4s

### API共通規則
必須在HTTP Header中加上
Content-Type=application/json

### 帳戶相關API

#### 新增
POST localhost:8080/account
``` json
{
  "email": "spike23_4@yahoo.com.tw",
  "passwd": "A76423HEBCDD",
  "nickname": "小朱",
  "createDate": "20150208T115722.333Z"
}
```

Respond
``` json
{
  "header": {
    "prc": "0"
  },
  "body": {
    "_id": "AUv6aGQQG6Ft_tZGWRKE"
  }
}
```

#### 取得
GET localhost:8080/account/${_id}

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

#### 更新
PUT localhost:8080/account/${_id}
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

#### 刪除
DELETE localhost:8080/account/${_id}

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
GET localhost:8080/account?nickname=帥

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

