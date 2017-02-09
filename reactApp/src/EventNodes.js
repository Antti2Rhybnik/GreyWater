import React, { Component } from 'react';

import EventNode from './EventNode';

class EventNodes extends Component {

    render() {

        let { nodes } = this.props;

        let renders = nodes.map((node, idx) => {

            let { node_id, parameters } = node;

            return (
                <div key={idx}>
                    <EventNode node_id={node_id} parameters={parameters} />
                </div>
            );
        });

        return (
            <div>
                <h3>EventNodes</h3>
                <div>{renders}</div>
            </div>
        );
    }

}


export default EventNodes;