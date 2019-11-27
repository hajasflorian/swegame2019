# Wofür wird GIT verwendet?

In dieser LV werden all Ihre Übungsausarbeitungen über GitLab abgegeben bzw. eingereicht. Wichtig hierbei ist, dass wir Ihren Masterbranch als maßgeblich für die Bewertung ansehen. Die jeweiligen Punkte und Prüfungsergebnisse sind weiterhin in Moodle ersichtlich. Wichtig: Ändern Sie nicht den Namen des Masterbranches! Dieser muss den Namen "master" tragen. Nur Daten die vor der Abgabedeadline im Masterbranch liegen werden während der Abgabegespräche gewertet.

# Wie erhalte ich lokalen Zugriff auf dieses Repository?

Um optimal mit diesem Repository zu arbeiten sollten Sie es auf Ihr lokales Arbeitsgerät spiegeln. Verwenden Sie hierzu den Befehl `git clone UrlIhresRepositories`. Die Url Ihres Repositories befindet sich im Kopf dieser Webseite direkt unter dem Namen des Repositories und sollte vergleichbar sein zu `https://git01lab.cs.univie.ac.at/.....`. 

**Probleme mit den Zertifikaten**: Falls Sie beim clonen Ihres Git Repositories Probleme gemeldet bekommen welche mit der Prüfung der Zertifikate in Verbindung stehen ist es eine schnelle Lösung diese abzuschalten. Hierzu kann folgender Befehl verwendet werden:  `git config --global http.sslVerify false`

# Wie nütze ich dieses Repository?

Clonen Sie hierzu dieses Repository wie oben angegeben. Danach können Sie mit `git add`, `commit`, `push`, etc. damit arbeiten. Optimalerweise legen Sie hierzu nach dem initialen clone Ihren Namen und Ihre E-Mail-Adresse fest sodass alle Commits Ihnen direkt zugeordnet werden können. Verwenden Sie hierzu folgende Befehle:

> `git config --global user.name "Mein Name"`

> `git config --global user.email a123456@univie.ac.at`

Weitere Informationen über dem Umgang mit GIT sind in den hierzu passenden Folien auf Moodle erhältlich bzw. wurden während dem Git Tutorials besprochen. Zusätzlich können Sie Git auch interaktiv erlernen unter: https://try.github.io

# Welche Inhalte sind vorgegeben und wofür sind diese gedacht?

Es wurden mehrere **Ordner** sowie **.gitignore** Dateien vorgegeben. Letztere dienen dazu Ihr Repository nicht mit "unnötigen" Dateien zu befüllen, welche es erschweren würden Ihr Projekt während der Abgabegespräche in die Entwicklungsumgebungen der Lektoren zu importieren (temporäre Dateien, etc.). Ändern Sie diese Dateien daher nicht bzw. nur sehr behutsam. 

Die vorgegebenen Ordner sind wie folgt zu verwenden:
* **Dokumentation** - Nützen Sie diesen Ordner um Ihre Dokumentation abzulegen bzw. abzugeben. Dies ist insbesondere für Teilaufgabe 1 und 3 relevant da Sie so ihre Ausarbeitung (das zu erstellende PDF) hier hinterlegen können um diese abzugeben. Eine Vorlage für die Ausarbeitung von Teilaufgabe 1 finden Sie in Moodle. Falls notwendig können Sie in diesem Ordner auch PNGs und SVGs ablegen (für Klassen- und Sequenzdiagramme).
* **Executables** - Hinterlegen Sie hier die finalen kompilierten Abgaben Ihrer Implementierung von Teilaufgabe 2 und Teilaufgabe 3. Diese sollten .jar Dateien seien, welche sich mit `java -jar <NameDerJarDatei>` exekutieren lassen. Prüfen Sie ob dies der Fall ist! Tipps dazu wie die notwendigen Jar Dateien erstellt werden können finden Sie auf Moodle. Die Jar Dateien dienen nur zu Ihrer **Sicherheit** und sind nicht zwingend notwendig. Sollte sich während der Abgabegespräche Ihr Projekt nicht importieren oder bauen lassen wird auf die hier hinterlegten Jar Dateien zurückgegriffen (sollte dies notwendig werden kann dies zu Punkteabzügen führen). 
* **Source** - Nützen Sie diesen Ordner um die Implementierung von Teilaufgabe 2 und Teilaufgabe 3 abzulegen (daher Sourcecode, Konfigurationen, etc.). Verwenden Sie diesen Ordner daher als Eclipse Workspace und erstellen Sie in diesem alle notwendigen Java Projekte. Binden Sie die notwendigen Bibliotheken nicht manuell ein sondern nutzen Sie hierzu Build-Management-Tools, welche diese automatisch herunterladen und einbinden da es sonst zu Problemen während der Abgabegespräche kommen kann (z.B. durch nicht mehr passende Pfade). Dies ist auch der normalerweise im professionellen Entwickleralltag eingesetzte Weg um mit Abhängigkeiten umzugehen. Tipps hierzu gibt es während der Client\Server Tutorials, die bereitgestellten Beispielprojekte sind bereits passend konfiguriert.
* **Vor einer Deadline**: Während der Abgabegespräche wird der Inhalt des Ordners Source von den LV Leitern in Eclipse importiert, gebaut, etc. und das hierbei entstehende Jar zur Bewertung herangezogen. Prüfen Sie daher sicherheitshalber ob dies fehlerfrei möglich ist indem Sie dieses Repository neu klonen, in einen neuen Eclipse Workspace importieren und anschließen Ihre Projekte bauen bzw. in ein ausführbares Jar exportieren!
 
Erstellen Sie bitte keine zusätzlichen Ordner im Wurzelverzeichnis dieses Repositories und verändern Sie nicht die Namen, etc. der vorgegebenen Ordner. Zusätzliche Ordner, etc. können Sie als Unterordner in den vorgegebenen Ordnern erstellen.

# Welche Funktionen sollen nicht genutzt werden?

GitLab ist eine mächtige Software die es erlaubt zahlreiche Einstellungen anzupassen. Wir würden dazu Raten diese Möglichkeiten nicht zu nützen da unbedachte Aktionen (z.B. das Löschen des Masterbranches) hierbei auch negative Auswirkungen haben können da GitLab teilweise nicht nachfragt sondern Aktionen einfach ausgeführt (Think before you click!). Verwenden Sie daher optimalerweise einfach die vorgegebenen Einstellungen. Zur Sicherheit wurden, soweit möglich, unnötige Funktionen von uns bereits deaktiviert.
