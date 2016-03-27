(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('WishDialogController', WishDialogController);

    WishDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Wish', 'Inscription'];

    function WishDialogController ($scope, $stateParams, $uibModalInstance, entity, Wish, Inscription) {
        var vm = this;
        vm.wish = entity;
        vm.inscriptions = Inscription.query();
        vm.load = function(id) {
            Wish.get({id : id}, function(result) {
                vm.wish = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('madrassaApp:wishUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.wish.id !== null) {
                Wish.update(vm.wish, onSaveSuccess, onSaveError);
            } else {
                Wish.save(vm.wish, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
