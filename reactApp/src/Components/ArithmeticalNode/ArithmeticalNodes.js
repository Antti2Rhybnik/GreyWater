import React, { Component } from 'react';

import ArithmeticalNode from './ArithmeticalNode';
import { connect } from "react-redux";

class ArithmeticalNodes extends Component {

    constructor(props) {
        super(props);
        this.forDelete = [];
    }


    deleteNode(node_id) {
        console.log(node_id);
        let { dispatch } = this.props;
        dispatch('DELETE_NODE', node_id);
    }


    render() {

        let { nodes } = this.props;

        let renders = nodes.map((node, idx) =>
            <div key={idx}>
                <ArithmeticalNode node_id={node.node_id} parameters={node.parameters} />
                <button type='button' onClick={() => { this.deleteNode(node.node_id); }}>Удалить</button>
            </div>
        );

        return (
            <div>
                <h3>Arithmetical Nodes</h3>
                <div>{renders}</div>
            </div>
        );
    }

}


export default connect(
    state => ({ store: state }),
    dispatch => ({ dispatch: (type, payload) => dispatch({type, payload}) })
)(ArithmeticalNodes);
