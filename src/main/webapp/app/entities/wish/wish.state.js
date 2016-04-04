(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wish', {
            parent: 'entity',
            url: '/wish',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.wish.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wish/wishes.html',
                    controller: 'WishController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wish');
                    $translatePartialLoader.addPart('momentEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wish-detail', {
            parent: 'entity',
            url: '/wish/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.wish.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wish/wish-detail.html',
                    controller: 'WishDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wish');
                    $translatePartialLoader.addPart('momentEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Wish', function($stateParams, Wish) {
                    return Wish.get({id : $stateParams.id});
                }]
            }
        })
        .state('wish.new', {
            parent: 'wish',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wish/wish-dialog.html',
                    controller: 'WishDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                moment: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wish', null, { reload: true });
                }, function() {
                    $state.go('wish');
                });
            }]
        })
        .state('wish.edit', {
            parent: 'wish',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wish/wish-dialog.html',
                    controller: 'WishDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Wish', function(Wish) {
                            return Wish.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('wish', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wish.delete', {
            parent: 'wish',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wish/wish-delete-dialog.html',
                    controller: 'WishDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Wish', function(Wish) {
                            return Wish.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('wish', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
