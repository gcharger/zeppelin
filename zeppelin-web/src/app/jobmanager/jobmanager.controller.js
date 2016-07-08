/*jshint loopfunc: true, unused:false */
/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

angular.module('zeppelinWebApp')
  .filter('jobManager', function() {

    function filterContext(jobItems, filterConfig) {
      var filterValueInterpreter = filterConfig.filterValueInterpreter;
      var filterValueNotebookName = filterConfig.filterValueNotebookName;
      var isRunningAlwaysTop = filterConfig.isRunningAlwaysTop;
      var isSortByAsc = filterConfig.isSortByAsc;

      var filterItems = jobItems;

      if (filterValueInterpreter === undefined) {
        filterItems = _.filter(filterItems, function (jobItem) {
          return jobItem.interpreter === undefined? true : false;
        });
      } else if (filterValueInterpreter !== '*') {
        filterItems = _.where(filterItems, {interpreter : filterValueInterpreter});
      }

      if (filterValueNotebookName !== '') {
        filterItems = _.filter(filterItems, function(jobItem){
          var lowerFilterValue = filterValueNotebookName.toLocaleLowerCase();
          var lowerNotebookName = jobItem.notebookName.toLocaleLowerCase();
          return lowerNotebookName.match(new RegExp('.*'+lowerFilterValue+'.*'));
        });
      }

      if (isSortByAsc === true) {
        filterItems = _.sortBy(filterItems, function (sortItem) {
          return sortItem.notebookName;
        });
      } else {
        filterItems = _.sortBy(filterItems, function (sortItem) {
          return sortItem.notebookName;
        });
        filterItems = filterItems.reverse();
      }

      if (isRunningAlwaysTop === true) {
        var runningJobList = _.where(filterItems, {isRunningJob : true});
        filterItems = _.reject(filterItems, {isRunningJob : true});
        runningJobList.map(function (runningJob) {
          filterItems.splice(0,0, runningJob);
        });
      }

      return filterItems;
    }
    return filterContext;
  })
  .controller('JobmanagerCtrl',
    function($scope, $route, $routeParams, $location, $rootScope, $http, $q,
             websocketMsgSrv, baseUrlSrv, $interval, $timeout, jobManagerFilter) {

      $scope.$on('setNotebookJobs', function(event, responseData) {
        $scope.lastJobServerUnixTime = responseData.lastResponseUnixTime;
        $scope.jobInfomations = responseData.jobs;
        $scope.jobInfomationsIndexs = $scope.jobInfomations? _.indexBy($scope.jobInfomations, 'notebookId') : {};
        $scope.JobInfomationsByFilter = $scope.jobTypeFilter($scope.jobInfomations, $scope.filterConfig);
        $scope.activeInterpreters = [
          {
            name : 'ALL',
            value : '*'
          }
        ];
        var interpreterLists = _.uniq(_.pluck($scope.jobInfomations, 'interpreter'), false);
        for (var index = 0, length = interpreterLists.length; index < length; index++) {
          $scope.activeInterpreters.push({
            name : interpreterLists[index],
            value : interpreterLists[index]
          });
        }
        $scope.doFiltering($scope.jobInfomations, $scope.filterConfig);
      });

      $scope.$on('setUpdateNotebookJobs', function(event, responseData) {
        var jobInfomations = $scope.jobInfomations;
        var indexStore = $scope.jobInfomationsIndexs;
        $scope.lastJobServerUnixTime = responseData.lastResponseUnixTime;
        var notes = responseData.jobs;
        notes.map(function (changedItem) {
          if (indexStore[changedItem.notebookId] === undefined) {
            var newItem = angular.copy(changedItem);
            jobInfomations.push(newItem);
            indexStore[changedItem.notebookId] = newItem;
          } else {
            var changeOriginTarget = indexStore[changedItem.notebookId];

            if (changedItem.isRemoved !== undefined && changedItem.isRemoved === true) {

              // remove Item.
              var removeIndex = _.findIndex(indexStore, changedItem.notebookId);
              if (removeIndex > -1) {
                indexStore.splice(removeIndex, 1);
              }

              removeIndex = _.findIndex(jobInfomations, { 'notebookId' : changedItem.notebookId});
              if (removeIndex) {
                jobInfomations.splice(removeIndex, 1);
              }

            } else {
              // change value for item.
              changeOriginTarget.isRunningJob = changedItem.isRunningJob;
              changeOriginTarget.notebookName = changedItem.notebookName;
              changeOriginTarget.notebookType = changedItem.notebookType;
              changeOriginTarget.interpreter = changedItem.interpreter;
              changeOriginTarget.unixTimeLastRun = changedItem.unixTimeLastRun;
              changeOriginTarget.paragraphs = changedItem.paragraphs;
            }
          }
          $scope.doFiltering(jobInfomations, $scope.filterConfig);
        });
      });

      $scope.doFiltering = function (jobInfomations, filterConfig) {
        asyncNotebookJobFilter(jobInfomations, filterConfig).then(
          function () {
            // success
            $scope.isLoadingFilter = false;
          },
          function (){
            // failed
          });
      };

      $scope.filterValueToName = function (filterValue, maxStringLength) {
        if ($scope.activeInterpreters === undefined) {
          return;
        }
        var index = _.findIndex($scope.activeInterpreters, {value : filterValue});
        if (index < 0) {
          console.log('filtervalue [{}]', filterValue, ' ', $scope.activeInterpreters);
        }
        if ($scope.activeInterpreters[index].name !== undefined) {
          if (maxStringLength !== undefined && maxStringLength > $scope.activeInterpreters[index].name) {
            return $scope.activeInterpreters[index].name.substr(0, maxStringLength -3) + '...';
          }
          return $scope.activeInterpreters[index].name;
        } else {
          return 'Undefined';
        }
      };

      $scope.setFilterValue = function (filterValue) {
        $scope.filterConfig.filterValueInterpreter = filterValue;
        $scope.doFiltering($scope.jobInfomations, $scope.filterConfig);
      };

      $scope.onChangeRunJobToAlwaysTopToggle = function () {
        $scope.filterConfig.isRunningAlwaysTop = !$scope.filterConfig.isRunningAlwaysTop;
        $scope.doFiltering($scope.jobInfomations, $scope.filterConfig);
      };

      $scope.onChangeSortAsc = function () {
        $scope.filterConfig.isSortByAsc = !$scope.filterConfig.isSortByAsc;
        $scope.doFiltering($scope.jobInfomations, $scope.filterConfig);
      };

      $scope.doFilterInputTyping = function (keyEvent, jobInfomations, filterConfig) {
        var RETURN_KEY_CODE = 13;
        $timeout.cancel($scope.dofilterTimeoutObject);
        $scope.dofilterTimeoutObject = $timeout(function(){
          $scope.doFiltering(jobInfomations, filterConfig);
        }, 1000);
        if (keyEvent.which === RETURN_KEY_CODE) {
          $timeout.cancel($scope.dofilterTimeoutObject);
          $scope.doFiltering(jobInfomations, filterConfig);
        }
      };

      $scope.init = function () {
        $scope.isLoadingFilter = true;
        $scope.filterConfig = {
          isRunningAlwaysTop : true,
          filterValueNotebookName : '',
          filterValueInterpreter : '*',
          isSortByAsc : true
        };
        $scope.jobTypeFilter = jobManagerFilter;
        $scope.jobInfomations = [];
        $scope.JobInfomationsByFilter = $scope.jobInfomations;

        websocketMsgSrv.getNotebookJobsList();
        var refreshObj = $interval(function () {
          if ($scope.lastJobServerUnixTime !== undefined) {
            websocketMsgSrv.getUpdateNotebookJobsList($scope.lastJobServerUnixTime);
          }
        }, 1000);

        $scope.$on('$destroy', function() {
          $interval.cancel(refreshObj);
          websocketMsgSrv.unsubscribeJobManager();
        });
      };

      var asyncNotebookJobFilter = function (jobInfomations, filterConfig) {
        return $q(function(resolve, reject) {
          $scope.JobInfomationsByFilter = $scope.jobTypeFilter(jobInfomations, filterConfig);
          resolve($scope.JobInfomationsByFilter);
        });
      };
});
