console.log("!Script JavaScript externo de registro cargado correctamente");

//Crear constantes
//Registro
const nameregister = document.getElementById("nombreregistro");
const usernameregister = document.getElementById("usernameregistro");
const emailregister = document.getElementById("emailregistro");
const passwordregister = document.getElementById("passwordregistro");
const nameregisterError = document.getElementById("nombreregistro-error");
const usernameregisterError = document.getElementById("usernameregistro-error");
const emailError = document.getElementById("emailregistro-error");
const passwordregisterError = document.getElementById("passwordregistro-error");
const registerbutton = document.getElementById("buttonregistro");

//Funciones
//Funcion para limpiar los errores
function clearErrors() {
    nameregisterError.textContent = "";
    usernameregisterError.textContent = "";
    emailError.textContent = "";
    passwordregisterError.textContent = "";
    nameregister.classList.remove("error");
    usernameregister.classList.remove("error");
    emailregister.classList.remove("error");
    passwordregister.classList.remove("error");
}

//Funcion para validar la informacion
function validateInputsRegister(){
    clearErrors();

    let isValidRegister = true;

    const name = nameregister.value.trim();
    const username = usernameregister.value.trim();
    const email = emailregister.value.trim();
    const password = passwordregister.value;

    if (name === '') {
        nameregisterError.textContent = "El nombre es obligatorio";
        nameregisterError.style.color = "red";
        nameregister.classList.add("error");
        isValidRegister = false
    } else {
        console.log("Nombre válido");
    }
    if (username === '') {
        usernameregisterError.textContent = "El nombre de usuario es obligatorio";
        usernameregisterError.style.color = "red";
        usernameregister.classList.add("error");
        isValidRegister = false
    } else if (username.length < 5) {
        usernameregisterError.textContent = "El nombre de usuario debe tener al menos 5 caracteres";
        usernameregisterError.style.color = "red";
        usernameregister.classList.add("error");
        isValidRegister = false
    } else {
        console.log("Nombre de usuario válido");
    }
    if (email === '') {
        emailError.textContent = "El email es obligatorio";
        emailError.style.color = "red";
        emailregister.classList.add("error");
        isValidRegister = false
    } else if (!email.includes('@')) {
        emailError.textContent = "El email debe contener un @";
        emailError.style.color = "red";
        emailregister.classList.add("error");
        isValidRegister = false
    } else {
        console.log("Email válido");
    }

    const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,}$/;
     
    if (password === '') {
        passwordregisterError.textContent = "La contraseña es obligatoria";
        passwordregisterError.style.color = "red";
        passwordregister.classList.add("error");
        isValidRegister = false
    } else if (!passwordRegex.test(password)){
        passwordregisterError.textContent = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número";
        passwordregisterError.style.color = "red";
        passwordregister.classList.add("error");
        isValidRegister = false
    } else {
        console.log("Contraseña válida");
    }
    if (isValidRegister) {
        alert("Datos registrados correctamente");
    }
    return isValidRegister;
}

//Eventos de validacion y limpieza
registerbutton.addEventListener("click", validateInputsRegister);
nameregister.addEventListener("input", clearErrors);
usernameregister.addEventListener("input", clearErrors);
emailregister.addEventListener("input", clearErrors);
passwordregister.addEventListener("input", clearErrors);