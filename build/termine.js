console.log("termine.js wurde geladen!");

let gewaehlterTermin = null;

function ladeVerfuegbareTermine() {

  const tageListeEl = document.getElementById("tage-liste");
  if (!tageListeEl) {
    console.error("Kein Element mit ID 'tage-liste' gefunden!");
    return;
  }
  tageListeEl.innerHTML = "";

  let xhr = new XMLHttpRequest();
  xhr.open("POST", "FreieSlotsAnzeigen", true);
  xhr.setRequestHeader("Content-Type", "application/json");

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      console.log("HTTP-Status:", xhr.status);
      console.log("Antwort:", xhr.responseText);

      if (xhr.status === 200) {
        let data = JSON.parse(xhr.responseText);
        console.log("API-Antwort:", data);

        let jetzt = new Date();

        let termineMap = new Map();

        data.forEach(slot => {
          let tagDatum = new Date(slot.datum);
          let verfuegbareZeiten = slot.zeiten.filter(zeit => {
            let [stunden, minuten] = zeit.split(":").map(Number);
            let slotDatum = new Date(tagDatum);
            slotDatum.setHours(stunden, minuten, 0, 0);
            return slotDatum > jetzt;
          });

          if (verfuegbareZeiten.length > 0) {
            if (!termineMap.has(slot.datum)) {
              termineMap.set(slot.datum, []);
            }
            termineMap.get(slot.datum).push(...verfuegbareZeiten);
          }
        });

        termineMap.forEach((zeiten, datum) => {
          let tagDatum = new Date(datum);

          let tagContainer = document.createElement("div");
          tagContainer.classList.add("tag-container");
          tagContainer.innerText = tagDatum.toLocaleDateString("de-DE", {
            weekday: 'long', day: '2-digit', month: '2-digit', year: 'numeric'
          });

          let zeitGrid = document.createElement("div");
          zeitGrid.classList.add("zeit-grid");
          zeitGrid.style.display = "none";

          zeiten.forEach(zeit => {
            let zeitSlot = document.createElement("div");
            zeitSlot.classList.add("zeit-slot");
            zeitSlot.innerText = zeit;

            zeitSlot.addEventListener("click", function () {
              document.querySelectorAll(".zeit-slot").forEach(slot => slot.classList.remove("ausgewaehlt"));
              gewaehlterTermin = {
                datum: datum,
                zeit: zeit,
                impfzentrum: data.find(s => s.datum === datum && s.zeiten.includes(zeit)).impfzentrum
              };
              zeitSlot.classList.add("ausgewaehlt");

              console.log("Gewählter Termin:", gewaehlterTermin);
              zeigeImpfstoffAuswahl();
            });

            zeitGrid.appendChild(zeitSlot);
          });

          tagContainer.addEventListener("click", function () {
            zeitGrid.style.display = (zeitGrid.style.display === "none") ? "grid" : "none";
          });

          tageListeEl.appendChild(tagContainer);
          tageListeEl.appendChild(zeitGrid);
        });

      } else {
        console.error("Fehler beim Laden der Slots:", xhr.statusText);
      }
    }
  };

  xhr.send();
}

function ladeImpfstoffe() {
  if (!gewaehlterTermin || !gewaehlterTermin.impfzentrum || gewaehlterTermin.impfzentrum.length === 0) {
    console.warn("Kein Impfzentrum für diesen Termin verfügbar!");
    return;
  }

  console.log("Lade Impfstoffe für Impfzentren:", gewaehlterTermin.impfzentrum);

  let impfzentrenParam = encodeURIComponent(gewaehlterTermin.impfzentrum.join(",")); // Array zu String machen
  let xhr = new XMLHttpRequest();
  xhr.open("GET", "ImpfstoffAnzeigen?impfzentren=" + impfzentrenParam, true);
  xhr.setRequestHeader("Content-Type", "application/json");

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      console.log("HTTP-Status:", xhr.status);
      console.log("Antwort:", xhr.responseText);

      if (xhr.status === 200) {
        let impfstoffe = JSON.parse(xhr.responseText);
        console.log("Gefilterte Impfstoffe:", impfstoffe);

        const impfstoffSelect = document.getElementById("impfstoff");
        if (!impfstoffSelect) {
          console.error("Dropdown 'impfstoff' nicht gefunden!");
          return;
        }

        impfstoffSelect.innerHTML = '<option value="">Bitte wählen...</option>';
        impfstoffe.forEach(impfstoff => {
          let option = document.createElement("option");
          option.value = impfstoff.id;
          option.textContent = impfstoff.name;
          impfstoffSelect.appendChild(option);
        });

        console.log("Impfstoffe erfolgreich ins Dropdown eingefügt.");

        impfstoffSelect.addEventListener("change", function () {
          const gewaehlterImpfstoff = this.value;
          if (!gewaehlterImpfstoff) return;

          console.log("Impfstoff geändert:", gewaehlterImpfstoff);

          ladeVerfuegbareImpfzentren(gewaehlterTermin.impfzentrum, gewaehlterImpfstoff);
        });

      } else {
        console.error("Fehler beim Laden der Impfstoffe:", xhr.statusText);
      }
    }
  };

  xhr.send();
}

