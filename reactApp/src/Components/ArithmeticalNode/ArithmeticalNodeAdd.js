import React, { Component } from 'react';
import { connect } from "react-redux";

class ArithmeticalNodeAdd extends Component {


    addTo() {
        let { id_input, expr_input, integrable_input } = this;

        let arithm_node = {
            node_id: id_input.value,
            node_type: 'arithmetical',
            parameters: {
                arithm_expr: expr_input.value,
                arithm_integrable: integrable_input.checked
            }
        };

        let { dispatch } = this.props;

        dispatch('ADD_NODE', arithm_node);

    }


    render() {

        return (
            <form>
                <p>id</p>
                <input className='text-field' type='text' ref={inp => this.id_input = inp}/>
                <p>expr</p>
                <input className='text-field' type='text' ref={inp => this.expr_input = inp}/>
                <p>integrable <input type='checkbox' ref={inp => this.integrable_input = inp}/></p>

                <button type='button' onClick={this.addTo.bind(this)}>Добавить</button>
            </form>
        );
    }
}

export default connect(
    state => ({ store: state }),
    dispatch => ({ dispatch: (type, payload) => dispatch({type, payload}) })
)(ArithmeticalNodeAdd);
