'use strict';

describe('Controller Tests', function() {

    describe('PersonInCharge Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPersonInCharge, MockScholar;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPersonInCharge = jasmine.createSpy('MockPersonInCharge');
            MockScholar = jasmine.createSpy('MockScholar');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PersonInCharge': MockPersonInCharge,
                'Scholar': MockScholar
            };
            createController = function() {
                $injector.get('$controller')("PersonInChargeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'madrassaApp:personInChargeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
