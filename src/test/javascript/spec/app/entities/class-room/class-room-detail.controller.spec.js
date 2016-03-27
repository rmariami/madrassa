'use strict';

describe('Controller Tests', function() {

    describe('ClassRoom Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClassRoom, MockTeacher, MockScholar;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClassRoom = jasmine.createSpy('MockClassRoom');
            MockTeacher = jasmine.createSpy('MockTeacher');
            MockScholar = jasmine.createSpy('MockScholar');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ClassRoom': MockClassRoom,
                'Teacher': MockTeacher,
                'Scholar': MockScholar
            };
            createController = function() {
                $injector.get('$controller')("ClassRoomDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'madrassaApp:classRoomUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
