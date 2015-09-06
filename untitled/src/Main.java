import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Main {

    protected static String valCursDateCurrent, valCursDatePrev, valCursDateNew;

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ParseException {

        for (int i = 0; i < 1; i++) {

            URL url = new URL("http://www.cbr.ru/scripts/XML_daily_eng.asp?date_req="); // no HTTPS :(
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "windows-1251"));

            String inputLine;
            PrintWriter printWriter = new PrintWriter(new File("temp.xml"));

            while ((inputLine = bufferedReader.readLine()) != null) printWriter.println(inputLine);
            printWriter.close();
            bufferedReader.close();

/* parsing XML with DOM */

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("temp.xml"));
            NodeList nodeList = document.getElementsByTagName("Valute");

            for (int j = 0; j < nodeList.getLength(); j++) {

                Element element = (Element) nodeList.item(j);
                String numCode = element.getElementsByTagName("NumCode").item(0).getChildNodes().item(0).getNodeValue();
                String charCode = element.getElementsByTagName("CharCode").item(0).getChildNodes().item(0).getNodeValue();
                int nominal = Integer.parseInt(element.getElementsByTagName("Nominal").item(0).getChildNodes().item(0).getNodeValue());
                String name = element.getElementsByTagName("Name").item(0).getChildNodes().item(0).getNodeValue();
                String value = element.getElementsByTagName("Value").item(0).getChildNodes().item(0).getNodeValue();
                valCursDateCurrent = element.getParentNode().getAttributes().getNamedItem("Date").getNodeValue();

                FileWriter fileWriterQuotation = new FileWriter("quotation_" + valCursDateCurrent + ".txt", true);
                fileWriterQuotation.write(valCursDateCurrent + " " + numCode + " " + charCode + " " + nominal + " " + name + " " + value + "\n");
                fileWriterQuotation.close();
            }

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
            LocalDate localDate = LocalDate.parse(valCursDateCurrent, dateTimeFormatter); // convert from dd.MM.yyyy to yyyy-MM-dd
            LocalDate localDatePrev = localDate.minusDays(1); // minus one day (pattern format yyyy-mm-dd)
            valCursDatePrev = String.valueOf(localDatePrev); // convert from date to String

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(valCursDatePrev);
            SimpleDateFormat formatterPrev = new SimpleDateFormat("dd.MM.yyyy"); // convert date from yyyy-MM-dd to dd.MM.yyyy
            valCursDatePrev = formatterPrev.format(date);
        }

        URL url = new URL("http://www.cbr.ru/scripts/XML_daily_eng.asp?date_req=" + valCursDatePrev); // get url connection to previous date
        URLConnection urlConnection = url.openConnection(); // server give previous accessed date !
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "windows-1251"));

        String inputLine;
        PrintWriter printWriter = new PrintWriter(new File("temp.xml"));

        while ((inputLine = bufferedReader.readLine()) != null) printWriter.println(inputLine);
        printWriter.close();
        bufferedReader.close();

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("temp.xml"));
        NodeList nodeList = document.getElementsByTagName("Valute");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Element element = (Element) nodeList.item(i);
            String numCode = element.getElementsByTagName("NumCode").item(0).getChildNodes().item(0).getNodeValue();
            String charCode = element.getElementsByTagName("CharCode").item(0).getChildNodes().item(0).getNodeValue();
            int nominal = Integer.parseInt(element.getElementsByTagName("Nominal").item(0).getChildNodes().item(0).getNodeValue());
            String name = element.getElementsByTagName("Name").item(0).getChildNodes().item(0).getNodeValue();
            String value = element.getElementsByTagName("Value").item(0).getChildNodes().item(0).getNodeValue();
            valCursDateNew = element.getParentNode().getAttributes().getNamedItem("Date").getNodeValue();

            FileWriter fileWriterQuotation = new FileWriter("quotation_" + valCursDateNew + ".txt", true);
            fileWriterQuotation.write(valCursDateNew + " " + numCode + " " + charCode + " " + nominal + " " + name + " " + value + "\n");
            fileWriterQuotation.close();
        }

        File fileValCursDateCurrent = new File("quotation_" + valCursDateCurrent + ".txt");
        File fileValCursNew = new File("quotation_" + valCursDateNew + ".txt");

        for (int i = 0; i < 1; i++) {
            File file = new File("quotation_" + valCursDateNew + ".txt");
            File file3 = new File("quotation" + valCursDateNew + ".txt");
            if (file3.exists() && file3.isFile()) {
                for (int j = 0; j < 1; j++) {
                    file3.delete();
                }
                FileWorker.copy(file, file3);
            } else {
                FileWorker.copy(file, file3);
            }
        }

        for (int i = 0; i < 1; i++) {
            File file4 = new File("quotation_" + valCursDateCurrent + ".txt");
            File file5 = new File("quotation" + valCursDateCurrent + ".txt");
            if (file5.exists() && file5.isFile()) {
                for (int j = 0; j < 1; j++) {
                    file5.delete();
                }
                FileWorker.copy(file4, file5);
            } else {
                FileWorker.copy(file4, file5);
            }
        }

        System.out.println("***********************************************");

        int begin = 10; // string from which to start reading
        int end = 11; // string which finishes reading
        int counter = 0;
        BufferedReader br = new BufferedReader(new FileReader(new File("quotation" + valCursDateCurrent + ".txt")));
        String str;
        while((str = br.readLine()) != null) {
            counter++;
            if(counter > begin-1) System.out.println(str);
            if(counter == end) break;
        }

        System.out.println("***********************************************");

        int begin1 = 10; // string from which to start reading
        int end1 = 11; // string which finishes reading
        int counter1 = 0;
        BufferedReader br1 = new BufferedReader(new FileReader(new File("quotation" + valCursDateNew + ".txt")));
        String str1;
        while((str1 = br1.readLine()) != null) {
            counter1++;
            if(counter1 > begin1-1) System.out.println(str1);
            if(counter1 == end1) break;
        }

        System.out.println("***********************************************");
        System.out.println("\n");
        System.out.println(new File("temp.xml").delete() && fileValCursDateCurrent.delete() && fileValCursNew.delete() ? "All temp xml files successfully deleted :)" : "Temp xml files not deleted :)");
        System.in.read();
    }
}