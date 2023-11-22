/* Stores and retrieves simple user data used by other scripts
 *
 */

const defaultJson = {
    name: 'nome',
    username: 'username' //used as identifier
}
const lsPath = 'userData';


const user = localStorage.getItem('userData');
if (user == null) console.warn('user data not found');

let parsedUser;
try {
    parsedUser = JSON.parse(user);
} catch (error) {
    console.error('Error parsing json: ', error.message);
    parsedUser = null;
}

export function getUserData() {return parsedUser;}; 
export function getName() {
    if (parsedUser != null) return parsedUser.name;
    return null; 
}
export function getUsername() {
    if (parsedUser != null) return parsedUser.username;
    return null; 
}


export function storeNewUserData(name, id){
    const newJson = {
        name: `${name}`,
        id: `${id}`
    }

    localStorage.setItem(lsPath, JSON.stringify(newJson));
}

export function storeDefaultUserData(){
    localStorage.setItem(lsPath, JSON.stringify(defaultJson));
}

export function clearUserData(){
    localStorage.removeItem(lsPath);
}