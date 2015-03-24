var app = angular.module("app", []);
    
function user_registry($scope, $http) {
    $scope.adduser = function(user) {
		user.createDate = new Date().toString();    
        $http({method: 'POST', url: 'http://localhost:8080/api/account', data: user, headers: {'Content-Type': 'application/json'}}).success(function(gdata){
            localStorage.id = gdata['body']['_id'];
            localStorage.name = user.nickname;
            localStorage.email = user.email;
			alert("welcome"+localStorage.name);
        }).error(function (gdata) {
            alert(gdata);
        });
    };
};

function user_login($scope, $http) {
    $scope.adduser = function(user) {
		user.createDate = new Date().toString();    
        $http({method: 'POST', url: 'http://localhost:8080/api/login', data: user, headers: {'Content-Type': 'application/json'}}).success(function(gdata){
            localStorage.id = gdata['body']['_id'];
            localStorage.name = user.nickname;
            localStorage.email = user.email;
			alert("welcome"+localStorage.name);
        }).error(function (gdata) {
            alert(gdata);
        });
    };
};
