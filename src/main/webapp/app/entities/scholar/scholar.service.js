(function() {
    'use strict';
    angular
        .module('madrassaApp')
        .factory('Scholar', Scholar);

    Scholar.$inject = ['$resource', 'DateUtils'];

    function Scholar ($resource, DateUtils) {
        var resourceUrl =  'api/scholars/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.birthDate = DateUtils.convertDateTimeFromServer(data.birthDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
