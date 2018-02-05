"use strict";

/******************************************************************************************

Tasks main

******************************************************************************************/

require("./expenses-controller.js");

var app = angular.module("expenses.controllers", [
	"expenses.controller"
]);

app.config(["$routeProvider", function($routeProvider) {
	// Labour analysis routes
	$routeProvider.when("/expenses", { templateUrl: "codingtest/expenses-content.html"});
	$routeProvider.otherwise({redirectTo: "/expenses"}   );
}]);

app.run(['$http', '$rootScope', function($http, $rootScope) {

    // DS: The Basic Authorization header is added to the http request with a
    // fixed user and password (since it is beyond the scope of this challenge
    // to define a user/login system)
    $http.defaults.headers.common['Authorization'] = 'Basic ZGlhbmE6c2VjcmV0';

	// Add app button
	$rootScope.appSections = $rootScope.appSections || [];
	$rootScope.appSections.push({ title: "Expenses", image: "img/icon-generic.png", app: "expenses" });

	// Configure tab sections
	$rootScope.tabSections = $rootScope.tabSections || {};
	$rootScope.tabSections.expenses = [
		{ title: "Expenses", app: "expenses" }
	];
}]);