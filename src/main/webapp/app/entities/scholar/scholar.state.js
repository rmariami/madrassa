(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('scholar', {
            parent: 'entity',
            url: '/scholar',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.scholar.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/scholar/scholars.html',
                    controller: 'ScholarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('scholar');
                    $translatePartialLoader.addPart('sexEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('scholar-detail', {
            parent: 'entity',
            url: '/scholar/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.scholar.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/scholar/scholar-detail.html',
                    controller: 'ScholarDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('scholar');
                    $translatePartialLoader.addPart('sexEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Scholar', function($stateParams, Scholar) {
                    return Scholar.get({id : $stateParams.id});
                }]
            }
        })
        .state('scholar.new', {
            parent: 'scholar',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/scholar/scholar-dialog.html',
                    controller: 'ScholarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                scholarNumber: null,
                                sex: null,
                                name: null,
                                firstName: null,
                                birthDate: null,
                                birthPlace: null,
                                photo: null,
                                photoContentType: null,
                                nbYearsXP: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('scholar', null, { reload: true });
                }, function() {
                    $state.go('scholar');
                });
            }]
        })
        .state('scholar.edit', {
            parent: 'scholar',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/scholar/scholar-dialog.html',
                    controller: 'ScholarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Scholar', function(Scholar) {
                            return Scholar.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('scholar', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('scholar.delete', {
            parent: 'scholar',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/scholar/scholar-delete-dialog.html',
                    controller: 'ScholarDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Scholar', function(Scholar) {
                            return Scholar.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('scholar', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
