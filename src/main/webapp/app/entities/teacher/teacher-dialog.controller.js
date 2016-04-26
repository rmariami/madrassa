(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('TeacherDialogController', TeacherDialogController);

    TeacherDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Teacher', 'User', 'ClassRoom'];

    function TeacherDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Teacher, User, ClassRoom) {
        var vm = this;
        vm.teacher = entity;
        vm.users = User.query();
        vm.classrooms = ClassRoom.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('madrassaApp:teacherUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.teacher.id !== null) {
                Teacher.update(vm.teacher, onSaveSuccess, onSaveError);
            } else {
                Teacher.save(vm.teacher, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
