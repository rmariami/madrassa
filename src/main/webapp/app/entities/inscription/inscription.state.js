(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inscription', {
            parent: 'entity',
            url: '/inscription',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.inscription.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscription/inscriptions.html',
                    controller: 'InscriptionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscription');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('inscription-detail', {
            parent: 'entity',
            url: '/inscription/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.inscription.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscription/inscription-detail.html',
                    controller: 'InscriptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscription');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Inscription', function($stateParams, Inscription) {
                    return Inscription.get({id : $stateParams.id});
                }]
            }
        })
        .state('inscription.new', {
            parent: 'inscription',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscription/inscription-dialog.html',
                    controller: 'InscriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                price: null,
                                statut: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inscription', null, { reload: true });
                }, function() {
                    $state.go('inscription');
                });
            }]
        })
        .state('inscription.edit', {
            parent: 'inscription',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscription/inscription-dialog.html',
                    controller: 'InscriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inscription', function(Inscription) {
                            return Inscription.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscription', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inscription.delete', {
            parent: 'inscription',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscription/inscription-delete-dialog.html',
                    controller: 'InscriptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Inscription', function(Inscription) {
                            return Inscription.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscription', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
