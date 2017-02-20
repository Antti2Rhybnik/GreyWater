// let mock = [{ node_id: '_3', node_type: 'arithmetical', parameters : { arithm_expr: '_1 + 77', integrable: false } }];

const requireNodes = () => new Promise((resolve, reject) => {
    const METHOD = 'GET';
    const URL = '/GreyWater/rest/api/getNodes';
    const ASYNC = true;
    const DONE = 4;
    const SUCCESS = 200;

    console.log("requireNodes");

    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {

        let { readyState: state, responseText: body, status: code } = xhr;

        if (state === DONE) {

            if (code === SUCCESS) {
                try {
                   let nodes = JSON.parse(body);
                   console.log(nodes);
                   resolve(nodes);
                } catch(e) {
                    reject(new Error(`Incorrect format JSON`));
                }

            } else {
                reject(new Error(`Failed HTTP request (${code})`));
            }
        }

    };

    xhr.onerror = reject;

    xhr.open(METHOD, URL, ASYNC);
    xhr.send();

});


const saveNodes = (nodes) => new Promise((resolve, reject) => {
    const METHOD = 'POST';
    const URL = '/GreyWater/rest/api/saveNodes';
    const ASYNC = true;
    const DONE = 4;
    const SUCCESS = 200;

    console.log("saveNodes");

    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {

        let { readyState: state, status: code } = xhr;

        if (state === DONE) {

            if (code === SUCCESS) {
                resolve('ok');
            } else {
                reject(new Error(`Failed HTTP request (${code})`));
            }
        }

    };

    xhr.onerror = reject;

    xhr.open(METHOD, URL, ASYNC);
    xhr.setRequestHeader('Content-Type', 'application/json');


    let body = JSON.stringify(nodes);
    xhr.send(body);

});


const startServer = () =>  new Promise((resolve, reject) => {
    const METHOD = 'GET';
    const URL = '/GreyWater/rest/api/start';
    const ASYNC = true;
    const DONE = 4;
    const SUCCESS = 200;

    console.log("startServer");

    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {

        let { readyState: state, status: code } = xhr;

        if (state === DONE) {

            if (code === SUCCESS) {
                resolve();
            } else {
                reject(new Error(`Failed HTTP request (${code})`));
            }
        }

    };

    xhr.onerror = reject;

    xhr.open(METHOD, URL, ASYNC);
    xhr.send();

});

const stopServer = () =>  new Promise((resolve, reject) => {
    const METHOD = 'GET';
    const URL = '/GreyWater/rest/api/stop';
    const ASYNC = true;
    const DONE = 4;
    const SUCCESS = 200;

    console.log("stopServer");

    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {

        let { readyState: state, status: code } = xhr;

        if (state === DONE) {

            if (code === SUCCESS) {
                resolve();
            } else {
                reject(new Error(`Failed HTTP request (${code})`));
            }
        }

    };

    xhr.onerror = reject;

    xhr.open(METHOD, URL, ASYNC);
    xhr.send();

});



export { requireNodes, saveNodes, startServer, stopServer };