(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('InscriptionDeleteController',InscriptionDeleteController);

    InscriptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Inscription'];

    function InscriptionDeleteController($uibModalInstance, entity, Inscription) {
        var vm = this;
        vm.inscription = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Inscription.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
