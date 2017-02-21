import React, { Component } from 'react';


class ArithmeticalNode extends Component {

    render() {

        let { node_id, parameters: { arithm_expr, arithm_integrable } } = this.props;

        return (
            <div>
                <div><b>{node_id+''}:</b> {arithm_expr+''}, {arithm_integrable+''}</div>
            </div>
        );
    }

}


export default ArithmeticalNode;