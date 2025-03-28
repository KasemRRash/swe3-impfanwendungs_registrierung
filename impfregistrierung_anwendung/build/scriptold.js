document.addEventListener("DOMContentLoaded", function () {
    console.log("script.js geladen");

    const routes = {
        home: `
        <nav class="nav">
            <a href="#/home" onclick="navigateTo('home')">Startseite</a> |
            <a href="#/register" onclick="navigateTo('register')">Registrieren</a> |
            <a href="#/login" onclick="navigateTo('login')">Login</a>
        </nav>
        <h1>Impfregistrierung Nemerb</h1>
        <h2>Startseite</h2>
        <div id="text">
            <p>Herzlich Willkommen</p><br><br>
            <p>Sie möchten sich impfen lassen?</p>
            <p>Auf dieser Webseite</p>
            <p>können Sie sich für eine</p>
            <p>Impfung in Nemerb</p>
            <p>registrieren.</p>
        </div>
        `,
        register: `
        <nav class="nav">
            <a href="#/home" onclick="navigateTo('home')">Startseite</a> |
            <a href="#/register" onclick="navigateTo('register')">Registrieren</a> |
            <a href="#/login" onclick="navigateTo('login')">Login</a>
        </nav>
        <h2>Registrierung</h2>
        <p>Erstellen Sie Ihr Konto zur Terminregistrierung in wenigen Schritten!</p>
        <p id="error-message" style="font-weight: bold;"></p>
        <form id="register-form">
            <div class="form">
                <p>E-Mail Adresse</p>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form">
                <p>Passwort</p>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form">
                <p>Passwort bestätigen</p>
                <input type="password" id="pw-confirm" name="pw-confirm" required>
            </div>
            <div class="form">
                <input type="checkbox" id="datenschutz" name="datenschutz" required>
                <label for="datenschutz">
                    Ich akzeptiere die <a href="datenschutz.html" target="blank">Datenschutzrichtlinie</a> und
                    <a href="Nutzungsbedingungen.html" target="blank">Nutzungsbedingungen</a> gelesen und akzeptiere sie.
                </label>
            </div>
            <button type="button" class="registrieren" onclick="register()">Registrieren</button>
        </form>
        `,
        login: `
        <nav class="nav">
            <a href="#/home" onclick="navigateTo('home')">Startseite</a> |
            <a href="#/register" onclick="navigateTo('register')">Registrieren</a> |
            <a href="#/login" onclick="navigateTo('login')">Login</a>
        </nav>
        <h2>Login</h2>
        <p>Melden Sie sich an, um Ihren Impftermin zu buchen oder Ihre Termine einzusehen.</p>
        <p id="error-message" style="font-weight: bold;"></p>
        <form id="login-form">
            <div class="form">
                <p>E-Mail Adresse</p>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form">
                <p>Passwort</p>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="button" class="login" onclick="login()">Login</button>
        </form>
        `,
        termine: `
        <nav class="nav">
            <a href="#/logout" onclick="logout(); return false;">Abmelden</a> |
        </nav>
        <h1>Impfregistrierung Nemerb</h1>
         <div id="buchungen-liste">
  <h3>Meine Termine</h3>
    <table id="buchungen-tabelle">
        <thead>
            <tr>
                <th>Datum</th>
                <th> Uhrzeit</th>
                <th> Impfstoff</th>
                <th> Impfzentrum</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

  </div>

        <h1>Kalenderansicht</h1>


 <div id="kalendar-container">
            <h2>Verfügbare Termine</h2>
            <div id="tage-liste"></div>
        </div>
        <div id="buchung-container" style="display: none;">
            <h2>Terminbuchung</h2>
            <label for="impfstoff">Impfstoff:</label>
            <select id="impfstoff">
                <option value="">Bitte wählen...</option>
            </select>
            <div id="impfzentrum-container" style="display: none;">
                <label for="impfzentrum">Impfzentrum:</label>
                <select id="impfzentrum">
                    <option value="">Bitte wählen...</option>
                </select>
            </div>
            <button id="buchen-btn" style="display: none;">Termin bestätigen</button>
        </div>
        `
   ,
      mitarbeiter:`
            <nav class="nav">
                <a href="#/logout" onclick="logout(); return false;">Abmelden</a> |
            </nav>
            <h1>Mitarbeiterbereich</h1>
            <div id="mitarbeiter-container">
               <h2>Buchungsübersicht</h2>
               <h2>Wähle ein Impfzentrum:</h2>
               <select id="impfzentrenDropdown">
               </select>
               <button id="auslastung-btn">Bestätigen</button>        
               <table id="buchungsTabelle">
                <thead id="buchungsTabelleHead">
               </thead>
               <tbody></tbody>
               </table>

           </div>
        `
    };
      


    function navigateTo(page) {
        const contentDiv = document.getElementById("content");
        if (!contentDiv) {
            console.error("Kein #content Element gefunden");
            return;
        }
        contentDiv.innerHTML = routes[page] || "<h1>Seite nicht gefunden</h1>";

        if (page === "termine") {
            console.log("Navigiere zur Termine-Seite.");
            if (typeof ladeKalender === "function") {
                ladeKalender();
            } else {
                console.warn("ladeKalender() ist noch nicht definiert. Lade termine.js...");
                let script = document.createElement("script");
                script.src = "termine.js";
                script.defer = true;
                script.onload = () => {
                    console.log("termine.js wurde geladen.");
                    if (typeof ladeKalender === "function") {
                        ladeKalender();
                    } else {
                        console.error("ladeKalender() wurde nicht definiert.");
                    }
                };
                document.body.appendChild(script);
            }
        }

      if (page === "mitarbeiter") {
    console.log("Navigiere zur Mitarbeiter-Seite.");
    let script = document.createElement("script");
    script.src = "impfzentren.js";
    script.defer = true;
    script.onload = () => {
        console.log("impfzentren.js wurde geladen.");
    if (typeof ladeImpfzentren === "function") {
             ladeImpfzentren();
     } else {
           console.error("ladeImpfzentren() wurde nicht definiert.");
 }

    };
    document.body.appendChild(script);
}


        console.log(`Navigiert zu: ${page}`);
    }

    window.navigateTo = navigateTo;

  //  console.log("Lade Startseite...");
//  navigateTo("home");


});

