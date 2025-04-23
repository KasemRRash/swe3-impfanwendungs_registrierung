console.log("login.js geladen");


function login() {
  console.log("Login anfragen...");

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const xhr = new XMLHttpRequest();
  xhr.open("POST", "UserAnmelden", true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  xhr.withCredentials = true;

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      console.log("Server Antwort:", xhr.responseText);

      if (xhr.status === 200) {
        try {
          const response = JSON.parse(xhr.responseText);

          if (response.status === "success") {
            if (response.redirect === "mitarbeiter") {
              console.log("Login erfolgreich! Navigiere zu Mitarbeiter-Seite.");
              navigateTo("mitarbeiter");
              console.log("ladeImpfzentren()...");
//              resetInactivityTimer();
              ladeImpfzentren();
            
               reinitializeSessionTracking(); 
            }
            else {
              console.log("Login erfolgreich! Navigiere zu Termine-Seite.");
              navigateTo("termine");
              console.log("ladebuchungen() ...");
              ladeBuchungen();
               reinitializeSessionTracking(); 
              //resetInactivityTimer();
            }

            /*
            ["mousemove", "keydown", "scroll", "click"].forEach(event => {
              window.addEventListener(event, resetInactivityTimer);
            });
            */
            //console.log("starte timer in login");
            //resetInactivityTimer();
          }else {
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

  const params = `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`;
  xhr.send(params);
}

// Funktion global verfügbar machen
window.login = login;

console.log("login.js bereit.");

