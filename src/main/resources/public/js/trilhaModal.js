
document.addEventListener("DOMContentLoaded", function () {
    
// Get the modal and its elements
const modal = document.getElementById("modal");
const modalImage = document.getElementById("modal-image");
const modalTitle = document.getElementById("modal-title");
const closeBtn = document.getElementById("close-modal");

// Get the clickable circle element
const circleContainers = document.querySelectorAll(".rounded-circle-container");

// Add a click event listener to the circle element
circleContainers.forEach((circleElement, index) => {
    circleElement.addEventListener("click", () => {
        // Set the modal content
        alert("sucess");
        const imageUrl = circleElement.querySelector("img").src;
        const title = circleElement.querySelector(".trilhaTitle").textContent;

        modalImage.src = imageUrl;
        modalTitle.textContent = title;

        // Show the modal
        modal.style.display = "block";
    });
});

// Close the modal when the close button is clicked
closeBtn.addEventListener("click", () => {
    modal.style.display = "none";
});

// Close the modal if the background is clicked
modal.addEventListener("click", (event) => {
    if (event.target === modal) {
        modal.style.display = "none";
    }
});

});

