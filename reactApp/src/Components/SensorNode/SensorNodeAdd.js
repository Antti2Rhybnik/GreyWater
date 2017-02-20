import React, { Component } from 'react';
import { connect } from 'react-redux';


class SensorNodeAdd extends Component {


    addTo() {
        let { id_input, sensor_name_input, sensor_unit_input } = this;

        let sensor_node = {
            node_id: id_input.value,
            node_type: 'sensor',
            parameters: {
                sensor_type: sensor_name_input.value,
                sensor_unit: sensor_unit_input.value
            }
        };

        let { dispatch } = this.props;

        dispatch('ADD_NODE', sensor_node);

    }

    render() {

        return (
            <div>
                <p>id</p>
                <input className='text-field' type="text" ref={inp => this.id_input = inp}/>
                <p>sensor_name</p>
                <input className='text-field' type="text" ref={inp => this.sensor_name_input = inp}/>
                <p>sensor_unit</p>
                <input className='text-field' type="text" ref={inp => this.sensor_unit_input = inp}/>
                <button type='button' onClick={this.addTo.bind(this)}>Добавить</button>
            </div>
        )
    }
}

export default connect(
    state => ({ store: state }),
    dispatch => ({ dispatch: (type, payload) => dispatch({type, payload}) })
)(SensorNodeAdd);