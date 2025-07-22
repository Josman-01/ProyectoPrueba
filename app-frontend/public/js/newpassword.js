console.log("!Script JavaScript externo cargado correctamente");


const emailInput = document.getElementById('email');
const validateEmailBtn = document.getElementById('validate-email-btn');
const emailMessage = document.getElementById('email-message');

const emailSection = document.getElementById('email-section');
const passwordSection = document.getElementById('password-section');

const newPasswordInput = document.getElementById('new-password');
const confirmPasswordInput = document.getElementById('confirm-password');
const changePasswordBtn = document.getElementById('change-password-btn');
const passwordMessage = document.getElementById('password-message');

// --- Parte 1: Validar Email ---
validateEmailBtn.addEventListener('click', async () => {
    const email = emailInput.value;
    if (!email) {
        emailMessage.textContent = 'Por favor, ingresa un email.';
        emailMessage.style.color = 'orange';
        return;
    }

    // Simula la llamada a tu API de validación de email
    // Reemplaza esta URL con la de tu API real
    const validationApiUrl = 'http://localhost:8081/api/credentials/validEmail'; // Ejemplo
    emailMessage.textContent = 'Validando email...';
    emailMessage.style.color = 'blue';

    try {
        console.log("Email:", email);
        const datos = {email: email};

        const response = await fetch(validationApiUrl, {
            method: 'POST', // O GET, dependiendo de cómo diseñaste tu API
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(datos)
        });

        const data = await response.text();
        console.log("Respuesta de la API:", data);


        if (response.ok) { // Verifica el status HTTP y el resultado de la API
            emailMessage.textContent = 'Email validado correctamente. Ahora puedes cambiar tu contraseña.';
            emailMessage.style.color = 'green';

            // **Ocultar sección de email y mostrar sección de contraseña**
            emailSection.classList.add('hidden');
            passwordSection.classList.remove('hidden');

            // Opcional: Podrías deshabilitar el input de email si quieres
            // emailInput.disabled = true;

        } else {
            emailMessage.textContent = `Error al validar email: ${data.message || 'Email no encontrado o inválido.'}`;
            emailMessage.style.color = 'red';
        }
    } catch (error) {
        console.error('Error al conectar con la API de validación de email:', error);
        emailMessage.textContent = 'Ocurrió un error al validar el email. Intenta de nuevo.';
        emailMessage.style.color = 'red';
    }
});

// --- Parte 2: Cambiar Contraseña ---
changePasswordBtn.addEventListener('click', async () => {
    const email = emailInput.value; // El email sigue siendo accesible desde el input original
    const newPassword = newPasswordInput.value;
    const confirmPassword = confirmPasswordInput.value;
    const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,}$/;

    if (!newPassword || !confirmPassword) {
        passwordMessage.textContent = 'Por favor, ingresa y confirma la nueva contraseña.';
        passwordMessage.style.color = 'orange';
        return;
    }
    if (!passwordRegex.test(newPassword)) {
        passwordMessage.textContent = 'La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.';
        passwordMessage.style.color = 'red';
        return;
    }

    if  (newPassword.length < 8) {
        passwordMessage.textContent = 'La contraseña debe tener al menos 8 caracteres.';
        passwordMessage.style.color = 'red';
        return;
    }

    if (newPassword !== confirmPassword) {
        passwordMessage.textContent = 'Las contraseñas no coinciden.';
        passwordMessage.style.color = 'red';
        return;
    }

    // Simula la llamada a tu API de cambio de contraseña
    // Reemplaza esta URL con la de tu API real
    const changePasswordApiUrl = 'http://localhost:8081/api/credentials/change-password'; // Ejemplo
    passwordMessage.textContent = 'Cambiando contraseña...';
    passwordMessage.style.color = 'blue';

    try {
        const datos = {email: email, newPassword: newPassword}

        const response = await fetch(changePasswordApiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(datos)
        });

        // Asume que tu API devuelve un JSON con { success: true/false } o similar
        const data = await response.text();

        if (response.ok) { // Verifica el status HTTP y el resultado de la API
            passwordMessage.textContent = 'Contraseña cambiada exitosamente.';
            passwordMessage.style.color = 'green';
            // Opcional: Puedes redirigir al usuario o limpiar los campos
            window.location.href = 'index.html';
        } else {
            passwordMessage.textContent = `Error al cambiar contraseña: ${data.message || 'No se pudo cambiar la contraseña.'}`;
            passwordMessage.style.color = 'red';
        }
    } catch (error) {
        console.error('Error al conectar con la API de cambio de contraseña:', error);
        passwordMessage.textContent = 'Ocurrió un error al cambiar la contraseña. Intenta de nuevo.';
        passwordMessage.style.color = 'red';
    }
});
