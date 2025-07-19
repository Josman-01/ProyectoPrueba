console.log("!Script JavaScript externo de login cargado correctamente");

//Llamar las constantes
//Inicio de sesion
const usernameInput = document.getElementById("input-uno");
const passwordInput = document.getElementById("input-dos");
const loginButton = document.getElementById("button-uno");
const usernameError = document.getElementById("input-uno-error");
const passwordError = document.getElementById("input-dos-error");

//Fuciones
//Funcion para limpiar los errores
function clearErrors() {
    usernameError.textContent = "";
    passwordError.textContent = "";
    usernameInput.classList.remove("error");
    passwordInput.classList.remove("error");
}

//Funcion para validar la informacion de los inputs
function validateInputs() {
    clearErrors();

    let isValid = true;

    const username = usernameInput.value.trim();
    const password = passwordInput.value;

    if (username === '') {
        usernameError.textContent = "El nombre de usuario es obligatorio";
        usernameError.style.color = "red";
        usernameInput.classList.add("error");
        isValid = false
    } else if (username.length < 5) {
        usernameError.textContent = "El nombre de usuario debe tener al menos 5 caracteres";
        usernameError.style.color = "red";
        usernameInput.classList.add("error");
        isValid = false
    } else {
        console.log("Nombre de usuario válido");
    }

    const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,}$/;
     
    if (password === '') {
        passwordError.textContent = "La contraseña es obligatoria";
        passwordError.style.color = "red";
        passwordInput.classList.add("error");
        isValid = false
    } else if (!passwordRegex.test(password)) {
        passwordError.textContent = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número";
        passwordError.style.color = "red";
        passwordInput.classList.add("error");
        isValid = false
    } else {
        console.log("Contraseña válida");
    }

    if (isValid) {
        console.log("Formulario válido");
        window.location.href = "MenuInicio.html";
    }
    return isValid;
}

//Evento de validacion y limpieza de inputs
loginButton.addEventListener("click", validateInputs);
usernameInput.addEventListener("input", clearErrors);
passwordInput.addEventListener("input", clearErrors);