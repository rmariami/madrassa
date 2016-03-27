(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('TeacherDialogController', TeacherDialogController);

    TeacherDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Teacher', 'User', 'ClassRoom'];

    function TeacherDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Teacher, User, ClassRoom) {
        var vm = this;
        vm.teacher = entity;
        vm.users = User.query();
        vm.classrooms = ClassRoom.query();
        vm.load = function(id) {
            Teacher.get({id : id}, function(result) {
                vm.teacher = result;
            });
        };

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
