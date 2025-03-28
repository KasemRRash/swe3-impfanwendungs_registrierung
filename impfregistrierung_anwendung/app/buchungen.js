function ladeBuchungen() {
  console.log("Lade Buchungen...");

  let xhr = new XMLHttpRequest();
  xhr.open("GET", "BuchungAnzeigen", true);
  xhr.setRequestHeader("Content-Type", "application/json");

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      console.log("server:", xhr.responseText);
      let response = JSON.parse(xhr.responseText);

      response.buchungen.sort((a, b) => {
                const dateA = new Date(`${a.datum}T${a.zeit}`);
                const dateB = new Date(`${b.datum}T${b.zeit}`);
                return dateA - dateB;
            });

      console.log("Aktuelle Buchungen:", response.buchungen);


      zeigeBuchungenImKalender(response.buchungen);
    }
  };

  xhr.send();
}


function zeigeBuchungenImKalender(buchungen) {
    console.log("Buchungen für den Kalender:", buchungen);

    let buchungenTabelle = document.getElementById("buchungen-tabelle").getElementsByTagName('tbody')[0];
    if (!buchungenTabelle) {
        console.error("Kein Element mit ID 'buchungen-tabelle' gefunden!");
        return;
    }

    buchungenTabelle.innerHTML = ""; // lösche alte Einträge

    buchungen.forEach(buchung => {
        console.log("Füge Buchung hinzu:", buchung);

        let row = buchungenTabelle.insertRow();
        let datumCell = row.insertCell(0);
        let zeitCell = row.insertCell(1);
        let impfstoffCell = row.insertCell(2);
        let impfzentrumCell = row.insertCell(3);
        let actionCell = row.insertCell(4); // Neue Spalte für den stornieren-Button

        datumCell.textContent = buchung.datum;
        zeitCell.textContent = buchung.zeit.substring(0, 5);
        impfstoffCell.textContent = buchung.impfstoff;
        impfzentrumCell.textContent = buchung.impfzentrum;

        let stornierenButton = document.createElement("button");
        stornierenButton.textContent = "Stornieren";
        stornierenButton.classList.add("termin-stornieren");
        stornierenButton.onclick = function () {
            storniereTermin(buchung.id);
        };

        actionCell.appendChild(stornierenButton);
        console.log("Stornieren-Button hinzugefügt für Buchung:", buchung.id);
    });

    console.log("Buchungen wurden in die Tabelle eingefügt!");
}

function storniereTermin(buchungId) {
    console.log("Sende Stornierungsanfrage für Buchung:", buchungId);

    if (!confirm("Möchten Sie diesen Termin wirklich stornieren?")) {
        return; // abbrechen wenn der User nicht bestätigt
    }

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "TerminStornieren", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            console.log("Antwort:", xhr.responseText);
            if (xhr.status === 200) {
                alert("Termin wurde erfolgreich storniert.");
                ladeBuchungen(); // Aktualisiert die Tabelle
            } else {
                alert("Fehler beim Stornieren des Termins.");
            }
        }
    };

    let params = `buchung_id=${encodeURIComponent(buchungId)}`;
    xhr.send(params);
}
