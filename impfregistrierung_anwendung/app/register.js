console.log("register.js geladen");

function register() {
    console.log("Registrierung anfragen...");

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const passwordConfirm = document.getElementById("pw-confirm").value;
    const datenschutzChecked = document.getElementById("datenschutz").checked;

    if (!datenschutzChecked) {
        alert("Bitte akzeptiere die Datenschutzrichtlinien.");
        return;
    }

//  console.log("Eingegebenes Passwort:", password);
//    console.log("Passwort Bestätigung:", passwordConfirm);

  /*
    if (password !== passwordConfirm) {
        document.getElementById("error-message").textContent = "Passwörter stimmen nicht überein.";
        document.getElementById("error-message").style.color = "red";
        return;
    }*/


  

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "register", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            console.log("Server Antwort:", xhr.responseText);

            if (xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);

                    if (response.status === "success") {
                        console.log("Registrierung erfolgreich! Navigiere zur Login-Seite.");
                        navigateTo("login");
                    } else {
                        console.error("Fehler:", response.message);
                        document.getElementById("error-message").textContent = response.message;
                        document.getElementById("error-message").style.color = "red";
                    }
                } catch (error) {
                    console.error("Fehler JSON-Antwort:", error);
                }
            } else {
                console.error("Server-Fehler:", xhr.status);
                alert("Ein Fehler ist aufgetreten");
            }
        }
    };

   const params = `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}&passwordConfirm=${encodeURIComponent(passwordConfirm)}`;

//  console.log("Gesendete Parameter:", params);
  xhr.send(params);
}

// Funktion global verfügbar machen
window.register = register;

console.log("register.js bereit.");

