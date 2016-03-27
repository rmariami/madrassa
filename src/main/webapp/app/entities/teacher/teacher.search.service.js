(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .factory('TeacherSearch', TeacherSearch);

    TeacherSearch.$inject = ['$resource'];

    function TeacherSearch($resource) {
        var resourceUrl =  'api/_search/teachers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
