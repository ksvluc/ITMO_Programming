& "C:\Program Files\PuTTY\plink.exe" -L 7777:localhost:5432 -P 2222 s467392@se.ifmo.ru

mvn package -DskipTests -f server-socket/pom.xml -q

mvn package -DskipTests -f client-socket/pom.xml -q

chcp 65001; java -jar server-socket/target/server-socket.jar

chcp 65001; java --module-path "javafx-lib\\javafx-sdk-21\\lib" --add-modules javafx.controls,javafx.fxml -jar "client-socket\\target\\client-socket.jar"

