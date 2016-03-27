'use strict';

describe('Controller Tests', function() {

    describe('Inscription Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInscription, MockClassRoom, MockScholar, MockUser, MockWish;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInscription = jasmine.createSpy('MockInscription');
            MockClassRoom = jasmine.createSpy('MockClassRoom');
            MockScholar = jasmine.createSpy('MockScholar');
            MockUser = jasmine.createSpy('MockUser');
            MockWish = jasmine.createSpy('MockWish');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Inscription': MockInscription,
                'ClassRoom': MockClassRoom,
                'Scholar': MockScholar,
                'User': MockUser,
                'Wish': MockWish
            };
            createController = function() {
                $injector.get('$controller')("InscriptionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'madrassaApp:inscriptionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
