package com.gateway.dashboard.interviews.anthpc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class VirtualThreadWebCrawler {

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private final Set<String> seen = ConcurrentHashMap.newKeySet();
    private final int depthLimit;
    private final Semaphore semaphore;
    private String startHost;

    record UrlDepth(String url, int depth) {}

    public VirtualThreadWebCrawler(int depthLimit, int maxConcurrency) {
        if (depthLimit < 0) throw new IllegalArgumentException("Depth limit must be non-negative.");
        if (maxConcurrency <= 0) throw new IllegalArgumentException("Max concurrency must be positive.");
        this.depthLimit = depthLimit;
        this.semaphore = new Semaphore(maxConcurrency);
    }

    public void start(String startUrl) {
        try {
            URI uri = new URI(startUrl);
            this.startHost = uri.getHost();
            if (this.startHost == null) {
                System.err.println("Invalid start URL: cannot determine host.");
                return;
            }
        } catch (URISyntaxException e) {
            System.err.printf("Invalid start URL '%s': %s%n", startUrl, e.getMessage());
            return;
        }

        System.out.printf("Starting crawl at %s with depth %d and concurrency %d%n",
                startUrl, depthLimit, semaphore.availablePermits());

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Phaser phaser = new Phaser(1); // Start with 1 registered party (the initial task)
            seen.add(startUrl);
            executor.submit(() -> crawlUrl(new UrlDepth(startUrl, 0), executor, phaser));
            phaser.arriveAndAwaitAdvance(); // Wait for the initial party and all subsequent parties to deregister
            System.out.println("Crawl finished.");
        }
    }

    private void crawlUrl(UrlDepth currentUrlDepth, ExecutorService executor, Phaser phaser) {
        try {
            semaphore.acquire();
            try {
                // Fetch, Parse, and Extract
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(currentUrlDepth.url()))
                        .timeout(Duration.ofSeconds(5))
                        .GET().build();

                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

                if (resp.statusCode() == 200) {
                    System.out.printf("Crawled [%d] %s%n", currentUrlDepth.depth(), currentUrlDepth.url());
                    if (currentUrlDepth.depth() < depthLimit) {
                        extractLinks(resp.body(), currentUrlDepth.url())
                                .forEach(newLink -> submitCrawlTask(newLink, currentUrlDepth.depth() + 1, executor, phaser));
                    }
                } else {
                    System.out.printf("Failed (%d) %s%n", resp.statusCode(), currentUrlDepth.url());
                }
            } catch (IOException | InterruptedException | IllegalArgumentException e) {
                System.err.printf("Error crawling %s: %s%n", currentUrlDepth.url(), e.getMessage());
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve the interrupted status
        } finally {
            phaser.arriveAndDeregister(); // Signal that this task is complete
        }
    }

    private void submitCrawlTask(String url, int depth, ExecutorService executor, Phaser phaser) {
        // Only crawl URLs on the same host and that haven't been seen before.
        if (isSameHost(url) && seen.add(url)) {
            phaser.register(); // Register a new party to the phaser
            executor.submit(() -> crawlUrl(new UrlDepth(url, depth), executor, phaser));
        }
    }

    private Stream<String> extractLinks(String body, String baseUrl) {
        Document doc = Jsoup.parse(body, baseUrl);
        // Select all <a> tags with an href attribute
        return doc.select("a[href]")
                .stream()
                .map(link -> link.attr("abs:href")) // Get the absolute URL
                .filter(url -> !url.isBlank() && url.startsWith("http")); // Basic filter
    }

    private boolean isSameHost(String url) {
        try {
            return startHost.equalsIgnoreCase(new URI(url).getHost());
        } catch (URISyntaxException e) {
            return false; // Not a valid URI, so we can't crawl it.
        }
    }

    static void main(String[] args) {
        // Crawl the Java 22 JEP index page, which has a good number of internal links.
        var crawler = new VirtualThreadWebCrawler(2, 50);
        crawler.start("https://openjdk.org/jeps/525");
    }
}