package ru.itmo.socket.server.manager;

import ru.itmo.socket.common.entity.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Загрузчик коллекции Route из XML-файла при запуске программы.
 * Размещается в отдельном пакете loader, чтобы не конфликтовать с остальными классами.
 */
public class XmlCollectionLoader {
    private final RouteTreeSetManager manager;
    private final String xmlFilePath;

    public XmlCollectionLoader(RouteTreeSetManager manager, String xmlFilePath) {
        this.manager = manager;
        this.xmlFilePath = xmlFilePath;
    }

    /**
     * Загружает все элементы <Route> из указанного XML и добавляет их в менеджер.
     */
    public void load() {
        try {
            File xmlFile = new File(XmlCollectionLoader.class.getClassLoader().getResource(xmlFilePath).getFile());
            if (!xmlFile.exists()) {
                System.out.println("XML-файл не найден: " + xmlFilePath);
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("Route");
            DateTimeFormatter date = DateTimeFormatter.ISO_DATE_TIME;

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) continue;

                Element elem = (Element) node;
                Route rt = parseRoute(elem, date);
                manager.add(rt);
            }

            System.out.println("Загружено элементов: " + nodes.getLength());
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Разбирает один узел <Route> в объект Route.
     * Использует конструктор модели и метод setId, чтобы сохранить исходный id.
     */
    private Route parseRoute(Element e, DateTimeFormatter zonedFmt) {
        int id = Integer.parseInt(getText(e, "id"));
        String name = getText(e, "name");

        // Coordinates
        Element coordsEl = (Element) e.getElementsByTagName("coordinates").item(0);
        double x = Double.parseDouble(getText(coordsEl, "x"));
        int y = Integer.parseInt(getText(coordsEl, "y"));
        Coordinates coords = new Coordinates(x, y);

        // Начальная локация
        Element fromEl = (Element) e.getElementsByTagName("from").item(0);
        String fromName = getText(fromEl, "name");
        int fromLocX = Integer.parseInt(getText(fromEl, "x"));
        double fromLocY = Double.parseDouble(getText(fromEl, "y"));
        int fromLocZ = Integer.parseInt(getText(fromEl, "z"));
        Location from = new Location(fromName, fromLocX, fromLocY, fromLocZ);

        // Конечная локация
        Element toEl = (Element) e.getElementsByTagName("to").item(0);
        String toName = getText(toEl, "name");
        int toLocX = Integer.parseInt(getText(toEl, "x"));
        double toLocY = Double.parseDouble(getText(toEl, "y"));
        int toLocZ = Integer.parseInt(getText(toEl, "z"));
        Location to = new Location(toName, toLocX, toLocY, toLocZ);

        int distance = Integer.parseInt(getText(e, "distance"));

        // Создаём объект через конструктор модели
        Route rt = new Route(name, coords, from, to, distance);
        rt.setId(id);

        return rt;
    }

    private String getText(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return "";
        return nl.item(0).getTextContent().trim();
    }
}
