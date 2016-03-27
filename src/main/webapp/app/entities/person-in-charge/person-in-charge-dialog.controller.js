(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('PersonInChargeDialogController', PersonInChargeDialogController);

    PersonInChargeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PersonInCharge', 'Scholar'];

    function PersonInChargeDialogController ($scope, $stateParams, $uibModalInstance, entity, PersonInCharge, Scholar) {
        var vm = this;
        vm.personInCharge = entity;
        vm.scholars = Scholar.query();
        vm.load = function(id) {
            PersonInCharge.get({id : id}, function(result) {
                vm.personInCharge = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('madrassaApp:personInChargeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.personInCharge.id !== null) {
                PersonInCharge.update(vm.personInCharge, onSaveSuccess, onSaveError);
            } else {
                PersonInCharge.save(vm.personInCharge, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