function zeigeImpfstoffAuswahl() {
  console.log("zeigeImpfstoffAuswahl() aufgerufen");
  console.log("Aktueller Termin:", gewaehlterTermin);

  if (!gewaehlterTermin) return;

  document.getElementById("buchung-container").style.display = "block";

  ladeImpfstoffe();


}

function ladeVerfuegbareImpfzentren(alleImpfzentren, gewaehlterImpfstoff) {
  console.log("Lade verfügbare Impfzentren für Impfstoff:", gewaehlterImpfstoff);

  let gefilterteImpfzentren = alleImpfzentren.filter(impfzentrum =>
    gewaehlterTermin.impfzentrum.includes(impfzentrum)
  );

  let impfzentrumContainer = document.getElementById("impfzentrum-container");
  let impfzentrumSelect = document.getElementById("impfzentrum");

  if (!impfzentrumSelect) {
    console.log("Erstelle neues Dropdown für Impfzentren...");
    impfzentrumSelect = document.createElement("select");
    impfzentrumSelect.id = "impfzentrum";
    impfzentrumContainer.appendChild(impfzentrumSelect);
  } else {
    console.log("Dropdown für Impfzentren bereits vorhanden, wird aktualisiert...");
  }

  impfzentrumSelect.innerHTML = '<option value="">Bitte wählen...</option>';
  gefilterteImpfzentren.forEach(impfzentrum => {
    let option = document.createElement("option");
    option.value = impfzentrum;
    option.textContent = impfzentrum;
    impfzentrumSelect.appendChild(option);
  });

  console.log("Impfzentren erfolgreich ins Dropdown eingefügt.");

  impfzentrumContainer.style.display = "block";

  let buchenBtn = document.getElementById("buchen-btn");
  if (!buchenBtn) {
    buchenBtn = document.createElement("button");
    buchenBtn.id = "buchen-btn";
    buchenBtn.textContent = "Termin buchen";
    buchenBtn.style.display = "none"; 

    impfzentrumContainer.appendChild(buchenBtn);

    buchenBtn.addEventListener("click", sendeBuchung);
  }

  impfzentrumSelect.addEventListener("change", function () {
    if (impfzentrumSelect.value) {
      buchenBtn.style.display = "block";
    } else {
      buchenBtn.style.display = "none";
    }
  });

}


function sendeBuchung() {

  const impfzentrum = document.getElementById("impfzentrum").value;
  const impfstoff = document.getElementById("impfstoff").value;

  if (!impfzentrum || !impfstoff || !gewaehlterTermin) {
    alert("Bitte alle Felder ausfüllen!");
    return;
  }

  let xhr = new XMLHttpRequest();
  xhr.open("POST", "TerminBuchen", true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let params = `datum=${encodeURIComponent(gewaehlterTermin.datum)}`
    + `&zeit=${encodeURIComponent(gewaehlterTermin.zeit)}`
    + `&impfstoff_id=${encodeURIComponent(impfstoff)}`
    + `&impfzentrum=${encodeURIComponent(impfzentrum)}`;
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        let response = JSON.parse(xhr.responseText);
        if (response.success) {
          alert(`Termin erfolgreich gebucht!\n\n Datum: ${gewaehlterTermin.datum}\n Uhrzeit: ${gewaehlterTermin.zeit}\n Impfzentrum: ${impfzentrum}\n Impfstoff: ${impfstoff}`);

          document.getElementById("buchung-container").style.display = "none";
          ladeBuchungen();
        } else {
          alert("Fehler: " + response.error);
        }
      } else {
        alert("Fehler bei der Buchung! Bitte erneut versuchen.");
      }
    }
  };

  console.log("Gesendete Daten:", params);
  xhr.send(params);
}



function ladeKalender() {
  console.log("ladeKalender() wird aufgerufen");

  if (!document.getElementById("tage-liste") ||
    !document.getElementById("buchung-container") ||
    !document.getElementById("impfzentrum-container") ||
    !document.getElementById("impfzentrum") ||
    !document.getElementById("impfstoff") ||
    !document.getElementById("buchen-btn")) {
    console.error("Fehlende HTML-Elemente!");
    return;
  }

  document.getElementById("buchen-btn").addEventListener("click", sendeBuchung);

  ladeVerfuegbareTermine();
}

