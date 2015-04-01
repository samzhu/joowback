### Version
0.0.1

### 文章API

#### 新增文章
POST localhost:8080/api/posts?access_token={access_token}
``` json
{
  "title": "第一篇",
  "content": "測試測試",
  "tags":["帥","別羨慕哥"]
}
```

Success Respond
HttpStatusCode 201 Created
``` json
{
  "postid": "AUxz2qyIaVI6PbHipWvB"
}
```

####查詢文章
GET localhost:8080/api/posts?access_token={access_token}

200 OK
``` json
{
  "total": 1,
  "max_score": 1,
  "hits": [
    {
      "_id": "AUxz6iEPaVI6PbHipWvC",
      "_score": 1,
      "post": {
        "title": "第一篇",
        "content": "測試測試",
        "tags": [
          "帥",
          "別羨慕哥"
        ],
        "ownerid": "1"
      }
    }
  ]
}
```