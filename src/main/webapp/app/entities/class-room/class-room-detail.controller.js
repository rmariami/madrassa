(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('ClassRoomDetailController', ClassRoomDetailController);

    ClassRoomDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ClassRoom', 'Teacher', 'Scholar'];

    function ClassRoomDetailController($scope, $rootScope, $stateParams, entity, ClassRoom, Teacher, Scholar) {
        var vm = this;
        vm.classRoom = entity;
        
        var unsubscribe = $rootScope.$on('madrassaApp:classRoomUpdate', function(event, result) {
            vm.classRoom = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
