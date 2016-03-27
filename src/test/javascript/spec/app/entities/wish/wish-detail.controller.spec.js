'use strict';

describe('Controller Tests', function() {

    describe('Wish Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWish, MockInscription;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWish = jasmine.createSpy('MockWish');
            MockInscription = jasmine.createSpy('MockInscription');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Wish': MockWish,
                'Inscription': MockInscription
            };
            createController = function() {
                $injector.get('$controller')("WishDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'madrassaApp:wishUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
