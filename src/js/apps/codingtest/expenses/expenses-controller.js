"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "config", "restalchemy", '$timeout',
    function ExpensesCtrl($rootScope, $scope, $config, $restalchemy, $timeout) {

	// Update the headings
	$rootScope.mainTitle = "Expenses";
	$rootScope.mainHeading = "Expenses";

	// Update the tab sections
	$rootScope.selectTabSection("expenses", 0);

	// DS: Error handling
    $scope.showMsg = false;
    $scope.showErr = false;
    $scope.doFade = false;
    $scope.contentMessage = " ";

    // DS: the function displayMsg is added to show and fade a new message in the form.
    $scope.displayMsg = function(){
        $scope.doFade = false;
        $timeout(function(){
          $scope.doFade = true;
          $scope.showErr = false;
          $scope.showMsg = false;
        }, 2500);
      };

    // DS: REST endpoints
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
		    if (result.isOk){
		        // DS: If the rest endpoint isOK, then no error is displayed
			    $scope.expenses = result.data; // DS: the variable "result" obtained from the rest endpoint
			                                   // has a "data" object where the data actually is.
		    }
		    else{
		        // DS: There was an error with the rest request GETTING the expense list for the user
		        $scope.contentMessage = "ERROR: " + result.error;
		        $scope.displayMsg();
			    }
		});
	}

	$scope.saveExpense = function() {
		if ($scope.expensesform.$valid) {
			// Post the expense via REST
			restExpenses.post($scope.newExpense).then(function(result) {
			    // DS: If the rest endpoint isOK, then a new message is displayed showing the user the result of the transaction
			    if (result.isOk){
            	    $scope.contentMessage = "The Expense was successfully created.";
            	    $scope.showMsg = true;
            	    $scope.showErr = false;
            		$scope.displayMsg();
            	}
            	else{
            	    // DS: There was an error with the rest request CREATING a new expense
            		$scope.contentMessage = result.error.errorCode + " -> " + result.error.errorDescription;
            	    $scope.showMsg = false;
            	    $scope.showErr = true;
            		$scope.displayMsg();
            	}
				// Reload new expenses list
				loadExpenses();
			});
		}
	};

	$scope.clearExpense = function() {
		$scope.newExpense = {};
		$scope.contentMessage = " "; // DS : the error message is clear
		$scope.showMsg = false;
		$scope.showErr = false;

	};

	$scope.getVATPge = function() {
		// DS : Get the VAT percentage via rest endpoint
		restVatPge.get().then(function(result) {
			$scope.pgeVAT = result;
		});
	};

	$scope.calcVAT = function() {
		 $scope.expenseVat = $scope.newExpense.amount * $scope.pgeVAT/100;
	};

    // Initialise scope variables
    $scope.init = function(){
        // DS: This function is used to calculate the VAT percentage
        // which is obtained via rest from the backend
        $scope.getVATPge();
    }
    $scope.init();
	loadExpenses();
	$scope.clearExpense();
}]);