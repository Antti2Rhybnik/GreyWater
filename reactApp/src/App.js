import React, {Component} from 'react';
import {connect} from 'react-redux';

// import logo from './logo.svg';
import PlayImg from './img/black-play-24.png';
import PauseImg from './img/pause-24.png';
import './App.css';

import ArithmeticalNodeAdd from './Components/ArithmeticalNode/ArithmeticalNodeAdd';
import EventNodeAdd from './Components/EventNode/EventNodeAdd';
import SensorNodeAdd from './Components/SensorNode/SensorNodeAdd';
import LogicalNodeAdd from './Components/LogicalNode/LogicalNodeAdd';

import NodesBlock from './Components/NodesBlock';
import ArithmeticalNode from "./Components/ArithmeticalNode/ArithmeticalNode";
import SensorNode from "./Components/SensorNode/SensorNode";
import LogicalNode from "./Components/LogicalNode/LogicalNode";
import EventNode from "./Components/EventNode/EventNode";

import {requireNodes, saveNodes, startServer, stopServer} from './async/serverAPI';

import Menu from './Menu';

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            showed: 'Sensors',
            running: false
        };
    }


    addNode() {
        let {dispatch} = this.props;

        console.log('addNode', this.nodeInp.value);
        let node = JSON.parse(this.nodeInp.value);
        dispatch('ADD_NODE', node);
        this.nodeInp.value = '';
    }

    getNodes() {
        let {dispatch} = this.props;

        console.log('getNodes');
        requireNodes()
            .then(nodes => dispatch('NODES_LOADED_SUCCESS', nodes))
            .catch(err => console.log(err));
    }

    saveNodes() {
        let {nodesData : nodes} = this.props.store;

        console.log('saveNodes');
        saveNodes(nodes)
            .then(resp => console.log(resp))
            .catch(err => console.log(err));
    }

    startServer() {
        startServer()
            .then(() => {
                console.log("started");
                this.setState({ running: true })
            })
            .catch(err => {
                console.log(err);
            });
    }

    stopServer() {
        stopServer()
            .then(() => {
                console.log('stopped');
                this.setState({ running: false });
            })
            .catch(err => {
                console.log(err);
            });
    }

    test(item) {
        this.setState({showed: item})
    }


    render() {

        let {nodesData : nodes} = this.props.store;
        let {showed, running} = this.state;

        let sensorNodes = nodes.filter(node => node.node_type === 'sensor');
        let arithmeticalNodes = nodes.filter(node => node.node_type === 'arithmetical');
        let logicalNodes = nodes.filter(node => node.node_type === 'logical');
        let eventNodes = nodes.filter(node => node.node_type === 'event');

        let nodeBlock = null;
        if (showed === 'Sensors')
            nodeBlock = <NodesBlock Node={SensorNode} NodeAdd={SensorNodeAdd} nodes={sensorNodes}/>;
        else if (showed === 'Arithmetic')
            nodeBlock = <NodesBlock Node={ArithmeticalNode} NodeAdd={ArithmeticalNodeAdd} nodes={arithmeticalNodes}/>;
        else if (showed === 'Logic')
            nodeBlock = <NodesBlock Node={LogicalNode} NodeAdd={LogicalNodeAdd} nodes={logicalNodes}/>;
        else if (showed === 'Events')
            nodeBlock = <NodesBlock Node={EventNode} NodeAdd={EventNodeAdd} nodes={eventNodes}/>;

        let playBtn = null;
        if (running)
            playBtn = <button className='osx-button img' onClick={this.stopServer.bind(this)}>
                <img src={PauseImg}/>
            </button>;
        else
            playBtn =<button className='osx-button img' onClick={this.startServer.bind(this)}>
                <img src={PlayImg}/>
            </button>;

        return (
            <div className="App">
                <header className="main-header">
                        <div className='header-item'>
                            <h3>GreyWater</h3>
                        </div>
                        <div className='header-item'>
                            <Menu items={['Sensors', 'Arithmetic', 'Logic', 'Events']} onClick={this.test.bind(this)}/>
                        </div>
                        <div className='header-item'>
                            {playBtn}
                        </div>
                        <div className='header-item'>
                            <button className='osx-button' onClick={this.getNodes.bind(this)}>Require nodes</button>
                        </div>
                        <div className='header-item'>
                            <button className='osx-button' onClick={this.saveNodes.bind(this)}>Save nodes</button>
                        </div>
                </header>

                <div className="App-cnt">{nodeBlock}</div>

            </div>
        );
    }
}

export default connect(
    state => ({store: state}),
    dispatch => ({dispatch: (type, payload) => dispatch({type, payload})})
)(App);
