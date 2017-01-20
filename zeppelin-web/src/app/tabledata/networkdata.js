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

import TableData from './tabledata';
import {DatasetType} from './dataset';

/**
 * Create network data object from paragraph graph type result
 */
export default class NetworkData extends TableData {
  constructor(graph) {
	super();
    this.graph = graph || {};
    if (this.graph.nodes) {
      this.loadParagraphResult({msg: JSON.stringify(graph), type: DatasetType.NETWORK});
    }
  };

  loadParagraphResult(paragraphResult) {
    if (!paragraphResult || paragraphResult.type !== DatasetType.NETWORK) {
      console.log('Can not load paragraph result');
      return;
    }

    this.graph = JSON.parse(paragraphResult.msg.trim() || '{}');

    if (!this.graph.nodes) {
      console.log('Graph result is empty');
      return;
    }

    this.setNodesDefaults();
    this.setEdgesDefaults();

    this.networkNodes = angular.equals({}, this.graph.labels || {}) ?
            null : {count: this.graph.nodes.length, labels: this.graph.labels};
    this.networkRelationships = angular.equals([], this.graph.types || []) ?
            null : {count: this.graph.edges.length, types: this.graph.types};

    var columnNames = [];
    var rows = [];
    var comment = '';
    var entities = this.graph.nodes.concat(this.graph.edges);
    var baseColumnNames = [{name: 'id', index: 0, aggr: 'sum'},
                       {name: 'label', index: 1, aggr: 'sum'}];
    var internalFieldsToJump = ['count', 'type', 'size',
                                'data', 'x', 'y', 'color',
                                'labels'];
    var baseCols = _.map(baseColumnNames, function(col) { return col.name; });
    var keys = _.map(entities, function(elem) { return Object.keys(elem.data || {}); });
    keys = _.flatten(keys);
    keys = _.uniq(keys).filter(function(key) {
      return baseCols.indexOf(key) === -1;
    });
    var columnNames = baseColumnNames.concat(_.map(keys, function(elem, i) {
      return {name: elem, index: i + baseColumnNames.length, aggr: 'sum'};
    }));
    for (var i = 0; i < entities.length; i++) {
      var entity = entities[i];
      var col = [];
      var col2 = [];
      entity.data = entity.data || {};
      for (var j = 0; j < columnNames.length; j++) {
        var name = columnNames[j].name;
        var value = name in entity && internalFieldsToJump.indexOf(name) === -1 ?
            entity[name] : entity.data[name];
        var parsedValue = value === null || value === undefined ? '' : value;
        col.push(parsedValue);
        col2.push({key: name, value: parsedValue});
      }
      rows.push(col);
    }

    this.comment = comment;
    this.columns = columnNames;
    this.rows = rows;
  };

  setNodesDefaults() {
    this.graph.nodes
      .forEach(function(node) {
        node.x = node.x || Math.random();
        node.y = node.y || Math.random();
        node.size = node.size || 10;
        node.weight = node.weight || 1;
      });
  };

  setEdgesDefaults() {
    let nodes = this.graph.nodes; 
    this.graph.edges
      .forEach(function(edge) {
        edge.type = edge.type || 'arrow';
        edge.color = edge.color || '#D3D3D3';
        edge.count = edge.count || 1;
        if (typeof +edge.source === 'number') {
          edge.source = nodes.filter((node) => edge.source === node.id)[0] || null;
        }
        if (typeof +edge.target === 'number') {
          edge.target = nodes.filter((node) => edge.target === node.id)[0] || null;
        }
      });
  };

  getNetworkProperties() {
    var baseCols = ['id', 'label'];
    var properties = {};
    this.graph.nodes.forEach(function(node) {
      var hasLabel = 'label' in node && node.label !== '';
      if (!hasLabel) {
        return;
      }
      var label = node.label;
      var hasKey = hasLabel && label in properties;
      var keys = _.uniq(Object.keys(node.data || {})
              .concat(hasKey ? properties[label].keys : baseCols));
      if (!hasKey) {
        properties[label] = {selected: 'label'};
      }
      properties[label].keys = keys;
    });
    return properties;
  };
}