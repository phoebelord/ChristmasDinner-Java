import {API_BASE_URL, ACCESS_TOKEN} from "../constants";

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });

    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN));
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response => response.json().then(json => {
            if (!response.ok) {
                return Promise.reject(json);
            }
            return json;
        }));
};

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: "POST",
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: "POST",
        body: JSON.stringify(signupRequest)
    });
}

export function checkEmailAvailability(email) {
    return request({
        url: API_BASE_URL + "/user/checkEmailAvailability?email=" + email,
        method: 'GET'
    });
}

export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    })
}

export function createConfig(configData) {
    return request({
        url: API_BASE_URL + "/config/create",
        method: 'POST',
        body: JSON.stringify(configData)
    });
}

export function getConfig(configId) {
    return request({
        url: API_BASE_URL + '/config/' + configId,
        method: 'GET'
    });
}

export function getMyConfigs() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/config/all",
        method: 'GET'
    });
}

export function getSolution(configId) {
    return request({
        url: API_BASE_URL + "/solution/" + configId,
        method: 'GET'
    })
}