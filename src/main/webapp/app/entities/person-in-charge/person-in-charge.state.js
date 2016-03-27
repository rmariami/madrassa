(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('person-in-charge', {
            parent: 'entity',
            url: '/person-in-charge',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.personInCharge.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/person-in-charge/person-in-charges.html',
                    controller: 'PersonInChargeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personInCharge');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('person-in-charge-detail', {
            parent: 'entity',
            url: '/person-in-charge/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.personInCharge.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/person-in-charge/person-in-charge-detail.html',
                    controller: 'PersonInChargeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personInCharge');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PersonInCharge', function($stateParams, PersonInCharge) {
                    return PersonInCharge.get({id : $stateParams.id});
                }]
            }
        })
        .state('person-in-charge.new', {
            parent: 'person-in-charge',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person-in-charge/person-in-charge-dialog.html',
                    controller: 'PersonInChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                firstName: null,
                                adress: null,
                                work: null,
                                phoneNumber: null,
                                mobilePhoneNumber: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('person-in-charge', null, { reload: true });
                }, function() {
                    $state.go('person-in-charge');
                });
            }]
        })
        .state('person-in-charge.edit', {
            parent: 'person-in-charge',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person-in-charge/person-in-charge-dialog.html',
                    controller: 'PersonInChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PersonInCharge', function(PersonInCharge) {
                            return PersonInCharge.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('person-in-charge', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('person-in-charge.delete', {
            parent: 'person-in-charge',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person-in-charge/person-in-charge-delete-dialog.html',
                    controller: 'PersonInChargeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PersonInCharge', function(PersonInCharge) {
                            return PersonInCharge.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('person-in-charge', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
