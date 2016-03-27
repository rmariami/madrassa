(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('ClassRoomDialogController', ClassRoomDialogController);

    ClassRoomDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClassRoom', 'Teacher', 'Scholar'];

    function ClassRoomDialogController ($scope, $stateParams, $uibModalInstance, entity, ClassRoom, Teacher, Scholar) {
        var vm = this;
        vm.classRoom = entity;
        vm.teachers = Teacher.query();
        vm.scholars = Scholar.query();
        vm.load = function(id) {
            ClassRoom.get({id : id}, function(result) {
                vm.classRoom = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('madrassaApp:classRoomUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.classRoom.id !== null) {
                ClassRoom.update(vm.classRoom, onSaveSuccess, onSaveError);
            } else {
                ClassRoom.save(vm.classRoom, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
