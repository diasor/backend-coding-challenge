"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "config", "restalchemy", function ExpensesCtrl($rootScope, $scope, $config, $restalchemy) {
	// Update the headings
	$rootScope.mainTitle = "Expenses";
	$rootScope.mainHeading = "Expenses";

	// Update the tab sections
	$rootScope.selectTabSection("expenses", 0);

    var restVatPge = $restalchemy.init({ root: $config.apiroot }).at("vat");
	var restExpenses = $restalchemy.init({ root: $config.apiroot }).at("expenses");

	$scope.dateOptions = {
		changeMonth: true,
		changeYear: true,
		dateFormat: "dd/mm/yy"
	};

	var loadExpenses = function() {
		// Retrieve a list of expenses via REST
		restExpenses.get().then(function(result) {
			$scope.expenses = result.data; // DS: the variable "result" obtained from thee microservices, has a data object where the data actually is
		});
	}

	$scope.saveExpense = function() {
		if ($scope.expensesform.$valid) {
			// Post the expense via REST
			restExpenses.post($scope.newExpense).then(function() {
				// Reload new expenses list
				loadExpenses();
			});
		}
	};

	$scope.clearExpense = function() {
		$scope.newExpense = {};
	};

	$scope.getVATPge = function() {
		// DS : Get the VAT percentage via REST
		restVatPge.get().then(function(result) {
			$scope.pgeVAT = result; // DS: the variable "result" obtained from thee microservices, has a data object where the data actually is
		});
	};

	$scope.calcVAT = function() {
		 $scope.expenseVat = $scope.newExpense.amount * $scope.pgeVAT/100;
	};

	$scope.smallInput = {"width" : "80px"};
    $scope.vatStyle = {"width" : "60px"};
    $scope.vatPgeStyle = {"width" : "80px"};

    // Initialise scope variables
    $scope.init = function(){
        // DS: This function is used to calculate the VAT percentaje
        // Obtained via rest from the backend
        $scope.getVATPge();
    }
    $scope.init();
	loadExpenses();
	$scope.clearExpense();
}]);
