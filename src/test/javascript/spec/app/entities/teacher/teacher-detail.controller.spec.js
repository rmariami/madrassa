'use strict';

describe('Controller Tests', function() {

    describe('Teacher Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTeacher, MockUser, MockClassRoom;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTeacher = jasmine.createSpy('MockTeacher');
            MockUser = jasmine.createSpy('MockUser');
            MockClassRoom = jasmine.createSpy('MockClassRoom');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Teacher': MockTeacher,
                'User': MockUser,
                'ClassRoom': MockClassRoom
            };
            createController = function() {
                $injector.get('$controller')("TeacherDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'madrassaApp:teacherUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
