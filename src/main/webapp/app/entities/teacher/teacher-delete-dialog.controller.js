(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('TeacherDeleteController',TeacherDeleteController);

    TeacherDeleteController.$inject = ['$uibModalInstance', 'entity', 'Teacher'];

    function TeacherDeleteController($uibModalInstance, entity, Teacher) {
        var vm = this;
        vm.teacher = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Teacher.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
