(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('ClassRoomDialogController', ClassRoomDialogController);

    ClassRoomDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClassRoom', 'Teacher', 'Scholar'];

    function ClassRoomDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClassRoom, Teacher, Scholar) {
        var vm = this;
        vm.classRoom = entity;
        vm.teachers = Teacher.query();
        vm.scholars = Scholar.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

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
