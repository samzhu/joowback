
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

* [Account API](https://bitbucket.org/joow/joowback/src/25790612db0885f14e6e99f6f3ecd26937124044/doc/account.md?at=master)
* [Auth API](https://bitbucket.org/joow/joowback/src/0d3d3fbff58cf654bb13d186731f3f76bc898767/doc/auth.md?at=master)