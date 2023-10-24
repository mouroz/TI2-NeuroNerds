const headerHtml =
    `
<!--HEADER WITH SIDEBAR FOR LOGGED WEBSITE-->
<div class="sidebarHeader"> <!--class="container" allows background to blend more-->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid" id="navbarDimensions">
        <a class="navbar-brand" href="/outras/telainicial.html">LOGO</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText"
            aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav mb-2 mb-lg-0">
                <li class="nav-item mb-2">
                    <a class="nav-link" href="#" data-bs-toggle="offcanvas"
                        data-bs-target="#offcanvasOptions"><i class="fa fa-user-circle"></i>
                        <span class="header-username-display">failure to get username</a></span>
                </li>
            </ul>
        </div>
    </div>
</nav>
<!-- Offcanvas -->
<div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasOptions">
    <div class="offcanvas-header">
        <h5 class="offcanvas-title" style="margin: 0 auto;">Para onde vamos ?</h5>
        <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas"
            aria-label="Close"></button>
    </div>
    <div class="offcanvas-body">
        <ul class="list-group">
            <!--for now the standart is given as adhd but if more options are added this value will need to be server updated-->
            <li class="list-group-item trilha-link"><a href="/outras/trilhas/adhd.html">Minha Trilha</a></li>
            <li class="list-group-item"><a href="/outras/perfil/perfil.html">Meu Perfil</a></li>
            <li class="list-group-item"><a href="/outras/forum.html">Forum</a></li>
            <li class="list-group-item"><a class="exit-button" href="#" >Sair</a></li>
        </ul>
    </div>
</div>
</div>
`;

const ss_UserKey = 'userData';

///MODULATES THE LOGGED IN HEADER
document.addEventListener("DOMContentLoaded", function () {

    const newHeader = document.createElement("header");    
    newHeader.innerHTML = headerHtml;
    
    //option for header to not have 80% width
    /*newHeader.classList.add("container")*/

    ///Update button click
    const exitButton = newHeader.querySelector('.exit-button');
    exitButton.addEventListener('click', (e) => {
        e.preventDefault();
        sessionStorage.removeItem(ss_UserKey);
        window.location.href='/index.html';
    })

    ///Update values according to sessionStorage JSON
    const sessionStr = sessionStorage.getItem(ss_UserKey);

    try {
        if (sessionStr.trim() === '') throw new Error('Invalid access - no session storage found for userData');

        const sessionJson = JSON.parse(sessionStr);
        if (!sessionJson) throw new Error('Invalid access - session storage is not usable json');

        const displayName = newHeader.querySelector('.header-username-display');
        const trilhaHtmlRedirect = newHeader.querySelector('.trilha-link');

        //update trilha
        trilhaHtmlRedirect.href = '/outras/trilhas/adhd.html'; //for now only this option exists

        //change display name to session name
        displayName.textContent = sessionJson.payload.name;

        //insert header
        document.body.insertBefore(newHeader, document.body.firstElementChild);

    } catch (error) {

        console.error('userHeader.js: ' + error);
    }

});