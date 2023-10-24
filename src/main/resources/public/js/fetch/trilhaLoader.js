const model = `
    <div class="rounded-circle-container">
        <a href="#">
            <img class="rounded-circle expand-on-hover" alt="avatar1"
                src="https://bs-uploads.toptal.io/blackfish-uploads/components/blog_post_page/content/cover_image_file/cover_image/1302221/regular_1708x683_staging.toptal.net_qa_how-to-write-testable-code-and-why-it-matters-25bff356169b7ee5f878b7b591b84afa.png" />
        </a>
    </div>
    <p class="trilhaTitle" id = "a"><b>Atividade</b></p>
`
const dataJson = {
    data: [
        { title: 'Placeholder 1', type: 0, description: 'my description is this' },
        { title: 'Placeholder 2', type: 0, description: 'my description is this' },
        { title: 'Placeholder 3', type: 1, description: 'my description is this' },
        { title: 'Placeholder 4', type: 1, description: 'my description is this' },
        { title: 'Placeholder 5', type: 0, description: 'my description is this' },
        { title: 'Placeholder 6', type: 1, description: 'my description is this' },
        { title: 'Placeholder 7', type: 0, description: 'my description is this' },
        { title: 'Placeholder 8', type: 1, description: 'my description is this' }
    ]
};

///FETCH
fetch('/api/endpoint')
    .then(response => {
        if (!response.ok) throw new Error('API request failed with status ' + response);
        return response.json();
    })
    .then(datajson => {
        console.log(datajson);

        //check json atributes are correct / exist
        if (!('data' in datajson)) throw new Error('Failure in atribute (data) on Trilha JSON');
        
        //check if jsons inside atribute [data] are also correct
        const jsonArray = datajson.data;
        for (let i = 0; i < jsonArray.length; i++) {
            const item = jsonArray[i];
            if (!('title' in item) || !('type' in item) || !('description' in item)) {
              throw new Error('Invalid attribute(s) found inside (data)['+i+'] json of Forum JSON');
            }
        }
        
        updateExercicios(datajson);
    })
    .catch(error => {
        console.error('trilhaBuilder.js error:', error + '\n' + 'getting default values');
        if (dataJson){ //reserved for if function starts receiving default as param
            updateExercicios(dataJson);
        }
    });

const imagesForIndex = ["https://bs-uploads.toptal.io/blackfish-uploads/components/blog_post_page/content/cover_image_file/cover_image/1302221/regular_1708x683_staging.toptal.net_qa_how-to-write-testable-code-and-why-it-matters-25bff356169b7ee5f878b7b591b84afa.png", 
"/imagens/card-pgInicial/buddha.jpeg"
]
function updateExercicios(json) {

    const container = document.getElementById("trilhaContainer");
    const nodes = container.querySelectorAll(".placeholderExercice"); //all nodes that were pre positioned on html
                                                                      

    //modal elements
    const modal = document.getElementById("modal");
    const modalImage = document.getElementById("modal-image");
    const modalTitle = document.getElementById("modal-title");
    const modalDescription = document.getElementById("modal-description");
    const modalLink = document.getElementById("modal-link");
    const closeBtn = document.getElementById("close-modal"); //for modal

    
    if (json.data.length > nodes.length) {
        console.warn("trilhaBuilder.js: Amount of nodes received from JSON exceeds amount of nodes in the html. Only "
            + nodes.length + " will be executed");
    }
    else if (json.data.length < nodes.length) {
        console.warn("trilhaBuilder.js: Amount of nodes received from JSON exceeds amount of nodes in the html. A few wont be executed");
    }

    var cont = 0; //acess to array and json order
    try {
        nodes.forEach(function (element) {
            if (cont >= json.data.length) return; //abort if exceeded
            console.log(json.data[cont]); //more details to console log

            element.classList.add("col-2");
            element.innerHTML = model;

            const image = element.querySelector("img");
            const title = element.querySelector(".trilhaTitle");

            
            if (json.data[cont].type < imagesForIndex.length) {
                image.src = imagesForIndex[json.data[cont].type];
            }
            else throw new Error('json type exceeds imagesForIndex length on nodes.forEach');

            title.textContent = json.data[cont].title;
            cont++; //end of atribute manipulation

            //add click listener for modal display
            element.addEventListener("click", () => {
                modalImage.src = image.src;
                modalTitle.textContent = title.textContent;
                modalDescription.textContent = json.data[cont].description;
                modalLink.href +=  "?" + modalTitle.textContent; //current identifier
                // Show the modal
                modal.style.display = "block";
            });


        });
    } catch (error) {
        console.error('trilhaBuilder.js node.forEach error:', error);
    };

    closeBtn.addEventListener("click", () => {
        modal.style.display = "none";
    });

    // Close the modal if the background is clicked
    modal.addEventListener("click", (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    });

}
