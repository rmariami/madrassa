(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('InscriptionDialogController', InscriptionDialogController);

    InscriptionDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Inscription', 'ClassRoom', 'Scholar', 'User', 'Wish'];

    function InscriptionDialogController ($scope, $stateParams, $uibModalInstance, entity, Inscription, ClassRoom, Scholar, User, Wish) {
        var vm = this;
        vm.inscription = entity;
        vm.classrooms = ClassRoom.query();
        vm.scholars = Scholar.query();
        vm.users = User.query();
        vm.wishs = Wish.query();
        vm.load = function(id) {
            Inscription.get({id : id}, function(result) {
                vm.inscription = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('madrassaApp:inscriptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.inscription.id !== null) {
                Inscription.update(vm.inscription, onSaveSuccess, onSaveError);
            } else {
                Inscription.save(vm.inscription, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
