package edu.uam.backend.cursos.Scrapping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    public static void main(String[] args) {
        try {
            String url = "https://en.wikipedia.org/wiki/Danny_Welbeck";
            Document doc = Jsoup.connect(url).get();

            // Por ejemplo, buscar todas las listas de premios en secciones de "Honours"
            Elements headers = doc.select("span.mw-headline");

            for (Element header : headers) {
                if (header.text().equalsIgnoreCase("Honours")) {
                    Element parent = header.parent().nextElementSibling();
                    if (parent != null) {
                        Elements honours = parent.select("li");
                        for (Element honour : honours) {
                            System.out.println(honour.text());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
