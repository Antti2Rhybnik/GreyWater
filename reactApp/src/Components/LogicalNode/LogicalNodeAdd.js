import React, { Component } from 'react';
import {connect} from "react-redux";


class LogicalNodeAdd extends Component {


    addTo() {
        let { id_input, expr_input } = this;

        let logic_node = {
            node_id: id_input.value,
            node_type: 'logical',
            parameters: {
                logic_expr: expr_input.value
            }
        };

        let { dispatch } = this.props;

        dispatch('ADD_NODE', logic_node);

    }

    render() {


        return (
            <div>
                <p>id</p>
                <input className='text-field' type="text" ref={inp => this.id_input = inp}/>
                <p>expr</p>
                <input className='text-field' type="text" ref={inp => this.expr_input = inp}/>
                <button type='button' onClick={this.addTo.bind(this)}>Добавить</button>
            </div>
        )
    }
}

export default connect(
    state => ({ store: state }),
    dispatch => ({ dispatch: (type, payload) => dispatch({type, payload}) })
)(LogicalNodeAdd);