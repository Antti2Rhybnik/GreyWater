import React, { Component } from 'react';
import {connect} from "react-redux";



class EventNodeAdd extends Component {


    addTo() {
        let { id_input, msg_input, importance_input } = this;

        let event_node = {
            node_id: id_input.value,
            node_type: 'event',
            parameters: {
                event_msg: msg_input.value,
                event_importance: importance_input.value
            }
        };

        let { dispatch } = this.props;

        dispatch('ADD_NODE', event_node);

    }


    render() {


        return (
            <div>
                <p>id</p>
                <input className='text-field' type="text" ref={inp => this.id_input = inp}/>
                <p>message</p>
                <input className='text-field' type="text" ref={inp => this.msg_input = inp}/>
                <p>importance</p>
                <input className='text-field' type="text" ref={inp => this.importance_input = inp}/>
                <button type='button' onClick={this.addTo.bind(this)}>Добавить</button>
            </div>
        )
    }
}

export default connect(
    state => ({ store: state }),
    dispatch => ({ dispatch: (type, payload) => dispatch({type, payload}) })
)(EventNodeAdd);