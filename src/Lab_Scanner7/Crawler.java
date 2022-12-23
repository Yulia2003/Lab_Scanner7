package Lab_Scanner7;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
public class Crawler {

    static LinkedList<String> getAllLinks(URLDepthPair myDepthPair) throws IOException
    {
        // список

        LinkedList<String> URLs = new LinkedList<String>();
        Socket sock;

        //  создает новый сокет из полученной строки с
        //именем хоста и из целого числа с номером порта, и устанавливает соединение.

        try {
            sock = new Socket(myDepthPair.getWebHost(), 80);
        }
        catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + e.getMessage());
            return URLs;
        }
        catch (IOException ex) {
            return URLs;
        }

        // устанавливает время ожидания сокета
        //(Socket) в миллисекундах

        try {
            sock.setSoTimeout(3000);
        }
        catch (SocketException exc) {
            System.err.println("SocketException: " + exc.getMessage());
            return URLs;
        }
        String docPath = myDepthPair.getDocPath();
        String webHost = myDepthPair.getWebHost();
        OutputStream outStream;

        // позволяет сокету получать данные с другой стороны
        //соединения

        try {
            outStream = sock.getOutputStream();
        }
        catch (IOException exce) {
            return URLs;
        }
        PrintWriter myWriter = new PrintWriter(outStream, true);
        if (docPath.length() == 0) {
            myWriter.println("GET / HTTP/1.1");
            myWriter.println("Host: " + webHost);
            myWriter.println("Connection: close");
            myWriter.println();
        }
        else {
            myWriter.println("GET " + docPath + " HTTP/1.1");
            myWriter.println("Host: " + webHost);
            myWriter.println("Connection: close");
            myWriter.println();
        }
        InputStream inStream;

        //  позволяет сокету отправлять данные на другую сторону
        //соединения

        try {
            inStream = sock.getInputStream();
        }
        catch (IOException excep) {
            return URLs;
        }
        InputStreamReader inStreamReader = new InputStreamReader(inStream);
        BufferedReader BuffReader = new BufferedReader(inStreamReader);
        int serverCode = 0;
        String lineCode;
        try {
            lineCode = BuffReader.readLine();
        }
        catch (IOException except) {
            return URLs;
        }
        Pattern patternCode = Pattern.compile("([234])[0-9]{2}");
        Matcher matcherCode = patternCode.matcher(lineCode);
        while (matcherCode.find()) {
            serverCode = Integer.parseInt(lineCode.substring(matcherCode.start(), matcherCode.end() - 2));
        }
        if (serverCode == 2) {
            while (true) {
                String line;
                try {
                    line = BuffReader.readLine();
                }
                catch (IOException except) {
                    return URLs;
                }
                if (line == null) {
                    break;
                }
                Pattern patternURL = Pattern.compile(
                        "[\"]" + "[https?://]{7,8}" + "([w]{3})?" + "[\\w\\.\\-]+" + "\\." + "[A-Za-z]{2,6}" + "[\\w\\.-/]*" + "[\"]");
                Matcher matcherURL = patternURL.matcher(line);
                while (matcherURL.find()) {
                    String newLink = line.substring(matcherURL.start() + 1,
                            matcherURL.end() - 1);
                    URLs.add(newLink);
                }
            }

            //  закрывает сокет

            sock.close();
            return URLs;
        }
        if (serverCode == 3) {
            String newURL = "";
            String tempLine;
            while (true) {
                try {
                    tempLine = BuffReader.readLine();
                }
                catch (IOException except) {
                    return URLs;
                }
                if (tempLine == null) {
                    break;
                }
                Pattern patternNewURL = Pattern.compile("(Location: ){1}[\\S]+");
                Matcher matcherNewURL = patternNewURL.matcher(tempLine);
                while (matcherNewURL.find()) {
                    newURL = tempLine.substring(matcherNewURL.start() + 10,
                            matcherNewURL.end());
                }
            }

            //  закрывает сокет

            if (newURL.equals(myDepthPair.getURL())) {
                sock.close();
                return URLs;
            }
            URLDepthPair newDepthPair;
            newDepthPair = new URLDepthPair(newURL, myDepthPair.getDepth());
            return getAllLinks(newDepthPair);
        }
        return URLs;
    }
}