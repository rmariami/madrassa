(function() {
    'use strict';
    angular
        .module('madrassaApp')
        .factory('Inscription', Inscription);

    Inscription.$inject = ['$resource', 'DateUtils'];

    function Inscription ($resource, DateUtils) {
        var resourceUrl =  'api/inscriptions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
