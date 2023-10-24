document.addEventListener("DOMContentLoaded", function () {
    

    fetch('/api/endpoint')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failure in perfil-fetch response');
            }
            return response.json();
        })
        .then(data => {
            console.log(data); 
            if ('nome' in jsonResponse && 'email' in jsonResponse){
                updatePerfil(data);
            }
            else {
                throw new Error('Failure in getting correct json value');
            }
            
        })
        .catch(error => {
            // Handle errors
            console.error('Error:', error + '\n' + "getting default values");
            defaultValues();
        });
});

function defaultValues(){
    const nome = document.getElementById('nome');
    const email = document.getElementById('email');
    const descricao = document.getElementById('descricao');

    nome.textContent = "nome";
    email.textContent = "email";
    descricao.textContent = "descricao";
}

function updatePerfil(jsonResponse){
    const nome = document.getElementById('nome');
    const email = document.getElementById('email');
    const descricao = document.getElementById('descricao');
    
    nome.textContent = jsonResponse.nome;
    email.textContent = jsonResponse.email;
    descricao.textContent = jsonResponse.descricao;
}