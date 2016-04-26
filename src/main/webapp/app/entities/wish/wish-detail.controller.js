(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('WishDetailController', WishDetailController);

    WishDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Wish', 'Inscription'];

    function WishDetailController($scope, $rootScope, $stateParams, entity, Wish, Inscription) {
        var vm = this;
        vm.wish = entity;
        
        var unsubscribe = $rootScope.$on('madrassaApp:wishUpdate', function(event, result) {
            vm.wish = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
