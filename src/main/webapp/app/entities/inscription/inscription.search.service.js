(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .factory('InscriptionSearch', InscriptionSearch);

    InscriptionSearch.$inject = ['$resource'];

    function InscriptionSearch($resource) {
        var resourceUrl =  'api/_search/inscriptions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
