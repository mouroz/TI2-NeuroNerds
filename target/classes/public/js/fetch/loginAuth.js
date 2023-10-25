import { ApiError, JsonError } from '/js/class/fetchErrors.js';

//NON SECURE SIMPLE AUTH FOR NOW. HEADER WILL BE IGNORED
const defaultJson = {
    header: {
        alg: 'HS256', //HMAC SHA256
        typ: 'JWT' //type of toke JWT
    },
    payload: {
        sub: 'username', //unique identifier, for now username 
        name: 'name',
        trilha: 'adhd'
        //iat: 124252354 //-> represents the time the token was issued to see if it expired
    }
    //signature: 'XXXXXXXXXXXXXXXXXXXXXX' //uses alg to encode header, payload and secret key
}
const localStorageName = 'userData';
const nextPageHtml = '/outras/telainicial.html';


//STATIC GLOBAL ELEMENTS
const form = document.getElementById('loginForm');
const usernameElement = document.getElementById('username');
const passwordElement = document.getElementById('password');

form.addEventListener('submit', (e) => {
    e.preventDefault(); // Prevent default form submission

    const existingData = localStorage.getItem(localStorageName);
    if (existingData != null){
        alert("login through logged in acc")
        window.location.href = nextPageHtml;
        return;
    }

    const usernameInput = usernameElement.value;
    const passwordInput = passwordElement.value;
    if (usernameInput.trim() !== '' || passwordInput.trim() !== ''){
        sendAuth(usernameInput, passwordInput);
    } 
})

function sendAuth(usernameInput, passwordInput) {
    //to keep consistency we will send a JSON instead of FORM DATA, 
    //although the code for formData is still here for use
    /*const serverRequestData = new FormData(form);*/

    //for security purposes it might be better do encrypt the data before sending
    const serverRequestData = {
        username: usernameInput,
        password: passwordInput
    };
    
    console.log(serverRequestData);
    //authentification will be done with a 'POST' and auth token.
    //a lot of the typical JWT is not being used for now so its 100% non secure
    //and serves only as prototype
    fetch('/auth', {
        method: "POST", // You can use GET or POST, depending on your server's implementation.
        body: JSON.stringify(serverRequestData),
        headers: {
            "Content-Type": "application/json"
            //for form body there is no need for Content-Type header
        }
    })
        .then(response => {
            if (response.status == 401) { //expected unauthorized response
                alert("Incorrect user or password");
                return;
            } else if (!response.ok) { //unexpected error responses, incluiding couldnt reach server
                throw new ApiError('API request failed with status ' + response.status);
            }
            return response.json();
        })
        .then(json => {
			console.log (JSON.stringify(json, null, 2));
            if (!('payload' in json)) throw new JsonError('Failure in atribute (payload) on Auth JSON');

            const payload = json.payload;
            if (!('username' in payload)) throw new JsonError('Failure in atribute (username) on Auth JSON');
            if (!('name' in payload)) throw new JsonError('Failure in atribute (password) on Auth JSON');
            updateLocalStorage(defaultJson);
        })
        .catch(error => {
            console.error('loginAuth.js error: ', error + '\n' + 'using test user values for now');
            if (error instanceof ApiError){
                if (defaultJson) { //reserved for if function starts receiving default as param
                    alert('couldnt reach server. Using test user');
                    updateLocalStorage(defaultJson);
                }
            }
        })
}

function updateLocalStorage(json){
    localStorage.setItem(localStorageName, JSON.stringify(json));
    window.location.href = nextPageHtml;
}

