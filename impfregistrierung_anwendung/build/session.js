// ===== Session Management ===== //

function createSessionOverlay() {
  if(document.getElementById("session-overlay")) return;

  let overlay = document.createElement("div");
  overlay.id = "session-overlay";
  overlay.className = "session-popup";
  overlay.style.display = "none";
  overlay.innerHTML = `
        <div class="session-content">
            <h2>Sitzung abgelaufen</h2>
            <p>Ihre Sitzung ist abgelaufen. Bitte melden Sie sich erneut an.</p>
            <button onclick="logout()">Zum Login</button>
        </div>
    `;
  document.body.appendChild(overlay);
}

function showSessionOverlay() {
  let overlay = document.getElementById("session-overlay");

  if (!overlay) {
    createSessionOverlay();
    overlay = document.getElementById("session-overlay");
  }

  console.log("Öffne Session-Popup...");
  overlay.style.display = "flex";
}

function checkSession() {
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "SessionPruefen", true);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      var data = JSON.parse(xhr.responseText);
      console.log("SessionPruefen Antwort:", data);
      if (data.session === "inactive") {
        console.log("Session abgelaufen!");
        showSessionOverlay();
      }
    }
  };

  xhr.send();
}

// ===== Inaktivitäts-Tracking ===== //

let inactivityTimer;
let trackingActive = false; // Verhindert doppelte Registrierung der Event-Listener

function resetInactivityTimer() {
  console.log("Benutzer aktiv – Timer zurückgesetzt.");
  clearTimeout(inactivityTimer);

  inactivityTimer = setTimeout(() => {
    console.log("Benutzer war 2 Minuten inaktiv – prüfe Session...");
    checkSession();
  }, 120000);
}

// **Event-Listener nur einmal registrieren (wird nach Login aufgerufen)**
function registerInactivityTracking() {
  if (trackingActive) {
    console.log("Inaktivitäts-Tracking bereits aktiv.");
    return;
  }

  console.log("Registriere Event-Listener für Inaktivitäts-Tracking...");
  ["mousemove", "keydown", "scroll", "click"].forEach(event => {
    window.addEventListener(event, resetInactivityTimer);
  });

  trackingActive = true;
}

// **Nach Login Tracking erneut aktivieren (wird in `login.js` aufgerufen)**
function reinitializeSessionTracking() {
  console.log("Starte Inaktivitäts-Tracking nach Login...");

   clearTimeout(inactivityTimer);
  trackingActive = false;

  registerInactivityTracking();
  resetInactivityTimer();
}

// **Globale Funktionen verfügbar machen**
window.resetInactivityTimer = resetInactivityTimer;
window.checkSession = checkSession;
window.registerInactivityTracking = registerInactivityTracking;
window.reinitializeSessionTracking = reinitializeSessionTracking;
