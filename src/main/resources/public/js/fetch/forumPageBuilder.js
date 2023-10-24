const postHtml = `
<div class="container mg-4">
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
    post: {
        user: { //holds user data including post date. For now theres no profile picture
            name: 'username', date: '10/07/2004'
        },
        content: { //holds post contents
            title: 'THIS IS MOCK DATA', //depending we could need multiple text fields
            text: 'Lorem ipsum dolor sit amet consectetur adipisicing elit. Consectetur atque, obcaecati similique aliquid aspernatur ipsa accusantium fuga deleniti voluptate dolorum quis fugiat culpa neque inventore officia nihil praesentium, voluptates odit unde odio dolor recusandae quasi possimus! Tempora nostrum itaque enim explicabo iusto debitis incidunt facilis alias excepturi, ipsa minus quo aperiam. Doloribus, nulla labore sequi veniam eveniet soluta vel voluptas optio quia molestias, beatae odio illum ab! Facilis ipsa quasi pariatur corporis ab architecto beatae maiores assumenda exercitationem harum cupiditate voluptate, earum ad veniam laborum! Nesciunt obcaecati laboriosam rerum nulla, delectus ea quos doloribus, maxime quam vel debitis expedita animi odio architecto soluta exercitationem molestias? Iure rerum ratione illo magnam excepturi, eos laudantium! Obcaecati eveniet ea animi aperiam ab exercitationem iusto ipsum sed tempora quae suscipit amet autem consectetur eius perspiciatis consequatur neque eos quis, perferendis dignissimos iste nobis explicabo ipsam! Itaque quia nostrum et magnam nisi quisquam rem odit doloribus! Consequatur iste eveniet non numquam itaque architecto soluta voluptatibus eligendi ex vero eius autem nobis aperiam ducimus laboriosam, sunt et inventore minima consequuntur velit odit perferendis enim quaerat! At adipisci dolorem architecto repellendus blanditiis rerum odit vitae omnis explicabo, ipsum ducimus! Adipisci officiis quod, non officia iure cumque veniam dolor ut laborum voluptas quidem voluptates id! Accusamus modi cumque voluptatibus vel tempore ex. Doloremque assumenda id nemo laborum officiis sunt repellendus asperiores, tenetur alias ipsam. Odio delectus saepe obcaecati rerum consequuntur, aspernatur vero. Labore voluptates culpa, vel tempora quisquam aut recusandae. Laudantium totam ab nisi quo eius labore magni nam debitis. Culpa modi obcaecati exercitationem velit est quaerat dignissimos rem maxime voluptatem. Nihil omnis vel, ut, vero esse doloribus quaerat nobis laudantium incidunt neque nam amet sequi cupiditate totam, exercitationem est! Quia quam blanditiis repellat nostrum incidunt et consectetur error perferendis distinctio, repudiandae quidem odit dolore, dolorum, velit molestiae.',
            likes: 999,
            comments: 2,
            tags: ['Important', 'Adhd'],
            id: 0
         },
    },
    comment: [{
        user: { //holds user data including post date. For now theres no profile picture
            name: 'commenter', date: '10/07/2004'
        },
        content: { //holds post contents
            text: 'my comment',
            likes: 999,
            id: 0,
            linkedid: null
        }
    },{
        user: { //holds user data including post date. For now theres no profile picture
            name: 'commenter', date: '10/07/2004'
        },
        content: { //holds post contents
            text: 'my comment',
            likes: 999,
            id: 1,
            linkedid: 0
        }
    }]
}


///FETCH
const windowLocation = window.location.href;
const windowId = windowLocation.split('?').pop();
console.log(windowId);
fetch(`/api/get/forum:${windowId}`) //either this or send it by adding a body. Not sure which yet
    .then(response => {
        if (!response.ok) throw new Error('API request failed with status ' + response);
        return response.json();
    })
    .then(json => {
        console.log(json);
        ///main json
        if (!('post' in json)) throw new Error('Failure in atribute (post) on Forum-Page JSON');
        if (!('comment' in json)) throw new Error('Failure in atribute (comment) on Forum-Page JSON');

        ///post-json
        const postJson = json.post;
        if (!('user' in postJson)) throw new Error('Failure in atribute (post)->(user) on Forum-Page JSON');
        if (!('content' in postJson)) throw new Error('Failure in atribute (post)->(content) on Forum-Page JSON');

        const postUserJson = postJson.user;
        if (!('user' in postUserJson) || !('date' in postUserJson))
        throw new Error(`Invalid attribute(s) found inside (post)->(user) on Forum-Page JSON`);

        const postContentJson = postJson.content;
        if (!('title' in postContentJson) || !('text' in postContentJson) || !('likes' in postContentJson) ||
        !('comments' in postContentJson) || !('tags' in postContentJson) || !('id' in postContentJson))
        throw new Error(`Invalid attribute(s) found inside (post)->(content) on Forum-Page JSON`);

        ///comment-jsonArray
        for (let i = 0; i < json.comment.len; i++) {
            const commentJson = json.comment[i];
            if (!('user' in commentJson)) throw new Error(`Failure in atribute (comment)[${i}]->(user) on Forum JSON`);
            if (!('content' in commentJson)) throw new Error(`Failure in atribute (comment)[${i}]->(content) on Forum JSON`);

            const userJson = commentJson.user;
            if (!('name' in userJson) || !('date' in userJson))
                throw new Error(`Invalid attribute(s) found inside (comment)[${i}]->(user) json of Forum JSON`);

            const contentJson = commentJson.content;
            if (!('title' in contentJson) || !('text' in contentJson) || !('likes' in contentJson) ||
                !('comments' in contentJson) || !('tags' in contentJson) || !('id' in contentJson))
                throw new Error(`Invalid attribute(s) found inside (comment)[${i}]->(content) on Forum JSON`);
            
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
            alert('Moving to next page');
            window.location.href = `/outras/forum-page.html?${jsonContent.id}`;
            //move to next page
        });
        postsContainer.appendChild(tempDiv);
    }

}


