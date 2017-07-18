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

import { ParagraphStatus, } from '../../notebook/paragraph/paragraph.status'
import { getJobColorByStatus, getJobIconByStatus } from '../job-status'

import './job.css'

// import jobTemplate from './job.html'
const jobTemplate = `
<div class="job" >
  <div>
    <!-- job control: start -->
    <div id="{{$ctrl.getNoteId()}}_control" class="control">
      <span ng-bind="$ctrl.lastExecuteTime()"></span>
      <span>
        <span ng-bind="$ctrl.isRunning() ? 'RUNNING' : 'READY'"></span>
      </span>

      <span ng-if="$ctrl.isRunning()" ng-bind="$ctrl.getProgress()"></span>

      <span
        class="job-control-btn" tooltip-placement="left"
        uib-tooltip-html="!$ctrl.isRunning() ? 'Start All Paragraphs' : 'Stop All Paragraphs'"
        ng-click="!$ctrl.isRunning() ? $ctrl.runJob() : $ctrl.stopJob()"
        ng-class="!$ctrl.isRunning() ? 'icon-control-play' : 'icon-control-pause'">
      </span>
    </div>
    <!-- job control: end -->

    <span class="job-types">
      <i ng-class="$ctrl.getJobTypeIcon()"></i>
    </span>
    <a style="text-decoration: none !important;" ng-href="#/notebook/{{$ctrl.getNoteId()}}">
      <span ng-bind="$ctrl.getNoteName() + ' - '"></span>
      <span ng-style="$ctrl.getInterpreterNameStyle()"
            ng-bind="$ctrl.getInterpreterName()">
      </span>
    </a>
    <!-- job progress bar: start -->
    <div id="{{$ctrl.getNoteId()}}_runControl" class="runControl">
      <div id="{{$ctrl.getNoteId()}}_progress" class="progress" ng-if="$ctrl.isRunning() === true">
        <div class="progress-bar" role="progressbar"
             ng-style="$ctrl.showPercentProgressBar() ? { 'width': $ctrl.getProgress() } : { 'width': '100%' }"
             ng-class="$ctrl.showPercentProgressBar() ? '' : 'progress-bar-striped active'">
        </div>
      </div>
    </div>
    <!-- job progress bar: end -->
  </div>

  <div>
    <span ng-repeat="paragraph in $ctrl.getParagraphs()">
      <a style="text-decoration: none !important;"
         ng-href="#/notebook/{{$ctrl.getNoteId()}}?paragraph={{paragraph.id}}">
        <i ng-style="{'color': $ctrl.getJobColorByStatus(paragraph.status)}"
           ng-class="$ctrl.getJobIconByStatus(paragraph.status)"
           tooltip-placement="top-left"
           uib-tooltip="{{paragraph.name}} is {{paragraph.status}}">
        </i>
      </a>
    </span>
  </div>
</div>
`

class JobController {
  constructor($http, baseUrlSrv) {
    'ngInject'
    this.$http = $http
    this.baseUrlSrv = baseUrlSrv
  }

  isRunning() {
    return this.note.isRunningJob
  }

  getParagraphs() {
    return this.note.paragraphs
  }

  getNoteId() {
    return this.note.noteId
  }

  getNoteName() {
    return this.note.noteName
  }

  runJob() {
    const noteId = this.getNoteId()
    const apiURL = this.baseUrlSrv.getRestApiBase() + '/notebook/job/' + noteId
    BootstrapDialog.confirm({
      closable: true,
      title: 'Job Dialog',
      message: 'Run all paragraphs?',
      callback: result => {
        console.log(this.$http)
        if (result) {
          this.$http({
            method: 'POST',
            url: apiURL,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
          }).then(response => {
            // success
          }, response => {
            let errorMessage
            // eslint-disable-next-line no-extra-boolean-cast
            if (!!response.data.message) { errorMessage = response.data.message }
            this.showErrorDialog('Execution Failure', errorMessage)
          })
        }
      }
    })
  }

  stopJob() {
    const noteId = this.getNoteId()
    const apiURL = this.baseUrlSrv.getRestApiBase() + '/notebook/job/' + noteId

    BootstrapDialog.confirm({
      closable: true,
      title: 'Job Dialog',
      message: 'Stop all paragraphs?',
      callback: result => {
        if (result) {
          this.$http({
            method: 'DELETE',
            url: apiURL,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
          }).then(response => {
            // success
          }, response => {
            let errorMessage
            // eslint-disable-next-line no-extra-boolean-cast
            if (!!response.data.message) { errorMessage = response.data.message }
            this.showErrorDialog('Stop Failure', errorMessage)
          })
        }
      }
    })
  }

  showErrorDialog(title, errorMessage) {
    if (!errorMessage) { errorMessage = 'SERVER ERROR' }
    BootstrapDialog.alert({
      closable: true,
      title: title,
      message: errorMessage
    })
  }

  lastExecuteTime() {
    const timestamp = this.note.unixTimeLastRun
    return moment.unix(timestamp / 1000).fromNow()
  }

  getInterpreterName() {
    return typeof this.note.interpreter === 'undefined'
      ? 'interpreter is not set' : this.note.interpreter
  }

  getInterpreterNameStyle() {
    return typeof this.note.interpreter === 'undefined'
      ? { color: 'gray' } : { color: 'black' }
  }

  getJobTypeIcon() {
    const noteType = this.note.noteType
    if (noteType === 'normal') {
      return 'icon-doc'
    } else if (noteType === 'cron') {
      return 'icon-clock'
    } else {
      return 'icon-question'
    }
  }

  getJobColorByStatus(status) {
    return getJobColorByStatus(status)
  }

  getJobIconByStatus(status) {
    return getJobIconByStatus(status)
  }

  getProgress() {
    const paragraphs = this.getParagraphs()
    let paragraphStatuses = paragraphs.map(p => p.status)
    let runningOrFinishedParagraphs = paragraphStatuses.filter(status => {
      return status === ParagraphStatus.RUNNING || status === ParagraphStatus.FINISHED
    })

    let totalCount = paragraphStatuses.length
    let runningCount = runningOrFinishedParagraphs.length
    let result = Math.ceil(runningCount / totalCount * 100)
    result = isNaN(result) ? 0 : result

    return `${result}%`
  }

  showPercentProgressBar() {
    return this.getProgress() > 0 && this.getProgress() < 100
  }
}

export const JobComponent = {
  bindings: {
    note: '<',
  },
  template: jobTemplate,
  controller: JobController,
}

export const JobModule = angular
  .module('zeppelinWebApp')
  .component('job', JobComponent)
  .name
