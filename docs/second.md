# 🚨 Alert: DB_Failure_Alert

---

## 🎯 Cel alertu

Ten alert monitoruje błędy 500 występujące w aplikacji X w krótkim czasie. Jego celem jest szybkie wykrycie awarii backendu lub problemów z serwisem API.

---

## 🔍 Opis działania (dla ludzi)

Jeśli w logach zostanie wykryte co najmniej 10 błędów 500 w ciągu ostatnich 5 minut, system wyśle alert do zespołu DevOps.

---

## 🧠 SPL – zapytanie

index=app_logs status=500
| stats count by host
| where count >= 10

---

### 🔎 Omówienie zapytania:

| Fragment SPL                | Znaczenie                                              |
| --------------------------- | ------------------------------------------------------ | ------------------------------------------------------------ |
| `index=app_logs status=500` | Filtruje logi aplikacyjne, w których wystąpił błąd 500 |
| `                           | stats count by host`                                   | Grupuje błędy według hosta i zlicza je                       |
| `                           | where count >= 10`                                     | Przepuszcza tylko te hosty, które mają co najmniej 10 błędów |

---

## 📬 Odbiorcy alertu

- devops@example.com
- monitoring@example.com

---

## ⏰ Harmonogram

- Czas uruchamiania: **co 5 minut**
- Zakres danych: **ostatnie 5 minut**
- Warunki dodatkowe: brak

---

## ⚙️ Akcje podejmowane po uruchomieniu

- Wysyłka e-mail z wynikami
- Wstępna analiza przez zespół DevOps
- Jeśli alert powtarza się 3 razy z rzędu → eskalacja do Incident Managera

---

## 📊 Krytyczność

**High** – możliwy wpływ na dostępność aplikacji produkcyjnej

---

## 📅 Historia zmian

| Data       | Zmiana                       | Autor        |
| ---------- | ---------------------------- | ------------ |
| 2024-10-01 | Utworzenie alertu            | Jan Kowalski |
| 2025-03-15 | Dodano warunek `count >= 10` | Anna Nowak   |

---

## 📌 Uwagi końcowe

- Proszę aktualizować tę stronę po każdej modyfikacji alertu
- Jeśli nie wiesz, jak interpretować SPL – użyj narzędzia [SPL Explainer Tool](#) lub skonsultuj się z zespołem
