
///MODULATES THE PAGE HEADER (FOR WHEN YOU ARENT LOGGED IN)

document.addEventListener("DOMContentLoaded", function () {
    const newFooter = document.createElement("footer");

    // Create content for the new header
    newFooter.innerHTML = `
<!--footer based on bootstrap standart ones-->
<ul class="nav justify-content-center border-bottom"> <!-- mb-3 for extra section below-->
    <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Home</a></li>
    <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Features</a></li>
    <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Pricing</a></li>
    <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">FAQs</a></li>
    <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">About</a></li>
</ul>
<!--<p class="text-center text-muted">© 2022 Company, Inc</p>--> <!--Removed from template-->
`
    //newHeader.classList.add("container")
    //option for footer to not fill the top

    //conform the class values on footer element
    newFooter.classList.add("py-3");

    //appends to last value
    document.body.append(newFooter);

});

