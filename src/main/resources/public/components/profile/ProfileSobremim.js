import { generateProfileFrame } from "../ProfileFrame.js"

function createSobremimHtml(){
    return (/*html*/`
        <main class="col profileDetails">
            <h2>Meus dados</h2>
            <div class="mt-5"></div> 
            <p>Username: <span id="display-username"></span></p>
            <p>Nome: <span id="display-nome"></span></p>
            <p>Email: <span id="display-email"></span></p>
        </main>
    `)
}

export function createSobremim(){
    let outerHtml = createSobremimHtml();
    return generateProfileFrame(outerHtml);
} 

export const sobremimDefaultJson = {
    
}