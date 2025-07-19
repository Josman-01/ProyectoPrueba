console.log("!Script JavaScript externo de menu cargado correctamente");

//Menus de sesion
const menuBoton = document.getElementById("menuInicio");
const menuDesplegable = document.getElementById("menuDesplegable");
const cerrarSesion = document.getElementById("cerrarSesion");

//Solicitudes de firma
const statusAPI = document.getElementById("estadoAPI");
const solicitudIndividual = document.getElementById("firmaIndividual");
const nombrefirmante = document.getElementById("firmante");
const solicitudFirmantes = document.getElementById("firmaColectiva");
const solicitudArchivos = document.getElementById("firmaColectivaArchivos")

//Consulta de ID
const consultaIDInput = document.getElementById("idConsulta");
const botonConsultar = document.getElementById("botonConsulta");
const idError = document.getElementById("idConsulta-error");

//Funciones
//Menu de sesion
menuBoton.addEventListener("click", function() {
    menuDesplegable.classList.toggle("mostrarMenu");
});

document.addEventListener("click", function(event) {
    if (!menuBoton.contains(event.target) && !menuDesplegable.contains(event.target)) {
        if (menuDesplegable.classList.contains("mostrarMenu")) {
            menuDesplegable.classList.remove("mostrarMenu");
        }
    }
});

//Cerrar sesion
cerrarSesion.addEventListener("click", function() {
    window.location.href = "index.html";
});

//Estado del API
statusAPI.addEventListener("click", obtenerEstadoAPI);
async function obtenerEstadoAPI() {
    try {
        const urlAPI = "http://localhost:8080/api/firmas/estado";
            
        const response = await fetch(urlAPI);

        if (!response.ok) {
            throw new Error('Error en la solicitud: ${response.status}');
        }

        const data = await response;
        console.log("Estado de la API:", data);
        alert("Estado de la API:" + data.response);
    } catch (error) {
        console.log("Error en la solicitud:", error);
        alert("Error en la solicitud:" + error);
    }
}

//Solicitud Firma individual un solo archivo
solicitudIndividual.addEventListener("click", solicitudfirma);

async function solicitudfirma(firmante, tipoDocumento, archivo, nombre, contenidoBase64, tipo) {
    try {
        const urlAPI = 'http://localhost:8080/api/firmas/solicitar';
        const datos = { firmante: nombrefirmante.value, archivo:{nombre: "pruebadesdejs.pdf", contenidoBase64: "VBERy0xLjQKJ...", tipo: "pdf"}, tipoDocumento: "pdf"};

        const response = await fetch(urlAPI, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        if (!response.ok) {
            throw new Error('Error en la solicitud: ${response.status}');
        } 

        const data = await response.text();
        console.log("Solicitud enviada exitosamente:", data);
        alert("Solicitud enviada exitosamente:" + data.firmante);
    } catch (error) {
        console.error("Error en la Solicitud: ", error);
        alert("Error en la solicitud:" + error.message);
    }
}

//Solicitud Firma Colectiva varios firmantes
solicitudFirmantes.addEventListener("click", variosfirmantes);
async function variosfirmantes(archivo, nombre, contenidoBase64, tipo, firmantes) {
    try {
        const urlAPI = "http://localhost:8080/api/firmas/solicitar-workflow";
        const datos = {archivo:{nombre: "pruebadesdejs.pdf", contenidoBase64: "VBERy0xLjQKJ...", tipo: "pdf"}, firmantes: ["Juana", "Pedro", "Maria"]};

        const response = await fetch(urlAPI, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        if (!response.ok){
            throw new Error('Error en la solicitud: ${response.status}');
        }

        const data = await response.text();
        console.log("Solicitud enviada exitosamente:", data);
        alert("Solicitud enviada exitosamente:" + data.firmante);
    } catch (error) {
        console.error("Error en la Solicitud:", error);
        alert("Error en la solicitud:" + error.message);
    }
}

//Solicitud Firma Colectiva varios archivos
solicitudArchivos.addEventListener("click", variosarchivos);
async function variosarchivos(firmanteUnico, archivos, nombre, contenidoBase64, tipo) {
    try {
        const urlAPI = "http://localhost:8080/api/firmas/solicitar-workflow";
        const datos = {firmanteUnico: "Juana", archivos: [{nombre: "prueba1.pdf", contenidoBase64: "VBERy0xLjQKJ...", tipo: "pdf"}, {nombre: "prueba2.pdf", contenidoBase64: "VBERy0xLjQKJ...", tipo: "pdf"}, {nombre: "prueba3.pdf", contenidoBase64: "VBERy0xLjQKJ...", tipo: "pdf"}]};

        const response = await fetch(urlAPI, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        if (!response.ok) {
            throw new Error('Error en la solicitud: ${response.status}');
        }

        const data = await response.text();
        console.log("Solicitud enviada correctamente:", data);
        alert("Solicitud enviada correctamente:" + data.firmante);
    } catch (error){
        console.error("Error en la solicitud;", error);
        alert("Error en la solicitud:" + error.message);
    }

    if (isValid) {
        console.log("ID valido");
    }
    return isValid;
}

//Funcion para limpiar los errores
function clearErrors() {
    idError.textContent = "";
    consultaIDInput.classList.remove("error");
}

//Validar id
function validarID() {
    clearErrors();

    let isValid = true;

    const id = consultaIDInput.value.trim();
    const idRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

    if (id === '') {
        idError.textContent = "El ID es obligatorio";
        idError.style.color = "red";
        consultaIDInput.classList.add("error");
        isValid = false
    } else if (idRegex.test(id)) {
        console.log("ID válido");
    } else {
        idError.textContent = "El ID no es valido no tiene un formato UUID";
        idError.style.color = "red";
        consultaIDInput.classList.add("error");
        isValid = false 
    }
    return isValid;
    
}

//Consulta de ID
const urlAPI = "http://localhost:8080/api/firmas/tareas/{{ID}}";

botonConsultar.addEventListener("click", async function() {
    try {
        const id = consultaIDInput.value;
        if (!validarID(id)) {
            console.log("ID invalido");
            return;
        }

        const finaUrlAPI = urlAPI.replace("{{ID}}", id);
        console.log("URL final:", finaUrlAPI);

        const response = await fetch(finaUrlAPI);

        if (!response.ok) {
            throw new Error('Error en la solicitud: ${response.status}');
        }

        const contentType = response.headers.get('content-type');
        let data;
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
            console.log("Respuesta de la solicitud (JSON):", data);
            alert("Respuesta de la solicitud:\n" + JSON.stringify(data, null, 2));
        } else {
            data = await response.text();
            console.log("Respuesta de la solicitud (Texto):", data);
            alert("Respuesta de la solicitud:\n" + data);
        }
    } catch (error) {
        console.error("Error en la solicitud:", error);
        alert("Error en la solicitud:" + error.message);
    }
});


consultaIDInput.addEventListener("input", clearErrors);