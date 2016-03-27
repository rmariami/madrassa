(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('WishDeleteController',WishDeleteController);

    WishDeleteController.$inject = ['$uibModalInstance', 'entity', 'Wish'];

    function WishDeleteController($uibModalInstance, entity, Wish) {
        var vm = this;
        vm.wish = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Wish.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
