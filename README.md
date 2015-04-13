
更新時間2015/04/01
以下四支API均有異動,以HTTP Status Code為主要狀態,
就不再回覆prc
帳號
登入
po文
搜尋文章


API規則如下
> RESTful URI

Http Method | URI | Description
----------- | --------- | -----------
GET | /accounts | 列出所有帳號
GET | /accounts/{account_id} | 取得指定帳號資料
POST | /accounts | 新增帳號
PUT | /accounts/{account_id} | 修改指定帳號資料
DELETE | /accounts/{account_id} | 刪除指定帳號資料
GET | /accounts/{account_id}/roles | 列出帳號的所有角色
GET | /accounts/{account_id}/roles/{role_id} | 取得帳號的指定角色資料
DELETE | /accounts/{account_id}/roles/{role_id} | 刪除帳號的指定角色資料


> 使用HTTP Status Code處理錯誤

HTTP Method | Status Code | 	Description
----------- | --------- | -----------
GET | 200 (OK) | 順利取得資源
GET | 400 (Bad Request) | 無法順利取得資源，因為參數有問題或查尋條件失效
GET | 404 (Not Found) | 資源不存在
POST | 201 (Created) | 成功新增資源
POST | 404 (Not Found) | 資源不存在
PUT | 200 (OK) | 成功修改，並回傳異動後的資源
PUT | 201 (Created) | 成功修改資源
PUT | 204 (No Content) | 成功修改，但不回傳異動後的資源
PUT | 404 (Not Found) | 資源不存在
DELETE | 200 (OK) | 成功刪除，並回傳異動後的資源
DELETE | 204 (No Content) | 成功刪除，但不回傳異動後的資源
DELETE | 404 (Not Found) | 資源不存在

假設我們要取得第二頁的十筆帳號。
其URI顯示如：/accounts?pageNo=2&pageSize=10

規則如/api/[version]/[resource name]
[version]：使用”v”開頭，加上正整數

再參考一下路由寫法
http://stackoverflow.com/questions/17519297/mapping-spray-parameters

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

* [Account API](https://bitbucket.org/joow/joowback/src/25790612db0885f14e6e99f6f3ecd26937124044/doc/account.md?at=master)
* [Auth API](https://bitbucket.org/joow/joowback/src/0d3d3fbff58cf654bb13d186731f3f76bc898767/doc/auth.md?at=master)