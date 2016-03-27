(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('InscriptionDialogController', InscriptionDialogController);

    InscriptionDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Inscription', 'ClassRoom', 'Scholar', 'User', 'Wish'];

    function InscriptionDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Inscription, ClassRoom, Scholar, User, Wish) {
        var vm = this;
        vm.inscription = entity;
        vm.classrooms = ClassRoom.query({filter: 'inscription-is-null'});
        $q.all([vm.inscription.$promise, vm.classrooms.$promise]).then(function() {
            if (!vm.inscription.classRoom || !vm.inscription.classRoom.id) {
                return $q.reject();
            }
            return ClassRoom.get({id : vm.inscription.classRoom.id}).$promise;
        }).then(function(classRoom) {
            vm.classrooms.push(classRoom);
        });
        vm.scholars = Scholar.query({filter: 'inscription-is-null'});
        $q.all([vm.inscription.$promise, vm.scholars.$promise]).then(function() {
            if (!vm.inscription.scholar || !vm.inscription.scholar.id) {
                return $q.reject();
            }
            return Scholar.get({id : vm.inscription.scholar.id}).$promise;
        }).then(function(scholar) {
            vm.scholars.push(scholar);
        });
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
