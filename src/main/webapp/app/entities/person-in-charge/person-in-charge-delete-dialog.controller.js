(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('PersonInChargeDeleteController',PersonInChargeDeleteController);

    PersonInChargeDeleteController.$inject = ['$uibModalInstance', 'entity', 'PersonInCharge'];

    function PersonInChargeDeleteController($uibModalInstance, entity, PersonInCharge) {
        var vm = this;
        vm.personInCharge = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PersonInCharge.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
