const AjaxRequest = {
    GET: (uri, callback, handleError) => {
        fetch(uri, {
            method: 'GET'
        }).then(response => {
            callback(response)
        }).catch(error => {
            error.text().then(text => handleError(text));
        });
    },
    POST: (uri, body, callback, handleError) => {
        fetch(uri, {
            headers: {
                'Content-type': 'application/json; charset=UTF-8'
            },
            method: 'POST',
            body: JSON.stringify(body)
        }).then(response => {
            callback(response)
        }).catch(error => {
            error.text().then(text => handleError(text))
        });
    },
    PUT: (uri, body, callback, handleError) => {
        fetch(uri, {
            headers: {
                'Content-type': 'application/json; charset=UTF-8'
            },
            method: 'PUT',
            body: JSON.stringify(body)
        }).then(response => {
            callback(response)
        }).catch(error => {
            error.text().then(text => handleError(text))
        });
    },
    DELETE: (uri, callback, handleError) => {
        fetch(uri, {
            method: 'DELETE'
        }).then(response => {
            callback(response)
        }).catch(error => {
            error.text().then(text => handleError(text))
        });
    },
}