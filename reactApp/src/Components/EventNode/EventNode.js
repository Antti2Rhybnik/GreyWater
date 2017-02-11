import React, { Component } from 'react';


class EventNode extends Component {

    render() {

        let { node_id, parameters: { event_importance, event_msg } } = this.props;

        return (
            <div>
                <div>id: {node_id+''}</div>
                <div>level: {event_importance+''}</div>
                <div>msg: {event_msg+''}</div>
            </div>
        );
    }

}


export default EventNode;