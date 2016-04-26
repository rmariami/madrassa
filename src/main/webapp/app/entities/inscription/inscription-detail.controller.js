(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('InscriptionDetailController', InscriptionDetailController);

    InscriptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Inscription', 'ClassRoom', 'Scholar', 'User', 'Wish'];

    function InscriptionDetailController($scope, $rootScope, $stateParams, entity, Inscription, ClassRoom, Scholar, User, Wish) {
        var vm = this;
        vm.inscription = entity;
        
        var unsubscribe = $rootScope.$on('madrassaApp:inscriptionUpdate', function(event, result) {
            vm.inscription = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
