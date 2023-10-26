
fetch('/perfil/sobremim', {
    method: 'GET'
})
    .then(response => {
        if (!response.ok) {
            throw new Error('Failure in perfil-fetch response');
        }
        return response.json();
    })
    .then(json => {
        console.log(json); 
        if ('nome' in json && 'email' in json && 'password' in json ){
            updatePerfil(json);
        }
        else {
            throw new Error('Failure in getting correct json value');
        }
        
    })
    .catch(error => {
        // Handle errors
        console.error('Error for sobremimGet.js:', error + '\n' + "getting default values");
        defaultValues();
    });


function defaultValues(){

}

function updatePerfil(json){
    const usernameDisplay = document.getElementById("username-display");
    const nameDisplay = document.getElementById("name-display");
    const emailDisplay = document.getElementById("email-display");
    
    usernameDisplay.textContent = json.username;
    nameDisplay.textContent = json.name;
    emailDisplay.textContent = json.email;
}