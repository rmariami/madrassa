(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('teacher', {
            parent: 'entity',
            url: '/teacher',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.teacher.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/teacher/teachers.html',
                    controller: 'TeacherController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('teacher');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('teacher-detail', {
            parent: 'entity',
            url: '/teacher/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'madrassaApp.teacher.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/teacher/teacher-detail.html',
                    controller: 'TeacherDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('teacher');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Teacher', function($stateParams, Teacher) {
                    return Teacher.get({id : $stateParams.id});
                }]
            }
        })
        .state('teacher.new', {
            parent: 'teacher',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/teacher/teacher-dialog.html',
                    controller: 'TeacherDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                firstName: null,
                                adress: null,
                                phoneNumber: null,
                                mobilePhoneNumber: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('teacher', null, { reload: true });
                }, function() {
                    $state.go('teacher');
                });
            }]
        })
        .state('teacher.edit', {
            parent: 'teacher',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/teacher/teacher-dialog.html',
                    controller: 'TeacherDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Teacher', function(Teacher) {
                            return Teacher.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('teacher', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('teacher.delete', {
            parent: 'teacher',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/teacher/teacher-delete-dialog.html',
                    controller: 'TeacherDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Teacher', function(Teacher) {
                            return Teacher.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('teacher', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
