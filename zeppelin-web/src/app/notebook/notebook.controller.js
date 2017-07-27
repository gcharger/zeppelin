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

import moment from 'moment'

import { isParagraphRunning, } from './paragraph/paragraph.status'

angular.module('zeppelinWebApp').controller('NotebookCtrl', NotebookCtrl)

function NotebookCtrl ($scope, $route, $routeParams, $location, $rootScope,
                      $http, websocketMsgSrv, baseUrlSrv, $timeout, saveAsService,
                      ngToast, noteActionService, noteVarShareService, TRASH_FOLDER_ID,
                      heliumService) {
  'ngInject'

  ngToast.dismiss()

  $scope.note = null
  $scope.editorToggled = false
  $scope.tableToggled = false
  $scope.viewOnly = false
  $scope.showSetting = false
  $scope.looknfeelOption = ['default', 'simple', 'report']
  $scope.cronOption = [
    {name: 'None', value: undefined},
    {name: '1m', value: '0 0/1 * * * ?'},
    {name: '5m', value: '0 0/5 * * * ?'},
    {name: '1h', value: '0 0 0/1 * * ?'},
    {name: '3h', value: '0 0 0/3 * * ?'},
    {name: '6h', value: '0 0 0/6 * * ?'},
    {name: '12h', value: '0 0 0/12 * * ?'},
    {name: '1d', value: '0 0 0 * * ?'}
  ]

  $scope.formatRevisionDate = function(date) {
    return moment.unix(date).format('MMMM Do YYYY, h:mm a')
  }

  $scope.interpreterSettings = []
  $scope.interpreterBindings = []
  $scope.isNoteDirty = null
  $scope.saveTimer = null

  let connectedOnce = false
  let isRevisionPath = function (path) {
    let pattern = new RegExp('^.*\/notebook\/[a-zA-Z0-9_]*\/revision\/[a-zA-Z0-9_]*')
    return pattern.test(path)
  }

  $scope.noteRevisions = []
  $scope.firstNoteRevisionForCompare = null
  $scope.secondNoteRevisionForCompare = null
  $scope.mergeNoteRevisionsForCompare = null
  $scope.currentFirstRevisionForCompare = 'Choose...'
  $scope.currentSecondRevisionForCompare = 'Choose...'
  $scope.currentRevision = 'Head'
  $scope.revisionView = isRevisionPath($location.path())

  $scope.$watch('note', function (value) {
    $rootScope.pageTitle = value ? value.name : 'Zeppelin'
  }, true)

  $scope.$on('setConnectedStatus', function (event, param) {
    if (connectedOnce && param) {
      initNotebook()
    }
    connectedOnce = true
  })

  $scope.getCronOptionNameFromValue = function (value) {
    if (!value) {
      return ''
    }

    for (let o in $scope.cronOption) {
      if ($scope.cronOption[o].value === value) {
        return $scope.cronOption[o].name
      }
    }
    return value
  }

  $scope.blockAnonUsers = function () {
    let zeppelinVersion = $rootScope.zeppelinVersion
    let url = 'https://zeppelin.apache.org/docs/' + zeppelinVersion + '/security/notebook_authorization.html'
    let content = 'Only authenticated user can set the permission.' +
      '<a data-toggle="tooltip" data-placement="top" title="Learn more" target="_blank" href=' + url + '>' +
      '<i class="icon-question" />' +
      '</a>'
    BootstrapDialog.show({
      closable: false,
      closeByBackdrop: false,
      closeByKeyboard: false,
      title: 'No permission',
      message: content,
      buttons: [{
        label: 'Close',
        action: function (dialog) {
          dialog.close()
        }
      }]
    })
  }

  /** Init the new controller */
  const initNotebook = function () {
    noteVarShareService.clear()
    if ($routeParams.revisionId) {
      websocketMsgSrv.getNoteByRevision($routeParams.noteId, $routeParams.revisionId)
    } else {
      websocketMsgSrv.getNote($routeParams.noteId)
    }
    websocketMsgSrv.listRevisionHistory($routeParams.noteId)
    let currentRoute = $route.current
    if (currentRoute) {
      setTimeout(
        function () {
          let routeParams = currentRoute.params
          let $id = angular.element('#' + routeParams.paragraph + '_container')

          if ($id.length > 0) {
            // adjust for navbar
            let top = $id.offset().top - 103
            angular.element('html, body').scrollTo({top: top, left: 0})
          }
        },
        1000
      )
    }
  }

  initNotebook()

  $scope.focusParagraphOnClick = function (clickEvent) {
    if (!$scope.note) {
      return
    }
    for (let i = 0; i < $scope.note.paragraphs.length; i++) {
      let paragraphId = $scope.note.paragraphs[i].id
      if (jQuery.contains(angular.element('#' + paragraphId + '_container')[0], clickEvent.target)) {
        $scope.$broadcast('focusParagraph', paragraphId, 0, true)
        break
      }
    }
  }

  // register mouseevent handler for focus paragraph
  document.addEventListener('click', $scope.focusParagraphOnClick)

  $scope.keyboardShortcut = function (keyEvent) {
    // handle keyevent
    if (!$scope.viewOnly && !$scope.revisionView) {
      $scope.$broadcast('keyEvent', keyEvent)
    }
  }

  // register mouseevent handler for focus paragraph
  document.addEventListener('keydown', $scope.keyboardShortcut)

  $scope.paragraphOnDoubleClick = function (paragraphId) {
    $scope.$broadcast('doubleClickParagraph', paragraphId)
  }

  // Move the note to trash and go back to the main page
  $scope.moveNoteToTrash = function (noteId) {
    noteActionService.moveNoteToTrash(noteId, true)
  }

  // Remove the note permanently if it's in the trash
  $scope.removeNote = function (noteId) {
    noteActionService.removeNote(noteId, true)
  }

  $scope.isTrash = function (note) {
    return note ? note.name.split('/')[0] === TRASH_FOLDER_ID : false
  }

  // Export notebook
  $scope.exportNote = function () {
    let jsonContent = JSON.stringify($scope.note)
    saveAsService.saveAs(jsonContent, $scope.note.name, 'json')
  }

  // Clone note
  $scope.cloneNote = function (noteId) {
    BootstrapDialog.confirm({
      closable: true,
      title: '',
      message: 'Do you want to clone this note?',
      callback: function (result) {
        if (result) {
          websocketMsgSrv.cloneNote(noteId)
          $location.path('/')
        }
      }
    })
  }

  // checkpoint/commit notebook
  $scope.checkpointNote = function (commitMessage) {
    BootstrapDialog.confirm({
      closable: true,
      title: '',
      message: 'Commit note to current repository?',
      callback: function (result) {
        if (result) {
          websocketMsgSrv.checkpointNote($routeParams.noteId, commitMessage)
        }
      }
    })
    document.getElementById('note.checkpoint.message').value = ''
  }

  // set notebook head to given revision
  $scope.setNoteRevision = function () {
    BootstrapDialog.confirm({
      closable: true,
      title: '',
      message: 'Set notebook head to current revision?',
      callback: function (result) {
        if (result) {
          websocketMsgSrv.setNoteRevision($routeParams.noteId, $routeParams.revisionId)
        }
      }
    })
  }

  $scope.preVisibleRevisionsComparator = function() {
    $scope.mergeNoteRevisionsForCompare = null
    $scope.firstNoteRevisionForCompare = null
    $scope.secondNoteRevisionForCompare = null
    $scope.currentFirstRevisionForCompare = 'Choose...'
    $scope.currentSecondRevisionForCompare = 'Choose...'
    $scope.$apply()
  }

  $scope.$on('listRevisionHistory', function (event, data) {
    console.debug('received list of revisions %o', data)
    $scope.noteRevisions = data.revisionList
    if ($scope.noteRevisions.length === 0 || $scope.noteRevisions[0].id !== 'Head') {
      $scope.noteRevisions.splice(0, 0, {
        id: 'Head',
        message: 'Head'
      })
    }
    if ($routeParams.revisionId) {
      let index = _.findIndex($scope.noteRevisions, {'id': $routeParams.revisionId})
      if (index > -1) {
        $scope.currentRevision = $scope.noteRevisions[index].message
      }
    }
  })

  $scope.$on('noteRevision', function (event, data) {
    console.log('received note revision %o', data)
    if (data.note) {
      $scope.note = data.note
      initializeLookAndFeel()
    } else {
      $location.path('/')
    }
  })

  $scope.$on('setNoteRevisionResult', function (event, data) {
    console.log('received set note revision result %o', data)
    if (data.status) {
      $location.path('/notebook/' + $routeParams.noteId)
    }
  })

  $scope.visitRevision = function (revision) {
    if (revision.id) {
      if (revision.id === 'Head') {
        $location.path('/notebook/' + $routeParams.noteId)
      } else {
        $location.path('/notebook/' + $routeParams.noteId + '/revision/' + revision.id)
      }
    } else {
      ngToast.danger({content: 'There is a problem with this Revision',
        verticalPosition: 'top',
        dismissOnTimeout: false
      })
    }
  }

  // compare revisions
  $scope.compareRevisions = function () {
    if ($scope.firstNoteRevisionForCompare && $scope.secondNoteRevisionForCompare) {
      let paragraphs1 = $scope.firstNoteRevisionForCompare.note.paragraphs
      let paragraphs2 = $scope.secondNoteRevisionForCompare.note.paragraphs
      let merge = {
        added: [],
        deleted: [],
        compared: []
      }
      for (let p1 of paragraphs1) {
        let p2 = null
        for (let p of paragraphs2) {
          if (p1.id === p.id) {
            p2 = p
            break
          }
        }
        if (p2 === null) {
          merge.deleted.push({paragraph: p1, firstString: (p1.text || '').split('\n')[0]})
        } else {
          let colorClass = ''
          let span = null
          let text1 = p1.text || ''
          let text2 = p2.text || ''

          let diff = window.JsDiff.diffLines(text1, text2)
          let diffHtml = document.createDocumentFragment()
          let identical = true
          let identicalClass = 'color-black'

          diff.forEach(function(part) {
            colorClass = part.added ? 'color-green' : part.removed ? 'color-red' : identicalClass
            span = document.createElement('span')
            span.className = colorClass
            if (identical && colorClass !== identicalClass) {
              identical = false
            }
            span.appendChild(document.createTextNode(part.value))
            diffHtml.appendChild(span)
          })

          let pre = document.createElement('pre')
          pre.appendChild(diffHtml)

          merge.compared.push(
            {paragraph: p1, diff: pre.innerHTML, identical: identical, firstString: (p1.text || '').split('\n')[0]})
        }
      }

      for (let p2 of paragraphs2) {
        let p1 = null
        for (let p of paragraphs1) {
          if (p2.id === p.id) {
            p1 = p
            break
          }
        }
        if (p1 === null) {
          merge.added.push({paragraph: p2, firstString: (p2.text || '').split('\n')[0]})
        }
      }
      $scope.mergeNoteRevisionsForCompare = merge
    }
  }

  $scope.getNoteRevisionForReview = function (revision, position) {
    if (position) {
      if (position === 'first') {
        $scope.currentFirstRevisionForCompare = revision.message
      } else {
        $scope.currentSecondRevisionForCompare = revision.message
      }
      websocketMsgSrv.getNoteByRevisionForCompare($routeParams.noteId, revision.id, position)
    }
  }

  $scope.$on('noteRevisionForCompare', function (event, data) {
    console.debug('received note revision for compare %o', data)
    if (data.note && data.position) {
      if (data.position === 'first') {
        $scope.firstNoteRevisionForCompare = data
      } else {
        $scope.secondNoteRevisionForCompare = data
      }

      if ($scope.firstNoteRevisionForCompare !== null && $scope.secondNoteRevisionForCompare !== null &&
        $scope.firstNoteRevisionForCompare.revisionId !== $scope.secondNoteRevisionForCompare.revisionId) {
        $scope.compareRevisions()
      }
    }
  })

  $scope.runAllParagraphs = function (noteId) {
    BootstrapDialog.confirm({
      closable: true,
      title: '',
      message: 'Run all paragraphs?',
      callback: function (result) {
        if (result) {
          const paragraphs = $scope.note.paragraphs.map(p => {
            return {
              id: p.id,
              title: p.title,
              paragraph: p.text,
              config: p.config,
              params: p.settings.params
            }
          })
          websocketMsgSrv.runAllParagraphs(noteId, paragraphs)
        }
      }
    })
  }

  $scope.saveNote = function () {
    if ($scope.note && $scope.note.paragraphs) {
      _.forEach($scope.note.paragraphs, function (par) {
        angular
          .element('#' + par.id + '_paragraphColumn_main')
          .scope()
          .saveParagraph(par)
      })
      $scope.isNoteDirty = null
    }
  }

  $scope.clearAllParagraphOutput = function (noteId) {
    noteActionService.clearAllParagraphOutput(noteId)
  }

  $scope.toggleAllEditor = function () {
    if ($scope.editorToggled) {
      $scope.$broadcast('openEditor')
    } else {
      $scope.$broadcast('closeEditor')
    }
    $scope.editorToggled = !$scope.editorToggled
  }

  $scope.showAllEditor = function () {
    $scope.$broadcast('openEditor')
  }

  $scope.hideAllEditor = function () {
    $scope.$broadcast('closeEditor')
  }

  $scope.toggleAllTable = function () {
    if ($scope.tableToggled) {
      $scope.$broadcast('openTable')
    } else {
      $scope.$broadcast('closeTable')
    }
    $scope.tableToggled = !$scope.tableToggled
  }

  $scope.showAllTable = function () {
    $scope.$broadcast('openTable')
  }

  $scope.hideAllTable = function () {
    $scope.$broadcast('closeTable')
  }

  /**
   * @returns {boolean} true if one more paragraphs are running. otherwise return false.
   */
  $scope.isNoteRunning = function () {
    if (!$scope.note) { return false }

    for (let i = 0; i < $scope.note.paragraphs.length; i++) {
      if (isParagraphRunning($scope.note.paragraphs[i])) {
        return true
      }
    }

    return false
  }

  $scope.killSaveTimer = function () {
    if ($scope.saveTimer) {
      $timeout.cancel($scope.saveTimer)
      $scope.saveTimer = null
    }
  }

  $scope.startSaveTimer = function () {
    $scope.killSaveTimer()
    $scope.isNoteDirty = true
    // console.log('startSaveTimer called ' + $scope.note.id);
    $scope.saveTimer = $timeout(function () {
      $scope.saveNote()
    }, 10000)
  }

  angular.element(window).on('beforeunload', function (e) {
    $scope.killSaveTimer()
    $scope.saveNote()
  })

  $scope.setLookAndFeel = function (looknfeel) {
    $scope.note.config.looknfeel = looknfeel
    if ($scope.revisionView === true) {
      $rootScope.$broadcast('setLookAndFeel', $scope.note.config.looknfeel)
    } else {
      $scope.setConfig()
    }
  }

  /** Set cron expression for this note **/
  $scope.setCronScheduler = function (cronExpr) {
    if (cronExpr) {
      if (!$scope.note.config.cronExecutingUser) {
        $scope.note.config.cronExecutingUser = $rootScope.ticket.principal
      }
    } else {
      $scope.note.config.cronExecutingUser = ''
    }
    $scope.note.config.cron = cronExpr
    $scope.setConfig()
  }

  /** Set the username of the user to be used to execute all notes in notebook **/
  $scope.setCronExecutingUser = function (cronExecutingUser) {
    $scope.note.config.cronExecutingUser = cronExecutingUser
    $scope.setConfig()
  }

  /** Set release resource for this note **/
  $scope.setReleaseResource = function (value) {
    $scope.note.config.releaseresource = value
    $scope.setConfig()
  }

  /** Update note config **/
  $scope.setConfig = function (config) {
    if (config) {
      $scope.note.config = config
    }
    websocketMsgSrv.updateNote($scope.note.id, $scope.note.name, $scope.note.config)
  }

  /** Update the note name */
  $scope.updateNoteName = function (newName) {
    const trimmedNewName = newName.trim()
    if (trimmedNewName.length > 0 && $scope.note.name !== trimmedNewName) {
      $scope.note.name = trimmedNewName
      websocketMsgSrv.renameNote($scope.note.id, $scope.note.name)
    }
  }

  const initializeLookAndFeel = function () {
    if (!$scope.note.config.looknfeel) {
      $scope.note.config.looknfeel = 'default'
    } else {
      $scope.viewOnly = $scope.note.config.looknfeel === 'report' ? true : false
    }

    if ($scope.note.paragraphs && $scope.note.paragraphs[0]) {
      $scope.note.paragraphs[0].focus = true
    }

    $rootScope.$broadcast('setLookAndFeel', $scope.note.config.looknfeel)
  }

  let cleanParagraphExcept = function (paragraphId, note) {
    let noteCopy = {}
    noteCopy.id = note.id
    noteCopy.name = note.name
    noteCopy.config = note.config
    noteCopy.info = note.info
    noteCopy.paragraphs = []
    for (let i = 0; i < note.paragraphs.length; i++) {
      if (note.paragraphs[i].id === paragraphId) {
        noteCopy.paragraphs[0] = note.paragraphs[i]
        if (!noteCopy.paragraphs[0].config) {
          noteCopy.paragraphs[0].config = {}
        }
        noteCopy.paragraphs[0].config.editorHide = true
        noteCopy.paragraphs[0].config.tableHide = false
        break
      }
    }
    return noteCopy
  }

  let addPara = function (paragraph, index) {
    $scope.note.paragraphs.splice(index, 0, paragraph)
    $scope.note.paragraphs.map(para => {
      if (para.id === paragraph.id) {
        para.focus = true

        // we need `$timeout` since angular DOM might not be initialized
        $timeout(() => { $scope.$broadcast('focusParagraph', para.id, 0, false) })
      }
    })
  }

  let removePara = function (paragraphId) {
    let removeIdx
    _.each($scope.note.paragraphs, function (para, idx) {
      if (para.id === paragraphId) {
        removeIdx = idx
      }
    })
    return $scope.note.paragraphs.splice(removeIdx, 1)
  }

  $scope.$on('addParagraph', function (event, paragraph, index) {
    if ($scope.paragraphUrl || $scope.revisionView === true) {
      return
    }
    addPara(paragraph, index)
  })

  $scope.$on('removeParagraph', function (event, paragraphId) {
    if ($scope.paragraphUrl || $scope.revisionView === true) {
      return
    }
    removePara(paragraphId)
  })

  $scope.$on('moveParagraph', function (event, paragraphId, newIdx) {
    if ($scope.revisionView === true) {
      return
    }
    let removedPara = removePara(paragraphId)
    if (removedPara && removedPara.length === 1) {
      addPara(removedPara[0], newIdx)
    }
  })

  $scope.$on('updateNote', function (event, name, config, info) {
    /** update Note name */
    if (name !== $scope.note.name) {
      console.log('change note name to : %o', $scope.note.name)
      $scope.note.name = name
    }
    $scope.note.config = config
    $scope.note.info = info
    initializeLookAndFeel()
  })

  let getInterpreterBindings = function () {
    websocketMsgSrv.getInterpreterBindings($scope.note.id)
  }

  $scope.$on('interpreterBindings', function (event, data) {
    $scope.interpreterBindings = data.interpreterBindings
    $scope.interpreterBindingsOrig = angular.copy($scope.interpreterBindings) // to check dirty

    let selected = false
    let key
    let setting

    for (key in $scope.interpreterBindings) {
      setting = $scope.interpreterBindings[key]
      if (setting.selected) {
        selected = true
        break
      }
    }

    if (!selected) {
      // make default selection
      let selectedIntp = {}
      for (key in $scope.interpreterBindings) {
        setting = $scope.interpreterBindings[key]
        if (!selectedIntp[setting.name]) {
          setting.selected = true
          selectedIntp[setting.name] = true
        }
      }
      $scope.showSetting = true
    }
  })

  $scope.interpreterSelectionListeners = {
    accept: function (sourceItemHandleScope, destSortableScope) { return true },
    itemMoved: function (event) {},
    orderChanged: function (event) {}
  }

  $scope.openSetting = function () {
    $scope.showSetting = true
    getInterpreterBindings()
  }

  $scope.closeSetting = function () {
    if (isSettingDirty()) {
      BootstrapDialog.confirm({
        closable: true,
        title: '',
        message: 'Interpreter setting changes will be discarded.',
        callback: function (result) {
          if (result) {
            $scope.$apply(function () {
              $scope.showSetting = false
            })
          }
        }
      })
    } else {
      $scope.showSetting = false
    }
  }

  $scope.saveSetting = function () {
    let selectedSettingIds = []
    for (let no in $scope.interpreterBindings) {
      let setting = $scope.interpreterBindings[no]
      if (setting.selected) {
        selectedSettingIds.push(setting.id)
      }
    }
    websocketMsgSrv.saveInterpreterBindings($scope.note.id, selectedSettingIds)
    console.log('Interpreter bindings %o saved', selectedSettingIds)

    _.forEach($scope.note.paragraphs, function (n, key) {
      let regExp = /^\s*%/g
      if (n.text && !regExp.exec(n.text)) {
        $scope.$broadcast('saveInterpreterBindings', n.id)
      }
    })

    $scope.showSetting = false
  }

  $scope.toggleSetting = function () {
    if ($scope.showSetting) {
      $scope.closeSetting()
    } else {
      $scope.openSetting()
      $scope.closePermissions()
      angular.element('html, body').animate({ scrollTop: 0 }, 'slow')
    }
  }

  let getPermissions = function (callback) {
    $http.get(baseUrlSrv.getRestApiBase() + '/notebook/' + $scope.note.id + '/permissions')
    .success(function (data, status, headers, config) {
      $scope.permissions = data.body
      $scope.permissionsOrig = angular.copy($scope.permissions) // to check dirty

      let selectJson = {
        tokenSeparators: [',', ' '],
        ajax: {
          url: function (params) {
            if (!params.term) {
              return false
            }
            return baseUrlSrv.getRestApiBase() + '/security/userlist/' + params.term
          },
          delay: 250,
          processResults: function (data, params) {
            let results = []

            if (data.body.users.length !== 0) {
              let users = []
              for (let len = 0; len < data.body.users.length; len++) {
                users.push({
                  'id': data.body.users[len],
                  'text': data.body.users[len]
                })
              }
              results.push({
                'text': 'Users :',
                'children': users
              })
            }
            if (data.body.roles.length !== 0) {
              let roles = []
              for (let len = 0; len < data.body.roles.length; len++) {
                roles.push({
                  'id': data.body.roles[len],
                  'text': data.body.roles[len]
                })
              }
              results.push({
                'text': 'Roles :',
                'children': roles
              })
            }
            return {
              results: results,
              pagination: {
                more: false
              }
            }
          },
          cache: false
        },
        width: ' ',
        tags: true,
        minimumInputLength: 3
      }

      $scope.setIamOwner()
      angular.element('#selectOwners').select2(selectJson)
      angular.element('#selectReaders').select2(selectJson)
      angular.element('#selectWriters').select2(selectJson)
      if (callback) {
        callback()
      }
    })
    .error(function (data, status, headers, config) {
      if (status !== 0) {
        console.log('Error %o %o', status, data.message)
      }
    })
  }

  $scope.openPermissions = function () {
    $scope.showPermissions = true
    getPermissions()
  }

  $scope.closePermissions = function () {
    if (isPermissionsDirty()) {
      BootstrapDialog.confirm({
        closable: true,
        title: '',
        message: 'Changes will be discarded.',
        callback: function (result) {
          if (result) {
            $scope.$apply(function () {
              $scope.showPermissions = false
            })
          }
        }
      })
    } else {
      $scope.showPermissions = false
    }
  }

  function convertPermissionsToArray () {
    $scope.permissions.owners = angular.element('#selectOwners').val()
    $scope.permissions.readers = angular.element('#selectReaders').val()
    $scope.permissions.writers = angular.element('#selectWriters').val()
    angular.element('.permissionsForm select').find('option:not([is-select2="false"])').remove()
  }

  $scope.restartInterpreter = function(interpreter) {
    const thisConfirm = BootstrapDialog.confirm({
      closable: false,
      closeByBackdrop: false,
      closeByKeyboard: false,
      title: '',
      message: 'Do you want to restart ' + interpreter.name + ' interpreter?',
      callback: function(result) {
        if (result) {
          let payload = {
            'noteId': $scope.note.id
          }

          thisConfirm.$modalFooter.find('button').addClass('disabled')
          thisConfirm.$modalFooter.find('button:contains("OK")')
            .html('<i class="fa fa-circle-o-notch fa-spin"></i> Saving Setting')

          $http.put(baseUrlSrv.getRestApiBase() + '/interpreter/setting/restart/' + interpreter.id, payload)
            .success(function(data, status, headers, config) {
              let index = _.findIndex($scope.interpreterSettings, {'id': interpreter.id})
              $scope.interpreterSettings[index] = data.body
              thisConfirm.close()
            }).error(function (data, status, headers, config) {
              thisConfirm.close()
              console.log('Error %o %o', status, data.message)
              BootstrapDialog.show({
                title: 'Error restart interpreter.',
                message: data.message
              })
            })
          return false
        }
      }
    })
  }

  $scope.savePermissions = function () {
    if ($scope.isAnonymous || $rootScope.ticket.principal.trim().length === 0) {
      $scope.blockAnonUsers()
    }
    convertPermissionsToArray()
    if ($scope.isOwnerEmpty()) {
      BootstrapDialog.show({
        closable: false,
        title: 'Setting Owners Permissions',
        message: 'Please fill the [Owners] field. If not, it will set as current user.\n\n' +
          'Current user : [ ' + $rootScope.ticket.principal + ']',
        buttons: [
          {
            label: 'Set',
            action: function(dialog) {
              dialog.close()
              $scope.permissions.owners = [$rootScope.ticket.principal]
              $scope.setPermissions()
            }
          },
          {
            label: 'Cancel',
            action: function(dialog) {
              dialog.close()
              $scope.openPermissions()
            }
          }
        ]
      })
    } else {
      $scope.setPermissions()
    }
  }

  $scope.setPermissions = function() {
    $http.put(baseUrlSrv.getRestApiBase() + '/notebook/' + $scope.note.id + '/permissions',
      $scope.permissions, {withCredentials: true})
    .success(function (data, status, headers, config) {
      getPermissions(function () {
        console.log('Note permissions %o saved', $scope.permissions)
        BootstrapDialog.alert({
          closable: true,
          title: 'Permissions Saved Successfully',
          message: 'Owners : ' + $scope.permissions.owners + '\n\n' + 'Readers : ' +
          $scope.permissions.readers + '\n\n' + 'Writers  : ' + $scope.permissions.writers
        })
        $scope.showPermissions = false
      })
    })
    .error(function (data, status, headers, config) {
      console.log('Error %o %o', status, data.message)
      BootstrapDialog.show({
        closable: false,
        closeByBackdrop: false,
        closeByKeyboard: false,
        title: 'Insufficient privileges',
        message: data.message,
        buttons: [
          {
            label: 'Login',
            action: function (dialog) {
              dialog.close()
              angular.element('#loginModal').modal({
                show: 'true'
              })
            }
          },
          {
            label: 'Cancel',
            action: function (dialog) {
              dialog.close()
              $location.path('/')
            }
          }
        ]
      })
    })
  }

  $scope.togglePermissions = function () {
    let principal = $rootScope.ticket.principal
    $scope.isAnonymous = principal === 'anonymous' ? true : false
    if (!!principal && $scope.isAnonymous) {
      $scope.blockAnonUsers()
    } else {
      if ($scope.showPermissions) {
        $scope.closePermissions()
        angular.element('#selectOwners').select2({})
        angular.element('#selectReaders').select2({})
        angular.element('#selectWriters').select2({})
      } else {
        $scope.openPermissions()
        $scope.closeSetting()
      }
    }
  }

  $scope.setIamOwner = function () {
    if ($scope.permissions.owners.length > 0 &&
      _.indexOf($scope.permissions.owners, $rootScope.ticket.principal) < 0) {
      $scope.isOwner = false
      return false
    }
    $scope.isOwner = true
    return true
  }

  $scope.toggleNotePersonalizedMode = function () {
    let personalizedMode = $scope.note.config.personalizedMode
    if ($scope.isOwner) {
      BootstrapDialog.confirm({
        closable: true,
        title: 'Setting the result display',
        message: function (dialog) {
          let modeText = $scope.note.config.personalizedMode === 'true' ? 'collaborate' : 'personalize'
          return 'Do you want to <span class="text-info">' + modeText + '</span> your analysis?'
        },
        callback: function (result) {
          if (result) {
            if ($scope.note.config.personalizedMode === undefined) {
              $scope.note.config.personalizedMode = 'false'
            }
            $scope.note.config.personalizedMode = personalizedMode === 'true' ? 'false' : 'true'
            websocketMsgSrv.updatePersonalizedMode($scope.note.id, $scope.note.config.personalizedMode)
          }
        }
      })
    }
  }

  const isSettingDirty = function () {
    if (angular.equals($scope.interpreterBindings, $scope.interpreterBindingsOrig)) {
      return false
    } else {
      return true
    }
  }

  const isPermissionsDirty = function () {
    if (angular.equals($scope.permissions, $scope.permissionsOrig)) {
      return false
    } else {
      return true
    }
  }

  angular.element(document).click(function () {
    angular.element('.ace_autocomplete').hide()
  })

  $scope.isOwnerEmpty = function() {
    if ($scope.permissions.owners.length > 0) {
      for (let i = 0; i < $scope.permissions.owners.length; i++) {
        if ($scope.permissions.owners[i].trim().length > 0) {
          return false
        } else if (i === $scope.permissions.owners.length - 1) {
          return true
        }
      }
    } else {
      return true
    }
  }

  /*
   ** $scope.$on functions below
   */

  $scope.$on('setConnectedStatus', function (event, param) {
    if (connectedOnce && param) {
      initNotebook()
    }
    connectedOnce = true
  })

  $scope.$on('moveParagraphUp', function (event, paragraph) {
    let newIndex = -1
    for (let i = 0; i < $scope.note.paragraphs.length; i++) {
      if ($scope.note.paragraphs[i].id === paragraph.id) {
        newIndex = i - 1
        break
      }
    }
    if (newIndex < 0 || newIndex >= $scope.note.paragraphs.length) {
      return
    }
    // save dirtyText of moving paragraphs.
    let prevParagraph = $scope.note.paragraphs[newIndex]
    angular
      .element('#' + paragraph.id + '_paragraphColumn_main')
      .scope()
      .saveParagraph(paragraph)
    angular
      .element('#' + prevParagraph.id + '_paragraphColumn_main')
      .scope()
      .saveParagraph(prevParagraph)
    websocketMsgSrv.moveParagraph(paragraph.id, newIndex)
  })

  $scope.$on('moveParagraphDown', function (event, paragraph) {
    let newIndex = -1
    for (let i = 0; i < $scope.note.paragraphs.length; i++) {
      if ($scope.note.paragraphs[i].id === paragraph.id) {
        newIndex = i + 1
        break
      }
    }

    if (newIndex < 0 || newIndex >= $scope.note.paragraphs.length) {
      return
    }
    // save dirtyText of moving paragraphs.
    let nextParagraph = $scope.note.paragraphs[newIndex]
    angular
      .element('#' + paragraph.id + '_paragraphColumn_main')
      .scope()
      .saveParagraph(paragraph)
    angular
      .element('#' + nextParagraph.id + '_paragraphColumn_main')
      .scope()
      .saveParagraph(nextParagraph)
    websocketMsgSrv.moveParagraph(paragraph.id, newIndex)
  })

  $scope.$on('moveFocusToPreviousParagraph', function (event, currentParagraphId) {
    let focus = false
    for (let i = $scope.note.paragraphs.length - 1; i >= 0; i--) {
      if (focus === false) {
        if ($scope.note.paragraphs[i].id === currentParagraphId) {
          focus = true
          continue
        }
      } else {
        $scope.$broadcast('focusParagraph', $scope.note.paragraphs[i].id, -1)
        break
      }
    }
  })

  $scope.$on('moveFocusToNextParagraph', function (event, currentParagraphId) {
    let focus = false
    for (let i = 0; i < $scope.note.paragraphs.length; i++) {
      if (focus === false) {
        if ($scope.note.paragraphs[i].id === currentParagraphId) {
          focus = true
          continue
        }
      } else {
        $scope.$broadcast('focusParagraph', $scope.note.paragraphs[i].id, 0)
        break
      }
    }
  })

  $scope.$on('insertParagraph', function (event, paragraphId, position) {
    if ($scope.revisionView === true) {
      return
    }
    let newIndex = -1
    for (let i = 0; i < $scope.note.paragraphs.length; i++) {
      if ($scope.note.paragraphs[i].id === paragraphId) {
        // determine position of where to add new paragraph; default is below
        if (position === 'above') {
          newIndex = i
        } else {
          newIndex = i + 1
        }
        break
      }
    }

    if (newIndex < 0 || newIndex > $scope.note.paragraphs.length) {
      return
    }
    websocketMsgSrv.insertParagraph(newIndex)
  })

  $scope.$on('setNoteContent', function (event, note) {
    if (note === undefined) {
      $location.path('/')
    }

    $scope.note = note

    $scope.paragraphUrl = $routeParams.paragraphId
    $scope.asIframe = $routeParams.asIframe
    if ($scope.paragraphUrl) {
      $scope.note = cleanParagraphExcept($scope.paragraphUrl, $scope.note)
      $scope.$broadcast('$unBindKeyEvent', $scope.$unBindKeyEvent)
      $rootScope.$broadcast('setIframe', $scope.asIframe)
      initializeLookAndFeel()
      return
    }

    initializeLookAndFeel()

    // open interpreter binding setting when there're none selected
    getInterpreterBindings()
    getPermissions()
    let isPersonalized = $scope.note.config.personalizedMode
    isPersonalized = isPersonalized === undefined ? 'false' : isPersonalized
    $scope.note.config.personalizedMode = isPersonalized
  })

  $scope.$on('$destroy', function () {
    angular.element(window).off('beforeunload')
    $scope.killSaveTimer()
    $scope.saveNote()

    document.removeEventListener('click', $scope.focusParagraphOnClick)
    document.removeEventListener('keydown', $scope.keyboardShortcut)
  })

  $scope.$on('$unBindKeyEvent', function () {
    document.removeEventListener('click', $scope.focusParagraphOnClick)
    document.removeEventListener('keydown', $scope.keyboardShortcut)
  })

  angular.element(window).bind('resize', function () {
    const actionbarHeight = document.getElementById('actionbar').lastElementChild.clientHeight
    angular.element(document.getElementById('content')).css('padding-top', actionbarHeight - 20)
  })
}
