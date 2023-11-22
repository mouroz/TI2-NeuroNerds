import { generateProfileFrame } from "../ProfileFrame.js"

function createProfileHtml(){
    return (/*html*/`
        <main class="col profileDetails">
            <!--Needs better font-->
            <h2>Sobre mim</h2>
            <img src="/images/profile/basic-graph.png">
            <div class="mt-5"></div> <!--vertical divider-->

        </main>
    `)
}

export function createProfile(){
    let outerHtml = createProfileHtml();
    return generateProfileFrame(outerHtml);
} 

export const profileDefaultJson = {

};