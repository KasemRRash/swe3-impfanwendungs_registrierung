function ladeImpfzentren() {
    console.log("Lade Impfzentren...");

    let dropdown = document.getElementById("impfzentrenDropdown");
    if (!dropdown) {
        console.error("Dropdown-Element nicht gefunden!");
        return;
    }

    let xhr = new XMLHttpRequest();
    xhr.open("GET", "ImpfzentrenAnzeigen", true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    let data = JSON.parse(xhr.responseText);
                    dropdown.innerHTML = "";
                    data.forEach(zentrumName => {
                        let option = document.createElement("option");
                        option.value = zentrumName.impfzentrum;
                        option.textContent = zentrumName.impfzentrum;
                        dropdown.appendChild(option);
                    });

                    console.log("Impfzentren erfolgreich geladen:", data);
                } catch (error) {
                    console.error("Fehler beim Parsen der Impfzentren:", xhr.responseText);
                }
            } else {
                console.error("Fehler beim Laden der Impfzentren:", xhr.status);
            }
        }
    };

    xhr.send();
}
document.getElementById("auslastung-btn").addEventListener("click", auslastungEinsehen);
function auslastungEinsehen() {
    let selectElement = document.getElementById("impfzentrenDropdown");
    let ausgewaehltesZentrum = selectElement.value;

    if (!ausgewaehltesZentrum) {
        console.error("Kein Impfzentrum ausgewählt.");
        return;
    }

    console.log("Lade Buchungsdaten für:", ausgewaehltesZentrum);
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "MitarbeiterServlet?impfzentrum=" + encodeURIComponent(ausgewaehltesZentrum), true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    let buchungen = JSON.parse(xhr.responseText);
                    console.log("Empfangene Buchungen:", buchungen);

                     updateBuchungsTabelle(buchungen, ausgewaehltesZentrum);
                } catch (error) {
                    console.error("Fehler beim Verarbeiten der Antwort:", error);
                }
            } else {
                console.error("Fehler beim Laden der Buchungen:", xhr.status, xhr.statusText);
            }
        }
    };
    xhr.send();
}
function updateBuchungsTabelle(buchungen, impfzentrumName) {
    let headerElement = document.querySelector("h2");
    headerElement.textContent = "Ausgewähltes Impfzentrum: " + impfzentrumName;
    let tabelle = document.getElementById("buchungsTabelle");
    let thead = document.getElementById("buchungsTabelleHead");
    let tbody = tabelle.querySelector("tbody");

    tbody.innerHTML = ""; 
    thead.innerHTML = "";

    let headerRow = thead.insertRow();
    let headers = ["Datum", "Slot", "Anzahl der Buchungen", "Kapazität", "Auslastung %"];
    headers.forEach(text => {
        let th = document.createElement("th");
        th.textContent = text;
        headerRow.appendChild(th);
    });
    let gesamtBuchungen = {}; 
    let gesamtKapazitaet = {};
    buchungen.forEach(ob => {
        let row = tbody.insertRow();
        row.insertCell(0).textContent = ob.datum;
        row.insertCell(1).textContent = ob.zeitslot;  
        row.insertCell(2).textContent = ob.anzahl;
        row.insertCell(3).textContent = ob.kapazitaet;
       let auslastung = (ob.anzahl / ob.kapazitaet) * 100;
       row.insertCell(4).textContent = `${auslastung.toFixed(2)}%`;
       if (!gesamtBuchungen[ob.datum]) {
            gesamtBuchungen[ob.datum] = 0;
            gesamtKapazitaet[ob.datum] = 0;
        }
        gesamtBuchungen[ob.datum] += ob.anzahl;
        gesamtKapazitaet[ob.datum] += ob.kapazitaet;
       
    });
      Object.keys(gesamtBuchungen).forEach(datum => {
        let gesamtRow = tbody.insertRow();
        gesamtRow.insertCell(0).textContent = datum + " (Gesamt)";
        gesamtRow.insertCell(1).textContent = "";
        gesamtRow.insertCell(2).textContent = gesamtBuchungen[datum];
        gesamtRow.insertCell(3).textContent = gesamtKapazitaet[datum];

        let gesamtAuslastung = ((gesamtBuchungen[datum] / gesamtKapazitaet[datum]) * 100).toFixed(2);
        gesamtRow.insertCell(4).textContent = gesamtAuslastung + '%';
    });
 
}


window.ladeImpfzentren = ladeImpfzentren;
