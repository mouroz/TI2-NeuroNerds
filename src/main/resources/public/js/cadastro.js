document.addEventListener("DOMContentLoaded", function() {
    const botaoProximo = document.getElementById("Proximo");
    const botaoVoltar = document.getElementById("Voltar");

    botaoVoltar.addEventListener("click", function() {
        window.location.href = "../index.html";
    });
    botaoProximo.addEventListener("click", function() {
        window.location.href = "./cadastro2.html";
    });
});