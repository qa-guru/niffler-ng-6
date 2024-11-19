package guru.qa.niffler.api.core.interceptor;

import guru.qa.niffler.api.core.RestClient;
import lombok.Getter;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class CustomHttpLoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final Logger log;


    public CustomHttpLoggingInterceptor() {
        this.log = LoggerFactory.getLogger(RestClient.class);
    }

    public CustomHttpLoggingInterceptor(Class<?> loggingClass) {
        this.log = LoggerFactory.getLogger(loggingClass);
    }

    private volatile Set<String> headersToRedact = Collections.emptySet();

    public void redactHeader(String name) {
        Set<String> newHeadersToRedact = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        newHeadersToRedact.addAll(headersToRedact);
        newHeadersToRedact.add(name);
        headersToRedact = newHeadersToRedact;
    }

    @Getter
    private volatile HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public CustomHttpLoggingInterceptor setLevel(HttpLoggingInterceptor.Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        StringBuilder logText = new StringBuilder();

        HttpLoggingInterceptor.Level logLevel = this.level;

        Request request = chain.request();
        if (logLevel == HttpLoggingInterceptor.Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = logLevel == HttpLoggingInterceptor.Level.BODY;
        boolean logHeaders = logBody || logLevel == HttpLoggingInterceptor.Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "\n[REQUEST]:\n--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logText.append(requestStartMessage).append('\n');

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logText.append("Content-Type: ").append(requestBody.contentType()).append('\n');
                }
                if (requestBody.contentLength() != -1) {
                    logText.append("Content-Length: ").append(requestBody.contentLength()).append('\n');
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logText.append(getHeader(headers, i)).append('\n');
                }
            }

            if (!logBody || !hasRequestBody) {
                logText.append("--> END ").append(request.method()).append('\n');
            } else if (bodyHasUnknownEncoding(request.headers())) {
                logText.append("--> END ").append(request.method()).append(" (encoded body omitted)\n");
            } else if (requestBody.isDuplex()) {
                logText.append("--> END ").append(request.method()).append(" (duplex request body omitted)\n");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                logText.append('\n');
                if (isPlaintext(buffer)) {
                    logText.append(buffer.readString(charset)).append('\n');
                    logText.append("--> END ").append(request.method())
                            .append(" (").append(requestBody.contentLength()).append("-byte body)\n");
                } else {
                    logText.append("--> END ").append(request.method())
                            .append(" (binary ").append(requestBody.contentLength()).append("-byte body omitted)\n");
                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logText.append("<-- HTTP FAILED: ").append(e).append('\n');
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        var responseStartMessage = "\n[RESPONSE]:\n<-- ";
        logText.append(responseStartMessage)
                .append(response.code())
                .append((response.message().isEmpty() ? "" : ' ' + response.message()))
                .append(' ').append(response.request().url())
                .append(" (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')' + "\n");


        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logText.append(getHeader(headers, i)).append('\n');
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logText.append("<-- END HTTP");
            } else if (bodyHasUnknownEncoding(response.headers())) {
                logText.append("<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.getBuffer();

                Long gzippedLength = null;
                if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                    gzippedLength = buffer.size();
                    try (GzipSource gzippedResponseBody = new GzipSource(buffer.clone())) {
                        buffer = new Buffer();
                        buffer.writeAll(gzippedResponseBody);
                    }
                }

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    logText.append('\n').append("<-- END HTTP (binary ").append(buffer.size()).append("-byte body omitted)\n");
                    return response;
                }

                if (contentLength != 0) {
                    logText.append('\n').append(buffer.clone().readString(charset));
                }

                if (gzippedLength != null) {
                    logText.append("<-- END HTTP (%d-byte, %d-gzipped-byte body)%s".formatted(buffer.size(), gzippedLength, '\n'));
                } else {
                    logText.append("<-- END HTTP (" + buffer.size() + "-byte body)\n");
                }
            }
        }
        log.info(logText.toString());
        return response;
    }

    @Nonnull
    private String getHeader(@Nonnull Headers headers, int i) {
        String value = headersToRedact.contains(headers.name(i)) ? "██" : headers.value(i);
        return headers.name(i) + ": " + value;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }

}
