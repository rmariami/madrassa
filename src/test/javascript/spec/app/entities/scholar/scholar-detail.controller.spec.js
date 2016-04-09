'use strict';

describe('Controller Tests', function() {

    describe('Scholar Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockScholar, MockPersonInCharge, MockClassRoom;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockScholar = jasmine.createSpy('MockScholar');
            MockPersonInCharge = jasmine.createSpy('MockPersonInCharge');
            MockClassRoom = jasmine.createSpy('MockClassRoom');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Scholar': MockScholar,
                'PersonInCharge': MockPersonInCharge,
                'ClassRoom': MockClassRoom
            };
            createController = function() {
                $injector.get('$controller')("ScholarDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'madrassaApp:scholarUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
