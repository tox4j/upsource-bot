<#ftl strip_text=true strip_whitespace=true>
<#-- @ftlvariable name="" type="im.tox.upsourcebot.views.ViewConventions" -->

<#macro page title="">
    <#escape name as name?html>
    <!DOCTYPE html>
    <html>
    <head>
        <title>Upsource Bot | ${title}</title>
        <base href="${links.base}">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="assets/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/css/bootstrap-theme.min.css" rel="stylesheet">
    </head>
    <body>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="${links.base}">Upsource bot</a>
            </div>
            <ul class="nav navbar-nav">
                <li><a href="users">Users</a></li>
                <li><a href="#">Projects</a></li>
            </ul>
        </div>
    </nav>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <#nested>
            </div>
        </div>
    </div>
    <script src="assets/js/jquery-2.1.3.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    </body>
    </html>
    </#escape>
</#macro>