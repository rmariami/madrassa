(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .factory('PersonInChargeSearch', PersonInChargeSearch);

    PersonInChargeSearch.$inject = ['$resource'];

    function PersonInChargeSearch($resource) {
        var resourceUrl =  'api/_search/person-in-charges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
