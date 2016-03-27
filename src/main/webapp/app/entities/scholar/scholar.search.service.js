(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .factory('ScholarSearch', ScholarSearch);

    ScholarSearch.$inject = ['$resource'];

    function ScholarSearch($resource) {
        var resourceUrl =  'api/_search/scholars/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
