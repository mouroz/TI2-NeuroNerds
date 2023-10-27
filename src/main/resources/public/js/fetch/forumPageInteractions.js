
const userLS = localStorage.getItem(userLocalStorageName);
if (userLS == null) console.error("Interactions -> Annonymous user error: cannot get logged user");

//STATIC GLOBAL ELEMENTS
const commentForm = document.getElementById('comment-form');
const commentBox = document.getElementById('comment-box-input');

const windowId = (window.location.href).split('?').pop();
console.log(windowId);


const localStr = localStorage.getItem('userData');
if(localStr === null) throw new Error('Invalid access - no session storage found for userData');
if (localStr.trim() === '') throw new Error('Invalid access - session storage is an empty string');

const localJson = JSON.parse(localStr);
if (!localJson) throw new Error('Invalid access - session storage is not usable json');
        
const subValue = localJson.payload.sub;

commentForm.addEventListener('submit', (e) => {
    e.preventDefault(); // Prevent default form submission

    console.log(userLS.payload.name);
    if (userLS == null || userLS.payload.name == "name") { //test user
        alert("Cannot comment as you are not logged in");
        return;
    }

    if (existingData != null) {
        alert("login through logged in acc")
        window.location.href = nextPageHtml;
        return;
    }

    const commentInput = commentBox.value; //commenting box content
    if (commentInput.trim() != '') {
        sendComment(commentInput);
    }
})

function sendComment(commentInput) {

        
    const serverRequestData = {
        username: userLS.payload.username,
        content: commentInput,
        sub: subValue,
        id: windowId
    };

    console.log(serverRequestData);
    fetch('/forum/page-comment', {
        method: "PUT", // You can use GET or POST, depending on your server's implementation.
        body: JSON.stringify(serverRequestData),
        headers: {
            "Content-Type": "application/json"
            //for form body there is no need for Content-Type header
        }
    })
        .then(response => {
            console.log(response.status);
            switch (response.status) {
                case 401:
                    alert("Server dismissed response");
                    break;
                case 200:
                    window.location.href = window.location.href; //refresh
                    break;
                default:
                    alert("Unexpected response from server");
            }
        });


}

