// JavaScript to make the h5 element clickable
document.addEventListener("DOMContentLoaded", function() {
    var clickableElements = document.querySelectorAll(".clickable");
    
    clickableElements.forEach(function(element) {
        element.addEventListener("click", function() {
            var text = element.textContent.trim();
            // Determine which link was clicked and navigate accordingly
            switch (text) {
                case "Informacoes gerais":
                    window.location.href = "/outras/perfil/perfil.html";
                    break;
                case "Sobre mim":
                    window.location.href = "/outras/perfil/sobremim.html";
                    break;
                case "Plano":
                    window.location.href = "/outras/perfil/plano.html";
                    break;
                default:
                    break;
            }
        });
    });
});