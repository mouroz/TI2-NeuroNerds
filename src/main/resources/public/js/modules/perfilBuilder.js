
///MODULATES THE PROFILE

document.addEventListener("DOMContentLoaded", function () {

    //get mainElement inside
    const mainElement = document.querySelector('main');


    //modularized profile body
    const profileBody = document.createElement("div");

    profileBody.innerHTML = `
<div class="container">
    <!--this container is currently a must for the footer css-->

    <div class="main-view container mt-5">
        <!--
            Spacing from elements inside box with container and mt-5. 
            Thats the why double containers
        -->
        <div class="profileField row">
            <div class="return-arrow">
                <a href="/outras/telainicial.html"><i class="fas fa-arrow-left"></i></a>
            </div>
            <!--placement for the return to initial screen inside this box-->

            <div class="col-3">
                <!--
                    Left and unchanging part of the profile view.
                    Holds profile picture and nav
                -->
                <div class="rounded-circle-container profilePicture">
                    <a href="#">
                        <img alt="avatar1" src="/imagens/card-pgPrincipal/perfil.png">
                    </a>
                </div>

                <div class="mt-5"></div> <!--vertical divider-->

                <div class="sidebar">
                    <h3 class="">Nome Sobrenome</h3>
                    <p class="streak"><b>6</b> <i class="fas fa-fire"></i> </p>

                    <div class="mt-5"></div> <!--vertical divider-->
                    <nav id="nav"> <!--navigation section-->
                        <h5 class="clickable pageOff">Informacoes gerais</h5>
                        <h5 class="clickable pageOff">Sobre mim</h5>
                        <h5 class="clickable pageOff">Plano</h5>
                        <div class="mt-5"></div> <!--lazy adjustment to make box bigger-->
                    </nav>
                </nav>
                </div>
            </div>

            <div class="col-2"></div> <!--lazy horizontal divider-->
            ${mainElement.outerHTML/*add main here*/}
        </div>
    </div>
</div>
`
    //finishes updating html
    if (mainElement) {
        mainElement.outerHTML = profileBody.innerHTML;
    } else throw Error;

    //Now to update which page you are in

    const navElement = document.getElementById('nav');
    const h5Elements = navElement.querySelectorAll('.pageOff');
    //class name a bit ambiguous. Might need to change later

    const currentPage = getCurrentPage();


        console.log(currentPage);
    if (currentPage === 'perfil.html') {
        h5Elements[0].classList.remove('pageOff');
        h5Elements[0].classList.add('pageOn');
    } else if (currentPage === 'sobremim.html') {
        h5Elements[1].classList.remove('pageOff');
        h5Elements[1].classList.add('pageOn');
    } else if (currentPage === 'plano.html') {
        h5Elements[2].classList.remove('pageOff');
        h5Elements[2].classList.add('pageOn');
    }

});

function getCurrentPage() {
    const path = window.location.pathname;
    const pathParts = path.split('/');
    const currentPage = pathParts[pathParts.length - 1]; //last index
    return currentPage;
}