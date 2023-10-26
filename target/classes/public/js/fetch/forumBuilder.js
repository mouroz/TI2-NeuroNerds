const postHtml = `
<div class="container circular-container post mg-4 mb-4 pt-4 pb-3">
<!--user data-->
<div class="row">
    <div class="post-user-display col">
        <img src="/imagens/card-pgPrincipal/perfil.png">
        <span class="post-username"></span>
        -
        <i class="post-age"></i>
    </div>

    <div class="col text-end"><i class="fa-solid fa-gear"></i></div>
</div>

<!--title-->
<div>
    <h4 class="post-title mt-2"></h4>
</div>

<!--tag-->
<div class="tag-container">
</div>

<!--description-->
<div class="post-description mt-3">
</div>

<!--like/comment-->
<div class="mt-3 row like-comment">
    <div class="col-1 circular-container">
        <span class="likes-num"></span>
        <i class="fa-regular fa-heart"></i>
    </div>
    <div class="col-1 circular-container">
        <span class="comments-num"></span> 
        <i class="fa-regular fa-comment "></i>
    </div>
</div>
</div>
`


const defaultJson = {
    data: [{
        user: { //holds user data including post date. For now theres no profile picture
            name: 'username', date: '10/07/2004'
        },
        content: { //holds post contents
            title: 'THIS IS MOCK DATA',
            text: 'Lorem ipsum dolor sit amet consectetur adipisicing elit. Omnis excepturi commodi labore deleniti iusto, porro mollitia quia facilis maiores cumque architecto, dicta nesciunt laboriosam nisi eligendi reiciendis fugit ea praesentium? Ipsam fugit quo eos rerum ab? Possimus, tenetur eos error laborum necessitatibus ut harum porro voluptates doloremque doloribus quo corrupti.',
            likes: 999,
            comments: 999,
            tags: ['Important', 'Adhd'],
            id: 0
         },
        
    }, {
        user: { //holds user data including post date. For now theres no profile picture
            name: 'username2', date: '10/10/2023'
        },
        content: { //holds post contents
            title: 'This years old post',
            text: 'Lorem ipsum dolor sit amet consectetur adipisicing elit. Omnis excepturi commodi labore deleniti iusto, porro mollitia quia facilis maiores cumque architecto, dicta nesciunt laboriosam nisi eligendi reiciendis fugit ea praesentium? Ipsam fugit quo eos rerum ab? Possimus, tenetur eos error laborum necessitatibus ut harum porro voluptates doloremque doloribus quo corrupti.',
            likes: 999,
            comments: 999,
            tags: ['Announcements', 'Important'],
            id: 1

        }   
    }]
}

const tagColor_LookUpTable = [ //0 = red, 1 = yellow, 2 = green, 3 = gray
    'bg-danger', 'bg-warning', 'bg-success', 'bg-secondary'
]

const tagToColor = { //doesnt work yet
    'important': 0,
    'announcements': 0,
    'adhd': 2,
};

///LOOKUP TABLE FOR TAG COLORS
function getTagColorClass(tag) {
    let colorIndex = 3;
    const cmpValue = tag.toLowerCase();
    if (cmpValue == 'announcements') colorIndex=0;
    if (cmpValue == 'important') colorIndex=0;
    if (cmpValue=='adhd') colorIndex=2;
    console.log(colorIndex)
    return tagColor_LookUpTable[colorIndex];
}

///FETCH
fetch("/forum/homepage")
    .then(response => {
        if (!response.ok) throw new Error('API request failed with status ' + response);
        return response.json();
    })
    .then(json => {
        console.log(json);
        if (!('data' in json)) throw new Error('Failure in atribute (data) on Forum JSON');

        for (let i = 0; i < json.data.len; i++) {
            const datajson = json.data[i];
            if (!('user' in datajson)) throw new Error(`Failure in atribute (data)[${i}]->(user) on Forum JSON`);
            if (!('content' in datajson)) throw new Error(`Failure in atribute (data)[${i}]->(content) on Forum JSON`);

            const userJson = datajson.user;
            if (!('name' in userJson) || !('date' in userJson))
                throw new Error(`Invalid attribute(s) found inside (data)[${i}]->(user) json of Forum JSON`);

            const contentJson = datajson.content;
            if (!('title' in contentJson) || !('text' in contentJson) || !('likes' in contentJson) ||
                !('comments' in contentJson) || !('tags' in contentJson) || !('id' in contentJson))
                throw new Error(`Invalid attribute(s) found inside (data)[${i}]->(content) on Forum JSON`);
            

        }
        updateForum(json);
       
    })
    .catch(error => {
        console.error('forumLoader.js error: ', error + '\n' + 'getting default values');
        if (defaultJson) { //reserved for if function starts receiving default as param
            updateForum(defaultJson);
        }
    });



//FIXED ELEMENTS
const postsContainer = document.getElementById("posts-section");


//FUNCTION ONLY HANDLES TYPE = 0 FOR NOW
function updateForum(json) {
    let len = json.data.length;
    if (json.data.length > 5) {
        console.warn("More than limit of 5 JSON (posts) received. Rest will be ignored");
        len = 5;
    }

    for (let i = 0; i < len; i++) {
        const curJson = json.data[i];
        const jsonUser = curJson.user;
        const jsonContent = curJson.content;
        
        //create element
        const tempDiv = document.createElement('div'); //this aproach will add an extra div
        tempDiv.innerHTML = postHtml;

        //query within the element
        const usernameDisplay = tempDiv.querySelector(".post-username"); //textcontent - X
        const postAgeDisplay = tempDiv.querySelector(".post-age"); //textcontent - X + (dias atras / X horas atras)
        const postTitle = tempDiv.querySelector(".post-title"); //textcontent - X
        const tagContainer = tempDiv.querySelector(".tag-container") //container
        const postDescription = tempDiv.querySelector(".post-description") //textcontent - X
        const likesNum = tempDiv.querySelector(".likes-num") //textcontent - X
        const commentsNum = tempDiv.querySelector(".comments-num") //textcontent - X

        ///update values according to json
        //user
        usernameDisplay.textContent = jsonUser.name;
        postAgeDisplay.textContent = jsonUser.date; //for now ill leave it like this

        //content
        postTitle.textContent = jsonContent.title;
        postDescription.textContent = jsonContent.text;
        likesNum.textContent = jsonContent.likes;
        commentsNum.textContent = jsonContent.comments;

 
        let tagLen = jsonContent.tags.length;
        if (tagLen > 5) {
            console.warn(`Higher than allowed amount of tags on (data)[${i}]->(tags)`);
            tagLen = 5;
        }
         
        for (let j = 0; j < tagLen; j++){
            const tagDiv = document.createElement('div');
            //for later compare the tags outputs from json to an array and assign different colors
            tagDiv.classList.add('circular-container', getTagColorClass(jsonContent.tags[j]), 'tag');
            tagDiv.innerHTML = jsonContent.tags[j];
            tagContainer.appendChild(tagDiv);
        }
        console.log(tempDiv)
        tempDiv.addEventListener('click', () => {
            window.location.href = `/outras/forum-page.html?${jsonContent.id}`;
            //move to next page
        });
        postsContainer.appendChild(tempDiv);
    }

}


