(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('class-room', {
            parent: 'entity',
            url: '/class-room',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.classRoom.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-room/class-rooms.html',
                    controller: 'ClassRoomController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classRoom');
                    $translatePartialLoader.addPart('classMoment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('class-room-detail', {
            parent: 'entity',
            url: '/class-room/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.classRoom.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-room/class-room-detail.html',
                    controller: 'ClassRoomDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classRoom');
                    $translatePartialLoader.addPart('classMoment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClassRoom', function($stateParams, ClassRoom) {
                    return ClassRoom.get({id : $stateParams.id});
                }]
            }
        })
        .state('class-room.new', {
            parent: 'class-room',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-room/class-room-dialog.html',
                    controller: 'ClassRoomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                moment: null,
                                startHour: null,
                                endHour: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('class-room', null, { reload: true });
                }, function() {
                    $state.go('class-room');
                });
            }]
        })
        .state('class-room.edit', {
            parent: 'class-room',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-room/class-room-dialog.html',
                    controller: 'ClassRoomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClassRoom', function(ClassRoom) {
                            return ClassRoom.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-room', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('class-room.delete', {
            parent: 'class-room',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-room/class-room-delete-dialog.html',
                    controller: 'ClassRoomDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClassRoom', function(ClassRoom) {
                            return ClassRoom.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-room', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
