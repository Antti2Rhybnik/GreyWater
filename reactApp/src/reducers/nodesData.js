let initState = [
    { node_id: '_1', node_type: 'sensor', parameters : { sensor_type: 'Water 1.0', sensor_unit: "m/s"} },
    { node_id: '_2', node_type: 'arithmetical', parameters : { arithm_expr: '_1 + 2', arithm_integrable: false } },
    { node_id: '_3', node_type: 'arithmetical', parameters : { arithm_expr: '_1 + 77', arithm_integrable: false } }
];


export default function nodesData(nodes = initState, action) {

    if (action.type === 'ADD_NODE') {

        let { payload: node } = action;
        return [...nodes, node];
    }

    if (action.type === 'DELETE_NODE') {

        let { payload: node_id } = action;
        return nodes.filter(node => node.node_id !== node_id);
    }

    if (action.type === 'CHANGE_NODE') {

        let { payload: { id, prop, val } } = action;
        return nodes.map(node => (node.node_id === id) ? {...node, [prop]: val} : {...node});

    }

    if (action.type === 'NODES_LOADED_SUCCESS') {

        let { payload : loadedNodes } = action;

        return loadedNodes;
    }

    return nodes;
}