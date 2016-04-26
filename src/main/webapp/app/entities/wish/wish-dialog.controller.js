(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('WishDialogController', WishDialogController);

    WishDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Wish', 'Inscription'];

    function WishDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Wish, Inscription) {
        var vm = this;
        vm.wish = entity;
        vm.inscriptions = Inscription.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

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
