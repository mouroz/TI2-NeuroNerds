document.addEventListener("DOMContentLoaded", function () {
    

    fetch('/api/endpoint')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failure in trilhaBuilder response');
            }
            return response.json();
        })
        .then(data => {
            console.log(data); 
            if ('binaryValues' in jsonResponse && 'title' in jsonResponse){
                updateTrilha(data);
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


}

function updateTrilha(jsonResponse){
    const container = document.getElementById("trilhaContainer");
    const exercices = container.querySelectorAll(".placeholderExercice");
    const model = 
    `<div class="col-2">
        <div class="rounded-circle-container">
            <a href="#">
                <img class="rounded-circle expand-on-hover" alt="avatar1" <!--grey-out to mark as not done-->
                    src="https://bs-uploads.toptal.io/blackfish-uploads/components/blog_post_page/content/cover_image_file/cover_image/1302221/regular_1708x683_staging.toptal.net_qa_how-to-write-testable-code-and-why-it-matters-25bff356169b7ee5f878b7b591b84afa.png" />
            </a>
        </div>
        <p class="trilhaTitle" id = "a"><b>Atividade</b></p>
    </div>`
    
    exercices.forEach(function (element) {
        //replace model string values here
        element.outerHTML = model;
    });
}