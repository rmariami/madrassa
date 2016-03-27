(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('ClassRoomDeleteController',ClassRoomDeleteController);

    ClassRoomDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClassRoom'];

    function ClassRoomDeleteController($uibModalInstance, entity, ClassRoom) {
        var vm = this;
        vm.classRoom = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ClassRoom.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
