//Simple model for each question
const defaultQuestionJson = {
    text: 'my description is this', 
    type: 0, //support for extra types for later
    alternatives: [
        "alternative1", "alternative2", "alternative3", "alternative4", "alternative5"
    ], //max 5
    correct: '1' //index 0 - 4 for alternative
}

//Queue (max 10) of exercicios
const defaultJsonQueue = [
    defaultQuestionJson,
    defaultQuestionJson,
    defaultQuestionJson
]
const incrementDefault = 2.5; 
const isCorrectMod = 5

let queue; //Queue following defaultJsonQueue model
let queueLen = 0; 
let trilhaProgress = 0; //(100) is completed
let correctAlternative = 0; //in index 0 - 4

import { getUsername } from "../modules/user-data.js";
import { restfulJsonGet, getPaths, restfulJsonPost, postPaths } from "../modules/bancoti2-fetch.js";

const username = getUsername();
const neuro = window.location.search.split('?').pop();
if (neuro != 'tdah' ||  neuro != '' || neuro != '') {
    console.error('Incorrect type value received');
}

const form = document.getElementById("question-form");
const question = document.querySelector(".question-form-text");
const alternatives = document.querySelectorAll(".alternative-label"); //labels
const alternativesText = document.querySelectorAll(".alternative-text");

const button = document.getElementById("enviar-button");
const progressBar = document.getElementById("progress-bar");
const debugCorrectValue = document.getElementById("alternative-debug-correct");



addEventListener('DOMContentLoaded', () => {
    if (username == null || username == '') console.error('Couldnt access user key');  
    fetchNewQueue();
})


function fetchNewQueue(){
    console.log("getting new queue");

    const jsonQueue = restfulJsonGet(getPaths.exercicios, username);

    queueLen = 0;
    if (jsonQueue != null && jsonQueue.length > 0) queue = jsonQueue;
    else {
        queue = defaultJsonQueue;
        //alert ('Using mock-up data');
    }

    loadExercicio (queue[queueLen++]);
}

function loadExercicio(json) {
    if (json.alternatives.length != 5) {
        console.warn("unexpected alternatives length " + json.alternatives.length);
    }

    question.textContent = json.text;
    if (debugCorrectValue != null) debugCorrectValue.textContent = json.correct;
    correctAlternative = json.correct;
    let cont = 0;
   
    alternatives.forEach(function (element) {
        const alternativeText = element.querySelector('.alternative-label-text');
        alternativeText.textContent = json.alternatives[cont];
        cont++;
    });

}

//Checks if input was selected and if so compare to stored correct result
form.addEventListener('submit', (e) => {
    e.preventDefault();

    let selectedValue = -1;
    for (let i = 0; i < alternatives.length; i++) {
        const label = alternatives[i];
        const input = label.querySelector('input[type="radio"]');
        if (input.checked) {
            selectedValue = input.value;
            input.checked=false;
            break; 
        }
    }

    if (selectedValue > -1){ //if something was selected
        const correct = (selectedValue == correctAlternative);
        let increment = (correct) ? incrementDefault*isCorrectMod : incrementDefault;
        trilhaProgress = (increment + trilhaProgress > 100) ? 100 : trilhaProgress+increment;
        //increase bar
        progressBar.style.height = trilhaProgress + '%';
        
        const isCorrectString = (correct) ? 'TRUE' : 'FALSE';
        console.log(isCorrectString);
        const postJson = {
            'username': username,
            'neuro': neuro,
            'isCorrect': isCorrectString
        };

        if ( !restfulJsonPost(postPaths.exerciciosSubmit, postJson) ) {
            //alert ('Couldnt submit answer to server');
            //return;
        }

        if (trilhaProgress < 100) {
            if (queueLen < queue.length) loadExercicio(queue[queueLen++]);
            else fetchNewQueue();
        }
        else {
            alert('Voce concluiu!');
            //code here
        }
    } else {
        alert('Selecione uma opcao!');
    }

});
