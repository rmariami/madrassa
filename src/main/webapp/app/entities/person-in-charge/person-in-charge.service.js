(function() {
    'use strict';
    angular
        .module('madrassaApp')
        .factory('PersonInCharge', PersonInCharge);

    PersonInCharge.$inject = ['$resource'];

    function PersonInCharge ($resource) {
        var resourceUrl =  'api/person-in-charges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
