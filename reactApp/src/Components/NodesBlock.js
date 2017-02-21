import React, { Component } from 'react';
import { connect } from "react-redux";

import './NodesBlock.css';

class NodesBlock extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selected:  -1
        };
    }


    deleteNode(node_id) {
        console.log(node_id);
        let { dispatch } = this.props;
        dispatch('DELETE_NODE', node_id);
    }

    changeSelect(idx, node) {
        console.log(idx, this.props);
        this.setState({selected: idx});
    }

    itemClass(idx) {
        let css = 'NodesBlock-item ';
        if (idx === this.state.selected) css+= 'selected ';
        return css;
    }

    render() {

        let { nodes, Node, NodeAdd, title } = this.props;

        let renders = nodes.map((node, idx) =>
            <div className={this.itemClass(idx)} onClick={() => this.changeSelect(idx, node)} key={idx}>
                <Node node_id={node.node_id} parameters={node.parameters} />
                <button className='NodesBlock-delete' type='button' onClick={() => { this.deleteNode(node.node_id); }}/>
            </div>
        );

        return (
            <div className='NodesBlock'>
                <div className='NodesBlock-list'>{renders}</div>
                <NodeAdd/>
            </ div>
        );
    }

}


export default connect(
    state => ({ store: state }),
    dispatch => ({ dispatch: (type, payload) => dispatch({type, payload}) })
)(NodesBlock);
