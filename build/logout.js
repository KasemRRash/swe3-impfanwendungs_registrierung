function logout() {
    console.log("logout anfragen...");

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "UserAbmelden", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            console.log("redirect home...");
            navigateTo("login"); 

          const overlay = document.getElementById("session-overlay");
            if (overlay) {
                overlay.style.display = "none";
            }

           if (inactivityTimer) {
                clearTimeout(inactivityTimer);
                inactivityTimer = null;
                console.log("Inaktivitäts-Tracking gestoppt.");
            }


          ["mousemove", "keydown", "scroll", "click"].forEach(event => {
                window.removeEventListener(event, resetInactivityTimer);
            });

            if (xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    console.log("Abmeldung ok", response.message);
                } catch (error) {
                    console.error("Fehler json", error);
                }
            } else {
                console.error("Fehler ", xhr.status, xhr.responseText);
                alert("Fehler beim Abmelden");
            }
        }
    };

    xhr.send();
}

window.logout = logout;
