(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('InscriptionDetailController', InscriptionDetailController);

    InscriptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Inscription', 'ClassRoom', 'Scholar', 'User', 'Wish'];

    function InscriptionDetailController($scope, $rootScope, $stateParams, entity, Inscription, ClassRoom, Scholar, User, Wish) {
        var vm = this;
        vm.inscription = entity;
        vm.load = function (id) {
            Inscription.get({id: id}, function(result) {
                vm.inscription = result;
            });
        };
        var unsubscribe = $rootScope.$on('madrassaApp:inscriptionUpdate', function(event, result) {
            vm.inscription = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
