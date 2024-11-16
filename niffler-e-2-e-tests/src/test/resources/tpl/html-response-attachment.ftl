<html>
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment"  -->
    <head>
        <meta http-equiv="content-type" content="text/html; charset = UTF-8">
        <link type="text/css" href="https://yandex.st/highlightjs/8.0/styles/github.min.css" rel="stylesheet"/>
        <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/highlight.min.js"></script>
        <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/http.min.js"></script>
        <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/xml.min.js"></script>
        <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/json.min.js"></script>
        <script type="text/javascript">hljs.initHighlightingOnLoad();</script>

        <style>
            pre {
                white-space: pre-wrap;
            }
        </style>
    </head>
    <body>
        <h5>HTTP Response</h5>
        <div>
            <pre>
                <code>
                        <h5>Status code </h5>
                        ${data.responseCode}
                        <h5>Url</h5>
                        <#if data.url?has_content>
                             ${data.url}
                        </#if>
                        <h5>Headers</h5>
                        <#if data.headers?has_content>
                            <#list data.headers as name, header>
                                <p>${name}: ${header}
                            </#list>
                        </#if>
                        <h5>Body</h5>
                        <#if data.body?has_content>
                            <#outputformat "HTML"> ${data.body}</#outputformat>
                        </#if>
                        <h5>Cookies</h5>
                        <#if data.cookies?has_content>
                            <#list data.cookies as name, cookie>
                                <p>${name}: ${cookie}
                            </#list>
                        </#if>
                </code>
            </pre>
        </div>
    </body>
</html>