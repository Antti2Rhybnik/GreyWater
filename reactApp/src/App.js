import React, { Component } from 'react';
import { connect } from 'react-redux';

// import logo from './logo.svg';
import './App.css';

import SensorNodes from './SensorNodes';
import ArithmeticalNodes from './ArithmeticalNodes';
import LogicalNodes from './LogicalNodes';
import EventNodes from './EventNodes';

import { requireNodes, saveNodes } from './async/serverAPI';

class App extends Component {

    addNode() {
        console.log('addNode', this.nodeInp.value);
        let node = JSON.parse(this.nodeInp.value);
        this.props.dispatch({ type: 'ADD_NODE', payload: node });
        this.nodeInp.value = '';
    }

    getNodes() {
        console.log('getNodes');
        requireNodes()
            .then(nodes => this.props.dispatch({ type: 'NODES_LOADED_SUCCESS', payload: nodes }))
            .catch(err => console.log(err));
    }

    saveNodes() {

        let { nodesData : nodes } = this.props.store;

        console.log('saveNodes');
        saveNodes(nodes)
            .then(resp => console.log(resp))
            .catch(err=> console.log(err));

    }

    render() {

        let { nodesData : nodes } = this.props.store;

        let sensorNodes = nodes.filter(node => node.node_type === 'sensor');
        let arithmeticalNodes = nodes.filter(node => node.node_type === 'arithmetical');
        let logicalNodes = nodes.filter(node => node.node_type === 'logical');
        let eventNodes = nodes.filter(node => node.node_type === 'event');

        return (
            <div className="App">
                <input type="text" ref={inp => this.nodeInp = inp}/>
                <button onClick={this.addNode.bind(this)}>Add node</button>
                <button onClick={this.getNodes.bind(this)}>Require nodes</button>
                <button onClick={this.saveNodes.bind(this)}>Save nodes</button>
                <div className="App-cnt">
                    <SensorNodes nodes={sensorNodes}/>
                    <ArithmeticalNodes nodes={arithmeticalNodes}/>
                    <LogicalNodes nodes={logicalNodes}/>
                    <EventNodes nodes={eventNodes}/>
                </div>
            </div>
        );
    }
}

export default connect(
    state => ({ store: state }),
    dispatch => ({ dispatch: dispatch })
)(App);
