(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('ScholarDialogController', ScholarDialogController);

    ScholarDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Scholar', 'PersonInCharge', 'ClassRoom'];

    function ScholarDialogController ($scope, $stateParams, $uibModalInstance, DataUtils, entity, Scholar, PersonInCharge, ClassRoom) {
        var vm = this;
        vm.scholar = entity;
        vm.personincharges = PersonInCharge.query();
        vm.classrooms = ClassRoom.query();
        vm.load = function(id) {
            Scholar.get({id : id}, function(result) {
                vm.scholar = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('madrassaApp:scholarUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.scholar.id !== null) {
                Scholar.update(vm.scholar, onSaveSuccess, onSaveError);
            } else {
                Scholar.save(vm.scholar, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.birthDate = false;

        vm.setPhoto = function ($file, scholar) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        scholar.photo = base64Data;
                        scholar.photoContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
