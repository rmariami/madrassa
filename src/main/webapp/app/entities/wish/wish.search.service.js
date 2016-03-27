(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .factory('WishSearch', WishSearch);

    WishSearch.$inject = ['$resource'];

    function WishSearch($resource) {
        var resourceUrl =  'api/_search/wishes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
