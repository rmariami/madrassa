(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('ScholarDeleteController',ScholarDeleteController);

    ScholarDeleteController.$inject = ['$uibModalInstance', 'entity', 'Scholar'];

    function ScholarDeleteController($uibModalInstance, entity, Scholar) {
        var vm = this;
        vm.scholar = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Scholar.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
