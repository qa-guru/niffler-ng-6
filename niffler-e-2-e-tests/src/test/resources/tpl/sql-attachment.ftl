<html>
<#-- @ftlvariable name="data" type="guru.qa.niffler.data.logging.SqlAttachmentData"  -->
    <head>
        <meta http-equiv="content-type" content="text/html; charset = UTF-8">
        <link type="text/css" href="https://yandex.st/highlightjs/8.0/styles/github.min.css" rel="stylesheet"/>
        <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/highlight.min.js"></script>
        <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/sql.min.js"></script>
        <script type="text/javascript">hljs.initHighlightingOnLoad();</script>

        <style>
            pre {
                white-space: pre-wrap;
            }
        </style>
    </head>
    <body>
        <h5>SQL Query</h5>
        <div>
            <pre>
                <code>
                    ${data.sql}
                </code>
            </pre>
        </div>
    </body>
</html>