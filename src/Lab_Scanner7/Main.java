package Lab_Scanner7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws IOException {

        String[] arg = new String[] {"https://sales-generator.ru/blog/glubina-sayta/","1"};
        int depth = 0;
        try {
            depth = Integer.parseInt(arg[1]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("usage: java Crawler <URL> <depth>");
            System.exit(1);
        }
        LinkedList<URLDepthPair> pendingURLs = new LinkedList<URLDepthPair>();
        LinkedList<URLDepthPair> processedURLs = new LinkedList<URLDepthPair>();
        URLDepthPair currentDepthPair = new URLDepthPair(arg[0], 0);
        pendingURLs.add(currentDepthPair);
        ArrayList<String> seenURLs = new ArrayList<String>();
        seenURLs.add(currentDepthPair.getURL());

        while (pendingURLs.size() != 0) {
            URLDepthPair depthPair = pendingURLs.pop();
            processedURLs.add(depthPair);
            int myDepth = depthPair.getDepth();
            LinkedList<String> linksList;
            linksList = Crawler.getAllLinks(depthPair);
            if (myDepth < depth) {
                for (int i=0;i<linksList.size();i++) {
                    String newURL = linksList.get(i);
                    if (seenURLs.contains(newURL)) {
                        continue;
                    }
                    else {
                        URLDepthPair newDepthPair = new URLDepthPair(newURL, myDepth + 1);
                        pendingURLs.add(newDepthPair);
                        seenURLs.add(newURL);
                    }
                }
            }
        }
        Iterator<URLDepthPair> iter = processedURLs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}