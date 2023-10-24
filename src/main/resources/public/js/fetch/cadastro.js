import { ApiError, JsonError } from '/js/class/fetchErrors.js';

const sessionStorageName = 'userData';
const nextPageHtml = '/outras/telainicial.html';


//STATIC GLOBAL ELEMENTS
const form = document.getElementById('loginForm');
const usernameElement = document.getElementById('username');
const passwordElement = document.getElementById('password');

form.addEventListener('submit', (e) => {
    e.preventDefault(); // Prevent default form submission

    const existingData = sessionStorage.getItem(sessionStorageName);
    if (existingData != null){
        alert("login through logged in acc")
        window.location.href = nextPageHtml;
        return;
    }

    const usernameInput = usernameElement.value;
    const passwordInput = passwordElement.value;
    console.log(usernameInput.trim());
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
            console.log(response.status);
            if (!response.ok) throw new ApiError('API request failed with status ' + response.status);
            if (response.status == 401) { //unauthorized
                alert("Incorrect user or password");
                return;
            } 
            return response.json();
        })
        .then(json => {
			console.log (JSON.stringify(json, null, 2));
            if (!('payload' in json)) throw new JsonError('Failure in atribute (payload) on Auth JSON');

            const payload = json.payload;
            if (!('username' in payload)) throw new JsonError('Failure in atribute (username) on Auth JSON');
            if (!('name' in payload)) throw new JsonError('Failure in atribute (password) on Auth JSON');
            updateSessionStorage(defaultJson);
        })
        .catch(error => {
            console.error('loginAuth.js error: ', error + '\n' + 'using test user values for now');
            if (error instanceof ApiError){
                if (defaultJson) { //reserved for if function starts receiving default as param
                    alert('couldnt reach server. Using test user');
                    updateSessionStorage(defaultJson);
                }
            }
        })
}
alert("jumpscare alerti")
function updateSessionStorage(json){
    //sessionStorage.setItem(sessionStorageName, JSON.stringify(json));
    //window.location.href = nextPageHtml;
}
