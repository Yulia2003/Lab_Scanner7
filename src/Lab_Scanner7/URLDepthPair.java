package Lab_Scanner7;

import java.net.*;
public class URLDepthPair {

    private final String currentURL;
    private final int currentDepth;

    public URLDepthPair(String URL, int depth) {
        currentURL = URL;
        currentDepth = depth;
    }

    public String getURL() {
        return currentURL;
    }

    public int getDepth() {
        return currentDepth;
    }

    public String toString() {
        String stringDepth = Integer.toString(currentDepth);
        return stringDepth + '\t' + currentURL;
    }

    public String getDocPath() {
        try {
            URL url = new URL(currentURL);
            return url.getPath();
        }
        catch (MalformedURLException e) {
            return null;
        }
    }

    public String getWebHost() {
        try {
            URL url = new URL(currentURL);
            return url.getHost();
        }
        catch (MalformedURLException e) {
            return null;
        }
    }
}