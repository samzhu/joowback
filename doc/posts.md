### Version
0.0.1

### 文章API

#### 新增文章
POST localhost:8080/api/posts?access_token={access_token}
``` json
{
  "title": "第一篇",
  "content": "測試測試",
  "tags": [
    "帥",
    "別羨慕哥"
  ],
  "location": {
    "lat": 50.05,
    "lon": 55.06
  }
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
      "_id": "AU0QEdOPoLeM81SbSwcX",
      "_score": 1,
      "post": {
        "title": "第一篇",
        "content": "測試測試",
        "tags": [
          "帥",
          "別羨慕哥"
        ],
        "location": {
          "lat": 50.05,
          "lon": 55.06
        },
        "ownerid": "1"
      }
    }
  ]
}
```