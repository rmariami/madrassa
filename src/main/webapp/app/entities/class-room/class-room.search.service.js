(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .factory('ClassRoomSearch', ClassRoomSearch);

    ClassRoomSearch.$inject = ['$resource'];

    function ClassRoomSearch($resource) {
        var resourceUrl =  'api/_search/class-rooms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
